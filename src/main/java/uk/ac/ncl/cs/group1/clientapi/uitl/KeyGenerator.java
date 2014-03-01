package uk.ac.ncl.cs.group1.clientapi.uitl;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * get public private Key Pairs based on DSA algorithms provided by SUN
 * serialize and unserialize public and private key
 * @author ZequnLi
 */
public class KeyGenerator {
    /**
     *  generate new key pairs
     * @return  KeyPair
     */
    public static KeyPair generateNewKeyPairs(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //get a strong random number
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyPairGenerator.initialize(2048, random);
            KeyPair kp = keyPairGenerator.genKeyPair();

            return kp;
        } catch (NoSuchProviderException e) {
            //never happens
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            //never happens
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] serializePublicKey(PublicKey key){
        return key.getEncoded();
    }

    public static byte[] serializePrivateKey(PrivateKey key){
        return key.getEncoded();
    }

    public static PublicKey unserializedPublicKey(byte[] bytes){
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return  keyFactory.generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            //never happens
            e.printStackTrace();
        }  catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("bytes are not in right format");
        }
        return null;
    }

    public static PrivateKey unserializeedPrivateKey(byte[] bytes){

        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return  keyFactory.generatePrivate(privKeySpec);
        } catch (NoSuchAlgorithmException e) {
            //never happens
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("bytes are not in right format");
        }
        return null;
    }
}
