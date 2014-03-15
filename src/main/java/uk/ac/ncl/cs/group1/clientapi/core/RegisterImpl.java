package uk.ac.ncl.cs.group1.clientapi.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.Register;
import uk.ac.ncl.cs.group1.clientapi.Resource;
import uk.ac.ncl.cs.group1.clientapi.TTPUrl;
import uk.ac.ncl.cs.group1.clientapi.entity.RegisterResponseEntity;
import uk.ac.ncl.cs.group1.clientapi.uitl.KeyGenerator;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Auther: Li Zequn
 * Date: 15/03/14
 */
public class RegisterImpl extends Resource implements Register {
    private RestTemplate restTemplate;
    public RegisterImpl(){
        restTemplate = new RestTemplate();
    }
    @Override
    public KeyPairStore register(String id) {
        log.info("register begin "+ id);
        //register
        String newUrl = TTPUrl.registerUrl+"/"+id;
        ResponseEntity<RegisterResponseEntity> infoEntity = restTemplate.postForEntity(newUrl,null, RegisterResponseEntity.class);
        //todo
        if (infoEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException("1");
        }
        log.info("1-->register end "+ id);
        //get private key and public key
        RegisterResponseEntity entity1 = infoEntity.getBody();
        PublicKey publicKey = KeyGenerator.unserializedPublicKey(entity1.getPublicKey());
        PrivateKey privateKey = KeyGenerator.unserializeedPrivateKey(entity1.getPrivateKey());
        log.info("get key "+ id);
        return new KeyPairStore(id,privateKey,publicKey);
    }
}
