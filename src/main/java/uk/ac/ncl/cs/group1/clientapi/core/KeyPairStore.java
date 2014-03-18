package uk.ac.ncl.cs.group1.clientapi.core;

import uk.ac.ncl.cs.group1.clientapi.util.KeyGenerator;
import uk.ac.ncl.cs.group1.clientapi.util.SignUtil;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
//todo
public class KeyPairStore {
    private final String id;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public KeyPairStore(String id,PrivateKey privateKey, PublicKey publicKey) {
        this.id = id;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    public void store2File(File id,File publicName, File privateName){

        if(publicName.exists()){
            throw new IllegalArgumentException("file already exist");
        }
        if(privateName.exists()){
            throw new IllegalArgumentException("file already exist");
        }
        if(id.exists()){
            throw new IllegalArgumentException("file already exist");
        }
        try {
            publicName.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(publicName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(publicKey.getEncoded());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            privateName.createNewFile();
            FileOutputStream outputStream1 = new FileOutputStream(privateName);
            BufferedOutputStream bufferedOutputStream1 = new BufferedOutputStream(outputStream1);
            bufferedOutputStream1.write(privateKey.getEncoded());
            bufferedOutputStream1.flush();
            bufferedOutputStream1.close();
            id.createNewFile();
            FileOutputStream outputStream2 = new FileOutputStream(id);
            BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(outputStream2);
            bufferedOutputStream2.write(this.id.getBytes());
            bufferedOutputStream2.flush();
            bufferedOutputStream2.close();
            outputStream2.close();
            outputStream.close();
            outputStream1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static KeyPairStore getFromFile(File id,File publicName,File privateName){
        if(!publicName.exists()){
            throw new IllegalArgumentException("file not exist");
        }
        if(!privateName.exists()){
            throw new IllegalArgumentException("file not exist");
        }
        if(!id.exists()){
            throw new IllegalArgumentException("file not exist");
        }
        try {
            byte[] publicByte={};
            byte[] privateByte = {};
            byte[] idByte = {};
            FileInputStream inputStream = new FileInputStream(publicName);
            inputStream.read(publicByte);
            inputStream.close();
            inputStream = new FileInputStream(privateName);
            inputStream.read(privateByte);
            inputStream.close();
            inputStream = new FileInputStream(id);
            inputStream.read(idByte);
            inputStream.close();
            return new KeyPairStore(new String(idByte), KeyGenerator.unserializeedPrivateKey(privateByte),KeyGenerator.unserializedPublicKey(publicByte));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getId() {
        return id;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
