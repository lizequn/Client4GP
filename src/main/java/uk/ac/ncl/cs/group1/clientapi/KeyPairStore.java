package uk.ac.ncl.cs.group1.clientapi;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
//todo
public class KeyPairStore {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public KeyPairStore(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    public void store2File(File publicName, File privateName){
        //todo
    }
//    public static KeyPairStore getFromFile(File publicName,File privateName){
//
//    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
