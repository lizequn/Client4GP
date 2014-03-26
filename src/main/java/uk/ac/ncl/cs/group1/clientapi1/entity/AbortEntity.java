package uk.ac.ncl.cs.group1.clientapi1.entity;

/**
 * @Auther: Li Zequn
 * Date: 19/03/14
 */
public class AbortEntity {
    private boolean success;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
