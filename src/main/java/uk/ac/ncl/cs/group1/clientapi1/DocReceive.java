package uk.ac.ncl.cs.group1.clientapi1;

import uk.ac.ncl.cs.group1.clientapi1.callback.CheckCallBack;
import uk.ac.ncl.cs.group1.clientapi1.callback.FileStore;
import uk.ac.ncl.cs.group1.clientapi1.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi1.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi1.entity.PublicKeyEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 15/03/14
 */
public interface DocReceive {
    List<UUID> checkExistCommunication();
    List<UUID> asyCheckExistCommunication(CheckCallBack callBack,long intervalTime,int times);
    UUID getFileAndReceipt(UUID uuid,FileStore fileStore,ReceiptCallBack receiptCallBack);
    PublicKeyEntity getPublicKey(String id);
    boolean verifyFileWithReceipt(File file,byte[] receipt,PublicKeyEntity entity) throws IOException;
    boolean verifyFileWithReceipt(File file,File receipt,PublicKeyEntity entity) throws IOException;
    boolean resolve(UUID uuid,FileStore fileStore);
}
