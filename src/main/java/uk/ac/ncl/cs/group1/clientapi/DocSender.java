package uk.ac.ncl.cs.group1.clientapi;

import uk.ac.ncl.cs.group1.clientapi.Sender.ReceiptCallBack;

import java.io.File;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public interface DocSender {
    UUID sendDoc(KeyPairStore keyPairStore,File file);
    void receiveReceipt(long intervalTime,UUID uuid,ReceiptCallBack callBack);

}
