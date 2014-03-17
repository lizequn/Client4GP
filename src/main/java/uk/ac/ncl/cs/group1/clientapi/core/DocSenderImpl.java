package uk.ac.ncl.cs.group1.clientapi.core;

import com.google.gson.Gson;
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
import uk.ac.ncl.cs.group1.clientapi.util.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.util.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.util.SignUtil;

import java.io.File;
import java.io.IOException;
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
    public UUID sendDoc(File file, String address) throws IOException {
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
        ResponseEntity<Phase1ResponseEntity> responseEntity = restTemplate.postForEntity(TTPURL.phase1RequestUrl, req, Phase1ResponseEntity.class);
        //todo
        if (responseEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        Phase1ResponseEntity entity = responseEntity.getBody();
        return entity.getUuid();
    }

    @Override
    public void receiveReceipt(final long intervalTime,final UUID uuid,final ReceiptCallBack callBack) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i = 10;
                while(i>0){
                    log.info("begin receive phase5");
                    String myUrl = TTPURL.phase5SigUrl+"/"+uuid;
                    ResponseEntity<String> entity1 = restTemplate.postForEntity(myUrl,null,String.class);
                    if(entity1.getStatusCode() != HttpStatus.OK){
                        try {
                            Thread.sleep(intervalTime);
                        } catch (InterruptedException ignored) {
                        }
                        i++;
                        continue;
                    }
                    Gson gson = GsonHelper.customGson;
                    Phase3RequestEntity result = gson.fromJson(entity1.getBody(), Phase3RequestEntity.class);
                    callBack.getReceipt(Base64Coder.decode(result.getReceiptHash()),uuid.toString());
                    break;
                }
            }
        };
        new Thread(runnable).start();
    }
}
