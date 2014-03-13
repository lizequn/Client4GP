package uk.ac.ncl.cs.group1.clientapi.entity;

/**
 * @Auther: Li Zequn
 * Date: 06/03/14
 */
public class RegisterResponseEntity {
    private String id;
    private byte[] publicKey;
    private byte[] privateKey;

    public RegisterResponseEntity(){

    }
    public RegisterResponseEntity(String id,byte[] publicKey, byte[] privateKey){
        this.id = id;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
