package uk.ac.ncl.cs.group1.clientapi.Sender;

import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public class SessionInfo {
    private String sessionName;
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
