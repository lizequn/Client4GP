package uk.ac.ncl.cs.group1.clientapi.Entity;

import java.io.Serializable;

/**
 * @Auther: Li Zequn
 * Date: 21/02/14
 */

public class RegisterEntity implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
