package uk.ac.ncl.cs.group1.clientapi1.core;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi1.Register;
import uk.ac.ncl.cs.group1.clientapi1.Resource;
import uk.ac.ncl.cs.group1.clientapi1.TTPURL;
import uk.ac.ncl.cs.group1.clientapi1.clientserver.CustomerErrorHandler;
import uk.ac.ncl.cs.group1.clientapi1.clientserver.GsonHelper;
import uk.ac.ncl.cs.group1.clientapi.core.*;
import uk.ac.ncl.cs.group1.clientapi1.entity.RegisterResponseEntity;
import uk.ac.ncl.cs.group1.clientapi1.util.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi1.util.KeyGenerator;

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
        restTemplate.setErrorHandler(new CustomerErrorHandler());
    }
    @Override
    public KeyPairStore register(String id) {
        log.info("register begin "+ id);
        //register
        String newUrl = TTPURL.registerUrl+"/"+id+".ignore";
        ResponseEntity<String> infoEntity = restTemplate.postForEntity(newUrl,null, String.class);
        if (infoEntity.getStatusCode()!= HttpStatus.OK){
            throw new IllegalArgumentException(infoEntity.getBody());
        }
        log.info("register end "+ id);
        //get private key and public key
        Gson gson = GsonHelper.customGson;
        RegisterResponseEntity entity1 = gson.fromJson(infoEntity.getBody(),RegisterResponseEntity.class);
        PublicKey publicKey = KeyGenerator.unserializedPublicKey(Base64Coder.decode(entity1.getPublicKey()));
        PrivateKey privateKey = KeyGenerator.unserializeedPrivateKey(Base64Coder.decode(entity1.getPrivateKey()));
        log.info("get key "+ id);
        return new KeyPairStore(id,privateKey,publicKey);
    }
}
