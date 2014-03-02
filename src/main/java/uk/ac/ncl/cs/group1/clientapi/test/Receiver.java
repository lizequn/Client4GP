package uk.ac.ncl.cs.group1.clientapi.test;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.Entity.*;
import uk.ac.ncl.cs.group1.clientapi.uitl.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.uitl.KeyGenerator;
import uk.ac.ncl.cs.group1.clientapi.uitl.SignUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

/**
 * @author ZequnLi
 *         Date: 14-3-2
 */
public class Receiver implements Runnable {
    private static Logger log = Logger.getLogger(Receiver.class);
    private final String name;
    private RestTemplate restTemplate;
    private final static String url = "http://localhost:8080";
    private final static String registerUrl = url+"/register";
    private final static String initRequestUrl = url+"/initRequest";
    private final static String getMyExchangeUrl = url+"/getmyexchange" ;
    private final static String phrase2Url = url+"/phrase2";
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private HttpHeaders headers;
    public Receiver(String name){
        this.name = name;
        restTemplate = new RestTemplate();
        register();
    }

    public void register(){
        log.info("register");
        //register
        RegisterEntity entity = new RegisterEntity();
        entity.setName(name);
        ResponseEntity<RegisterInfoEntity> infoEntity = restTemplate.postForEntity(registerUrl, entity, RegisterInfoEntity.class);
        if (infoEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        RegisterInfoEntity entity2 = infoEntity.getBody();
//        log.info(entity1.getPrivateKey().length);
        publicKey = KeyGenerator.unserializedPublicKey(entity2.getPublicKey());
        privateKey = KeyGenerator.unserializeedPrivateKey(entity2.getPrivateKey());
        log.info("generate Header");
        headers = new HttpHeaders();
        headers.add("name",name);
        headers.add("auth_token", Base64Coder.encode(SignUtil.sign(privateKey, name.getBytes())));
        log.info(headers.get("auth_token"));
    }

    public boolean begin(){
        log.info("begin");
        log.info("get my exchange");
        String myUrl = getMyExchangeUrl+"/"+name;
        HttpEntity req = new HttpEntity(null,headers);
        ResponseEntity<GetMyExchangeResponseEntity> response =  restTemplate.postForEntity(myUrl,req, GetMyExchangeResponseEntity.class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException();
        }
        GetMyExchangeResponseEntity exchangeResponseEntity = response.getBody();
        List<UUID> list = exchangeResponseEntity.getLists();
//        restTemplate.postForEntity(myUrl,req, GetMyExchangeResponseEntity.class);
        log.info("get my exchange end");
        if(list.size()<=0){
            return false;
        }

        log.info("begin phrase2");
        UUID uuid = list.get(0);
        log.info("ID:"+uuid);
        String myUrl1 = phrase2Url+"/"+name+"/"+uuid;
        ResponseEntity<InitRequestEntity> responseEntity = restTemplate.postForEntity(myUrl1,req,InitRequestEntity.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException();
        }
        InitRequestEntity requestEntity = responseEntity.getBody();
        log.info("finish phrase2");
        return true;
    }

    @Override
    public void run() {
        int i =0;
        while (!begin())  {
            i++;
            log.info("counter:"+i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }

    }
}
