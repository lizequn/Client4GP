package uk.ac.ncl.cs.group1.clientapi.entity;

/**
 * @Auther: Li Zequn
 * Date: 18/03/14
 */
public class PublicKeyEntity {
    private String id;
    private String publicKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
