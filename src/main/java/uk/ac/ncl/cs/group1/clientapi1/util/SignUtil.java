package uk.ac.ncl.cs.group1.clientapi1.util;

import uk.ac.ncl.cs.group1.clientapi.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 *
 */
public class SignUtil {
    public static byte[] sign(PrivateKey privateKey,byte[] bytes){
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE,privateKey);
            return cipher.doFinal(bytes);
            //todo finish exception
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] sign(byte[] privateKey,byte[] bytes){
        return sign(KeyGenerator.unserializeedPrivateKey(privateKey),bytes);
    }

    public static byte[] unSign(PublicKey publicKey,byte[] bytes){
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.DECRYPT_MODE,publicKey);
            return cipher.doFinal(bytes);
            //todo finish exception
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] unSign(byte[] publicKey,byte[] bytes){
      return unSign(KeyGenerator.unserializedPublicKey(publicKey),bytes);
    }
}
