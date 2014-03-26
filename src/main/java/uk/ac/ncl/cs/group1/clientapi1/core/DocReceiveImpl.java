package uk.ac.ncl.cs.group1.clientapi1.core;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi1.DocReceive;
import uk.ac.ncl.cs.group1.clientapi1.Resource;
import uk.ac.ncl.cs.group1.clientapi1.TTPURL;
import uk.ac.ncl.cs.group1.clientapi1.callback.CheckCallBack;
import uk.ac.ncl.cs.group1.clientapi1.callback.FileStore;
import uk.ac.ncl.cs.group1.clientapi1.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi1.clientserver.GsonHelper;
import uk.ac.ncl.cs.group1.clientapi1.clientserver.MyRestTemplate;
import uk.ac.ncl.cs.group1.clientapi.core.*;
import uk.ac.ncl.cs.group1.clientapi1.entity.GetMyExchangeResponseEntity;
import uk.ac.ncl.cs.group1.clientapi1.entity.Phase1RequestEntity;
import uk.ac.ncl.cs.group1.clientapi1.entity.Phase3RequestEntity;
import uk.ac.ncl.cs.group1.clientapi1.entity.PublicKeyEntity;
import uk.ac.ncl.cs.group1.clientapi1.util.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi1.util.HashUtil;
import uk.ac.ncl.cs.group1.clientapi1.util.SignUtil;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ZequnLi
 *         Date: 14-3-16
 */
public class DocReceiveImpl extends Resource implements DocReceive {
    private final KeyPairStore keyPairStore;
    private final RestTemplate restTemplate;
    public DocReceiveImpl(KeyPairStore keyPairStore){
        this.keyPairStore = keyPairStore;
        restTemplate = MyRestTemplate.getTemplate(keyPairStore.getId(), Base64Coder.encode(SignUtil.sign(keyPairStore.getPrivateKey(), keyPairStore.getId().getBytes())));
    }

    @Override
    public List<UUID> checkExistCommunication() {
        log.info("get my exchange "+keyPairStore.getId());
        String myUrl = TTPURL.getMyExchangeUrl+"/"+keyPairStore.getId()+".ignore";
        ResponseEntity<String> response =  restTemplate.postForEntity(myUrl,null, String.class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException(response.getBody());
        }
        GetMyExchangeResponseEntity exchangeResponseEntity = GsonHelper.customGson.fromJson(response.getBody(),GetMyExchangeResponseEntity.class);
        List<UUID> list = exchangeResponseEntity.getLists();
//        restTemplate.postForEntity(myUrl,req, GetMyExchangeResponseEntity.class);
        if(list.size()<=0){
            return null;
        }
        log.info("number "+list.size());
        return list;
    }

    @Override
    public List<UUID> asyCheckExistCommunication(final CheckCallBack callBack,final long intervalTime,final int times) {
        final AtomicReference<List<UUID>> result = new AtomicReference<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i =times;
                while(i>0)  {
                    log.info("get my exchange "+keyPairStore.getId());
                    String myUrl = TTPURL.getMyExchangeUrl+"/"+keyPairStore.getId()+".ignore";
                    ResponseEntity<String> response =  restTemplate.postForEntity(myUrl,null, String.class);
                    if(response.getStatusCode() != HttpStatus.OK){
                        throw new IllegalStateException(response.getBody());
                    }
                    GetMyExchangeResponseEntity exchangeResponseEntity =GsonHelper.customGson.fromJson(response.getBody(),GetMyExchangeResponseEntity.class);
                    List<UUID> list = exchangeResponseEntity.getLists();
                    result.set(list);
                    if(list.size()<=0){
                        try {
                            Thread.sleep(intervalTime);
                        } catch (InterruptedException ignored) {
                        }
                        i++;
                        continue;
                    }
                    log.info("number "+list.size());
                    callBack.getUUID(list);
                    break;
                }
            }
        };
        new Thread(runnable).start();
        return result.get();
    }

    @Override
    public UUID getFileAndReceipt(UUID uuid,FileStore fileStore, ReceiptCallBack receiptCallBack) {
        log.info("begin get file and receipt ID:"+uuid);
        String myUrl1 = TTPURL.phase2Url+"/"+uuid;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(myUrl1,null,String.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException(responseEntity.getBody());
        }
        Phase1RequestEntity requestEntity = GsonHelper.customGson.fromJson(responseEntity.getBody(),Phase1RequestEntity.class

        );


        byte[] bytes = Base64Coder.decode(requestEntity.getSignedHash());
        String hashedBytes = HashUtil.calHash(bytes);
        byte[] sigB = SignUtil.sign(keyPairStore.getPrivateKey(),hashedBytes.getBytes());
        String myUrl2 = TTPURL.phase3Url+"/"+uuid;
        Phase3RequestEntity entity = new Phase3RequestEntity();
        entity.setReceiptHash(Base64Coder.encode(sigB));
        ResponseEntity<byte[]> result =  restTemplate.postForEntity(myUrl2,entity,byte[].class);
        if(result.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException(new String(result.getBody()));
        }
        System.out.println(result.getHeaders());
        String filename =result.getHeaders().get("FileName").get(0);
        receiptCallBack.getReceipt(bytes,filename+".rec");
        fileStore.storeFile(result.getBody(),filename);
        log.info("finish ID "+uuid);
        return uuid;
    }

    @Override
    public PublicKeyEntity getPublicKey(String id) {
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
    public boolean verifyFileWithReceipt(File file, byte[] receipt,PublicKeyEntity entity) throws IOException {
        String recHash = new String(SignUtil.unSign(Base64Coder.decode(entity.getPublicKey()),receipt));
        String fileHash = HashUtil.calHashFromFile(file);
        return recHash.equals(fileHash);
    }

    @Override
    public boolean verifyFileWithReceipt(File file, File receipt,PublicKeyEntity entity) throws IOException {
        FileInputStream stream = new FileInputStream(receipt);
        byte[] bytes = IOUtils.toByteArray(stream);
        stream.close();
        return verifyFileWithReceipt(file,bytes,entity);
    }

    @Override
    public boolean resolve(UUID uuid, FileStore fileStore) {
        String url = TTPURL.receiverResolveUrl+"/"+ uuid.toString();
        ResponseEntity<byte[]> result =  restTemplate.getForEntity(url, byte[].class);
        if(result.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException(new String(result.getBody()));
        }
        System.out.println(result.getHeaders());
        String filename =result.getHeaders().get("FileName").get(0);
        fileStore.storeFile(result.getBody(),filename);
        return true;
    }
}
