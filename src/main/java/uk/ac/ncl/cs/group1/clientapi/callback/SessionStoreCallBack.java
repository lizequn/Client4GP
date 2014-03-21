package uk.ac.ncl.cs.group1.clientapi.callback;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 21/03/14
 */
public interface SessionStoreCallBack {
    void storeUUID(File id,UUID uuid,boolean finish);
    List<UUID> getUnfinishedUUID(File id);
}
