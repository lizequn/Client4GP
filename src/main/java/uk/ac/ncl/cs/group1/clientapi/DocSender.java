package uk.ac.ncl.cs.group1.clientapi;

import uk.ac.ncl.cs.group1.clientapi.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.core.KeyPairStore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public interface DocSender {
    UUID sendDoc(KeyPairStore keyPairStore,File file,String address) throws IOException;
    void receiveReceipt(long intervalTime,UUID uuid,ReceiptCallBack callBack);

}
