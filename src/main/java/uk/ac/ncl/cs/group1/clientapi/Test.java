package uk.ac.ncl.cs.group1.clientapi;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.Entity.*;
import uk.ac.ncl.cs.group1.clientapi.uitl.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.uitl.KeyGenerator;
import uk.ac.ncl.cs.group1.clientapi.uitl.SignUtil;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ZequnLi
 *         Date: 14-2-23
 */
public class Test {
    private final static Logger log = Logger.getLogger(Test.class);
    private final static String name = "test1";
    private final static String destination = "test2";
    private final static String url = "http://localhost:8080";
    private final static String registerUrl = url+"/register";
    private final static String initRequestUrl = url+"/initRequest";
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate();
        log.info("begin");
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
        PublicKey publicKey = KeyGenerator.unserializedPublicKey(entity1.getPublicKey());
        PrivateKey privateKey = KeyGenerator.unserializeedPrivateKey(entity1.getPrivateKey());
        log.info("2-->get key");
        //initRegister
        log.info("3-->init Register begin");
        File file = new File("testfile");
        String unsignedHash = HashUtil.calHashFromFile(file);
        byte[] bytes = SignUtil.sign(privateKey,unsignedHash.getBytes());
        InitRequestEntity entity2 = new InitRequestEntity();
        entity2.setFrom(name);
        entity2.setTo(destination);
        entity2.setSignedHash(bytes);

        ResponseEntity<InitResponseEntity> responseEntity = restTemplate.postForEntity(initRequestUrl, entity2, InitResponseEntity.class);
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
        ResponseEntity<UploadSuccessEntity> responseEntity1 = restTemplate.postForEntity(uploadFileUrl,pairs,UploadSuccessEntity.class);

        if (responseEntity1.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        log.info("4-->upload end");
        log.info(responseEntity1.getBody().getInfo());


    }
}
