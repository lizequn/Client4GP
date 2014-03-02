package uk.ac.ncl.cs.group1.clientapi.test;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.Entity.*;
import uk.ac.ncl.cs.group1.clientapi.uitl.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.uitl.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.uitl.KeyGenerator;
import uk.ac.ncl.cs.group1.clientapi.uitl.SignUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Auther: Li Zequn
 * Date: 01/03/14
 */
public class Sender implements Runnable {
    private static Logger log = Logger.getLogger(Sender.class);
    private final String name;
    private RestTemplate restTemplate;
    private final String destination;
    private final static String url = "http://localhost:8080";
    private final static String registerUrl = url+"/register";
    private final static String initRequestUrl = url+"/initRequest";
    private PublicKey publicKey;
    private PrivateKey privateKey;
    public Sender(String name,String destination){
        this.name=name;
        this.destination = destination;
        restTemplate = new RestTemplate();
        register();
    }
    private void register(){
        log.info("1-->register begin");
        //register
        RegisterEntity entity = new RegisterEntity();
        entity.setName(name);
        ResponseEntity<RegisterInfoEntity> infoEntity = restTemplate.postForEntity(registerUrl, entity, RegisterInfoEntity.class);
        if (infoEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        log.info("1-->register end");
        //get private key and public key
        RegisterInfoEntity entity1 = infoEntity.getBody();
//        log.info(entity1.getPrivateKey().length);
        publicKey = KeyGenerator.unserializedPublicKey(entity1.getPublicKey());
        privateKey = KeyGenerator.unserializeedPrivateKey(entity1.getPrivateKey());
        log.info("2-->get key");
    }

    private void begin() throws IOException, NoSuchAlgorithmException {
        log.info("begin");
        log.info("generate Header");
        HttpHeaders headers = new HttpHeaders();
        headers.add("name",name);
        headers.add("auth_token", Base64Coder.encode(SignUtil.sign(privateKey, name.getBytes())));

        //initRegister
        log.info("3-->init Register begin");
        File file = new File("testfile");
        String unsignedHash = HashUtil.calHashFromFile(file);
        byte[] bytes = SignUtil.sign(privateKey,unsignedHash.getBytes());
        InitRequestEntity entity2 = new InitRequestEntity();
        entity2.setFrom(name);
        entity2.setTo(destination);
        entity2.setSignedHash(bytes);
        HttpEntity req = new HttpEntity<>(entity2, headers);
        ResponseEntity<InitResponseEntity> responseEntity = restTemplate.postForEntity(initRequestUrl, req, InitResponseEntity.class);

        if (responseEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        InitResponseEntity initResponseEntity = responseEntity.getBody();
        log.info("3-->init Register end");

        //uploadFile
        log.info("4-->upload begin");
        String uploadFileUrl = url+"/upload/"+initResponseEntity.getUrl();
        MultiValueMap<String,Object> pairs = new LinkedMultiValueMap<>();
        pairs.add("name","testfile");
        pairs.add("file",new FileSystemResource(file));
        pairs.add("uuid",initResponseEntity.getUrl());
        HttpEntity<MultiValueMap<String,Object>> req1 = new HttpEntity<>(pairs,headers);
        ResponseEntity<UploadSuccessEntity> responseEntity1 = restTemplate.postForEntity(uploadFileUrl,req1,UploadSuccessEntity.class,headers);

        if (responseEntity1.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        log.info("4-->upload end");
        log.info(responseEntity1.getBody().getInfo());
    }

    @Override
    public void run() {
        try {
            begin();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
