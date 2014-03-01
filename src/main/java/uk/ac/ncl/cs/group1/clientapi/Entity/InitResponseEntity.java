package uk.ac.ncl.cs.group1.clientapi.Entity;


/**
 * @author ZequnLi
 *         Date: 14-2-22
 */
public class InitResponseEntity {
    private String url;

    public InitResponseEntity(){

    }
    public InitResponseEntity(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
