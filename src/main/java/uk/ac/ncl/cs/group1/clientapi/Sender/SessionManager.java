package uk.ac.ncl.cs.group1.clientapi.Sender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public class SessionManager {
    private static SessionManager ourInstance = new SessionManager();

    public static SessionManager getInstance() {
        return ourInstance;
    }

    private SessionManager() {
        senderMap = new HashMap<>();
        receiveMap = new HashMap<>();
    }
    private Map<UUID,SessionInfo> senderMap;
    private Map<UUID,SessionInfo> receiveMap;

    public void addNewSender(SessionInfo info){
       senderMap.put(info.getUuid(),info);

    }
    public void addNewReceiver(SessionInfo info){
        receiveMap.put(info.getUuid(),info);
    }
    public SessionInfo getSender(UUID uuid){
        return senderMap.get(uuid);
    }
    public SessionInfo getReceiver(UUID uuid){
        return receiveMap.get(uuid);
    }
}
