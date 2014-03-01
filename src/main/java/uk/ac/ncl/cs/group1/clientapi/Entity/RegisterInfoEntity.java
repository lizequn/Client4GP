package uk.ac.ncl.cs.group1.clientapi.Entity;

/**
 * @Auther: Li Zequn
 * Date: 21/02/14
 */

public class RegisterInfoEntity {
    private String name;
    private byte[] publicKey;
    private byte[] privateKey;
    public RegisterInfoEntity(){

    }

    public RegisterInfoEntity(String name, byte[] privateKey, byte[] publicKey) {
        this.name = name;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
