package uk.ac.ncl.cs.group1.clientapi.core;

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
import uk.ac.ncl.cs.group1.clientapi.TTPUrl;
import uk.ac.ncl.cs.group1.clientapi.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.clientserver.MyRestTemplate;
import uk.ac.ncl.cs.group1.clientapi.entity.Phase1ResponseEntity;
import uk.ac.ncl.cs.group1.clientapi.uitl.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.uitl.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.uitl.SignUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 15/03/14
 */
public class DocSenderImpl extends Resource implements DocSender {
    private RestTemplate restTemplate;


    @Override
    public UUID sendDoc(KeyPairStore keyPairStore, File file, String address) throws IOException {
        log.info("send doc begin "+ keyPairStore.getId());


        if(restTemplate == null){
            restTemplate = MyRestTemplate.getTemplate(keyPairStore.getId(), Base64Coder.encode(SignUtil.sign(keyPairStore.getPrivateKey(), keyPairStore.getId().getBytes())));
        }

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
        ResponseEntity<Phase1ResponseEntity> responseEntity = restTemplate.postForEntity(TTPUrl.phase1RequestUrl, req, Phase1ResponseEntity.class);
        //todo
        if (responseEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        Phase1ResponseEntity entity = responseEntity.getBody();
        return entity.getUuid();
    }

    @Override
    public void receiveReceipt(long intervalTime, UUID uuid, ReceiptCallBack callBack) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
