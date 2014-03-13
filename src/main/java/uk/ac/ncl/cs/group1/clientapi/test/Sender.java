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
import uk.ac.ncl.cs.group1.clientapi.entity.Phase1RequestEntity;
import uk.ac.ncl.cs.group1.clientapi.entity.Phase1ResponseEntity;
import uk.ac.ncl.cs.group1.clientapi.entity.RegisterResponseEntity;
import uk.ac.ncl.cs.group1.clientapi.uitl.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.uitl.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.uitl.KeyGenerator;
import uk.ac.ncl.cs.group1.clientapi.uitl.SignUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

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
    private final static String initRequestUrl = url+"/phase1";
    private final static String getSigUrl= url+"/phase5";
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
        String newUrl = registerUrl+"/"+this.name;
        ResponseEntity<RegisterResponseEntity> infoEntity = restTemplate.postForEntity(newUrl,null, RegisterResponseEntity.class);
        if (infoEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        log.info("1-->register end");
        //get private key and public key
        RegisterResponseEntity entity1 = infoEntity.getBody();
//        log.info(entity1.getPrivateKey().length);
        publicKey = KeyGenerator.unserializedPublicKey(entity1.getPublicKey());
        privateKey = KeyGenerator.unserializeedPrivateKey(entity1.getPrivateKey());
        log.info("2-->get key");
    }

    private UUID begin() throws IOException, NoSuchAlgorithmException {
        log.info("begin");
        log.info("generate Header");
        HttpHeaders headers = new HttpHeaders();
        headers.add("name",name);
        headers.add("auth_token", Base64Coder.encode(SignUtil.sign(privateKey, name.getBytes())));

        //initRegister
        log.info("phase1 begin");
        File file = new File("testfile");
        String unsignedHash = HashUtil.calHashFromFile(file);
        byte[] bytes = SignUtil.sign(privateKey,unsignedHash.getBytes());
        System.out.println(Base64Coder.encode(bytes));
       // SignUtil.unSign(publicKey,bytes);
        MultiValueMap<String,Object> pairs = new LinkedMultiValueMap<>();
        pairs.add("fromAddress",this.name);
        pairs.add("toAddress",this.destination);
        pairs.add("name", "testfile");
        pairs.add("signedHash",Base64Coder.encode(bytes));
        pairs.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String,Object>> req = new HttpEntity<>(pairs,headers);
        ResponseEntity<Phase1ResponseEntity> responseEntity = restTemplate.postForEntity(initRequestUrl, req, Phase1ResponseEntity.class);

        if (responseEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        Phase1ResponseEntity entity = responseEntity.getBody();
        log.info("phase1 end");
        return entity.getUuid();


    }

    public boolean receive(UUID uuid) {
        log.info("check 1");
        HttpHeaders headers = new HttpHeaders();
        headers.add("name",name);
        headers.add("auth_token", Base64Coder.encode(SignUtil.sign(privateKey, name.getBytes())));
        HttpEntity entity = new HttpEntity(headers);
        String myUrl = getSigUrl+"/"+uuid;
        ResponseEntity<String> entity1 = restTemplate.postForEntity(myUrl,entity,String.class);
        if(entity1.getStatusCode() != HttpStatus.OK){
            return false;
        }
        String result = entity1.getBody();
        System.out.println(result);
        return true;
    }

    @Override
    public void run() {
        try {
            UUID uuid = begin();
            while(!receive(uuid)){
                Thread.sleep(1000);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InterruptedException ignored) {
        }


    }
}
