package uk.ac.ncl.cs.group1.clientapi;

import uk.ac.ncl.cs.group1.clientapi.Receiver.CheckCallBack;

import java.util.List;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 15/03/14
 */
public interface DocReceive {
    List<UUID> checkExistCommunication();
    void asyCheckExistCommunication(CheckCallBack callBack,long intervalTime);

}
