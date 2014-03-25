package uk.ac.ncl.cs.group1.clientapi.core;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.DocSender;
import uk.ac.ncl.cs.group1.clientapi.Resource;
import uk.ac.ncl.cs.group1.clientapi.TTPURL;
import uk.ac.ncl.cs.group1.clientapi.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.clientserver.GsonHelper;
import uk.ac.ncl.cs.group1.clientapi.clientserver.MyRestTemplate;
import uk.ac.ncl.cs.group1.clientapi.entity.Phase1ResponseEntity;
import uk.ac.ncl.cs.group1.clientapi.entity.Phase3RequestEntity;
import uk.ac.ncl.cs.group1.clientapi.entity.PublicKeyEntity;
import uk.ac.ncl.cs.group1.clientapi.util.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.util.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.util.KeyGenerator;
import uk.ac.ncl.cs.group1.clientapi.util.SignUtil;

import java.io.*;
import java.security.PublicKey;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 15/03/14
 */
public class DocSenderImpl extends Resource implements DocSender {
    private final RestTemplate restTemplate;
    private final KeyPairStore keyPairStore;
    public DocSenderImpl(KeyPairStore keyPairStore){
        this.keyPairStore = keyPairStore;
        restTemplate = MyRestTemplate.getTemplate(keyPairStore.getId(), Base64Coder.encode(SignUtil.sign(keyPairStore.getPrivateKey(), keyPairStore.getId().getBytes())));
    }

    @Override
    public UUID sendDoc(File file, String address,boolean email) throws IOException {
        log.info("send doc begin "+ keyPairStore.getId());
        //initRegister
        log.info("phase1 begin");

        String unsignedHash = HashUtil.calHashFromFile(file);
        byte[] bytes = SignUtil.sign(keyPairStore.getPrivateKey(),unsignedHash.getBytes());
        MultiValueMap<String,Object> pairs = new LinkedMultiValueMap<>();
        pairs.add("fromAddress",keyPairStore.getId());
        pairs.add("toAddress",address);
        pairs.add("name", file.getName());
        pairs.add("signedHash",Base64Coder.encode(bytes));
        pairs.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String,Object>> req = new HttpEntity<>(pairs);
        String url = TTPURL.phase1RequestUrl4Normal;
        if(email){
            url = TTPURL.phase1RequestUrl4Email;
        }
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, req, String.class);
        if (responseEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException(responseEntity.getBody());
        }
        Phase1ResponseEntity entity = GsonHelper.customGson.fromJson(responseEntity.getBody(),Phase1ResponseEntity.class);
        return entity.getUuid();
    }

    @Override
    public void receiveReceipt(final long intervalTime,final int times,final UUID uuid,final ReceiptCallBack callBack) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i = times;
                while(i>0){
                    System.out.println("waiting receipt"+times);
                    log.info("begin receive phase5");
                    String myUrl = TTPURL.phase5SigUrl+"/"+uuid;
                    ResponseEntity<String> entity1 = restTemplate.postForEntity(myUrl,null,String.class);
                    if(entity1.getStatusCode() == HttpStatus.NOT_MODIFIED){
                        try {
                            Thread.sleep(intervalTime);
                        } catch (InterruptedException ignored) {
                        }
                        i++;
                        continue;
                    }
                    if(entity1.getStatusCode() != HttpStatus.OK){
                        throw new IllegalStateException(entity1.getBody());
                    }
                    Gson gson = GsonHelper.customGson;
                    Phase3RequestEntity result = gson.fromJson(entity1.getBody(), Phase3RequestEntity.class);
                    callBack.getReceipt(Base64Coder.decode(result.getReceiptHash()),uuid.toString());
                    System.out.println("Finish phase5");
                    log.info("finish phase5");
                    break;
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public PublicKeyEntity getPublicKey(String id) {
        //initRegister
        log.info("get public key "+id);
        String url = TTPURL.getPublicKeyUrl+"/"+id+".ignore";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        if (responseEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException(responseEntity.getBody());
        }
        Gson gson = GsonHelper.customGson;
        return gson.fromJson(responseEntity.getBody(),PublicKeyEntity.class);
    }

    @Override
    public boolean checkSignature(File file, File receipt, PublicKeyEntity entity) {
        try {
            FileInputStream stream = new FileInputStream(receipt);
            byte[] bytes = IOUtils.toByteArray(stream);
            stream.close();
            return checkSignature(file, bytes, entity);
        } catch (IOException e) {
            throw new IllegalArgumentException("file not exist");
        }
    }

    @Override
    public boolean checkSignature(File file, byte[] receipt, PublicKeyEntity entity) {
        try {
            String str = HashUtil.calHashFromFile(file);
            byte[] bytes = SignUtil.sign(keyPairStore.getPrivateKey(),str.getBytes());
            String result = HashUtil.calHash(bytes);
            PublicKey key = KeyGenerator.unserializedPublicKey(Base64Coder.decode(entity.getPublicKey()));
            String getResult = new String(SignUtil.unSign(key,receipt));
            return result.equals(getResult);
        } catch (IOException e) {
            throw new IllegalArgumentException("file not exist");
        }
    }

    @Override
    public boolean resolve(UUID uuid, ReceiptCallBack callBack) {
        String url = TTPURL.senderResolveUrl+"/"+ uuid.toString();
        ResponseEntity<String> result =  restTemplate.getForEntity(url,String.class);
        if(result.getStatusCode() != HttpStatus.OK){
            log.info(result.getBody());
            throw new IllegalStateException(result.getBody());
        }
        Gson gson = GsonHelper.customGson;
        Phase3RequestEntity phase3RequestEntity = gson.fromJson(result.getBody(),Phase3RequestEntity.class);
        String rec = phase3RequestEntity.getReceiptHash();
        callBack.getReceipt(Base64Coder.decode(rec),uuid.toString());
        return true;
    }
}
