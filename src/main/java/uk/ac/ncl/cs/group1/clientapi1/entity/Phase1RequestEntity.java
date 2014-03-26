package uk.ac.ncl.cs.group1.clientapi1.entity;

/**
 * @Auther: Li Zequn
 * Date: 21/02/14
 */
public class Phase1RequestEntity {
    private String from;
    private String to;
    private String signedHash;

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

    public String getSignedHash() {
        return signedHash;
    }

    public void setSignedHash(String signedHash) {
        this.signedHash = signedHash;
    }


}
