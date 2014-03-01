package uk.ac.ncl.cs.group1.clientapi.Entity;

/**
 * @author ZequnLi
 *         Date: 14-2-22
 */
public class UploadSuccessEntity {
    private String info;

    public UploadSuccessEntity() {
    }

    public UploadSuccessEntity(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
