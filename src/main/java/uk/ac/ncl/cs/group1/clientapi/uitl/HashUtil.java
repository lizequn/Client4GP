package uk.ac.ncl.cs.group1.clientapi.uitl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash util based on sha-256
 * @author ZequnLi
 *
 */
public class HashUtil {
    /**
     * get hash code by giving string
     * @param s
     * @return HEX HashCode
     */
    public static String calHash(String s){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(s.getBytes());
            byte [] hashCode = messageDigest.digest();
            //change to HEX
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashCode.length; i++) {
                sb.append(Integer.toString((hashCode[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            //never happens
            e.printStackTrace();
        }
        return null;
    }

    public static String calHash(byte [] s){
        return calHash(new String(s));
    }

    public static String calHashFromFile(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }

        byte[] mdbytes = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
