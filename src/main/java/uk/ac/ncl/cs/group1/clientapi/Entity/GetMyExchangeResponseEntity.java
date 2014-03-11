package uk.ac.ncl.cs.group1.clientapi.entity;

import java.util.List;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 25/02/14
 */
public class GetMyExchangeResponseEntity {
    private List<UUID> lists;

    public List<UUID> getLists() {
        return lists;
    }

    public void setLists(List<UUID> lists) {
        this.lists = lists;
    }
}
