package uk.ac.ncl.cs.group1.clientapi.Entity;

/**
 * @Auther: Li Zequn
 * Date: 21/02/14
 */
public class InitRequestEntity {
    private String from;
    private String to;
    private byte[] signedHash;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public byte[] getSignedHash() {
        return signedHash;
    }

    public void setSignedHash(byte[] signedHash) {
        this.signedHash = signedHash;
    }
}
