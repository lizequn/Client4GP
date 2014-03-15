package uk.ac.ncl.cs.group1.clientapi;

import uk.ac.ncl.cs.group1.clientapi.callback.CheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.FileStore;
import uk.ac.ncl.cs.group1.clientapi.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.core.KeyPairStore;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 15/03/14
 */
public interface DocReceive {
    List<UUID> checkExistCommunication(KeyPairStore keyPairStore);
    void asyCheckExistCommunication(KeyPairStore keyPairStore,CheckCallBack callBack,long intervalTime);
    void getFileAndReceipt(KeyPairStore keyPairStore,FileStore fileStore,ReceiptCallBack receiptCallBack);
    boolean verifyFileWithReceipt(File file,byte[] receipt);
    boolean verifyFileWithReceipt(File file,File receipt);
}
