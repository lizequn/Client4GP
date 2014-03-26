package uk.ac.ncl.cs.group1.clientapi1;

import uk.ac.ncl.cs.group1.clientapi1.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi1.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi1.entity.PublicKeyEntity;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public interface DocSender {
    UUID sendDoc(File file,String address,boolean email) throws IOException;
    void receiveReceipt(long intervalTime,int times,UUID uuid,ReceiptCallBack callBack);
    PublicKeyEntity getPublicKey(String id);
    boolean checkSignature(File file,File receipt,PublicKeyEntity entity);
    boolean checkSignature(File file,byte[] receipt,PublicKeyEntity entity);
    boolean resolve(UUID uuid,ReceiptCallBack callBack);
    boolean abort(UUID uuid);
}
