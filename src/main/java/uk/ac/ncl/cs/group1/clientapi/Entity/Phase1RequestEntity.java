package uk.ac.ncl.cs.group1.clientapi.entity;

import java.util.Arrays;

/**
 * @Auther: Li Zequn
 * Date: 21/02/14
 */
public class Phase1RequestEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phase1RequestEntity entity = (Phase1RequestEntity) o;

        if (!from.equals(entity.from)) return false;
        if (!Arrays.equals(signedHash, entity.signedHash)) return false;
        if (!to.equals(entity.to)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + Arrays.hashCode(signedHash);
        return result;
    }
}
