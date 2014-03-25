package uk.ac.ncl.cs.group1.clientapi.test;

import org.apache.log4j.Logger;
import uk.ac.ncl.cs.group1.clientapi.DocReceive;
import uk.ac.ncl.cs.group1.clientapi.Register;
import uk.ac.ncl.cs.group1.clientapi.callback.CheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultCheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultFileStore;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.core.DocReceiveImpl;
import uk.ac.ncl.cs.group1.clientapi.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi.core.RegisterImpl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 19/03/14
 */
public class ReceiverTest {
    private final static Logger log = Logger.getLogger(ReceiverTest.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
//        String senderName = "test31";
//        String receiverName = "test32";
//        Thread thread1 = new Thread(new Sender(senderName,receiverName));
//        Thread thread2 = new Thread(new Receiver(receiverName));
//        // wait finish
//        Thread.sleep(2000);
//        thread1.start();
//        thread2.start();
        final String senderName = "lzq910123@gmail.com";
        final String receiverName = "anirudhagarwal0910@gmail.com";

        Register register = new RegisterImpl();

        KeyPairStore keyPairStore2;
        try {
            keyPairStore2 = KeyPairStore.getFromFile(new File(receiverName),new File(receiverName+".puk"),new File(receiverName+".pik"));

        }catch (IllegalArgumentException e){
            keyPairStore2 = register.register(receiverName);
            keyPairStore2.store2File(new File(receiverName),new File(receiverName+".puk"),new File(receiverName+".pik"));
        }

        final DocReceive docReceive = new DocReceiveImpl(keyPairStore2);
        CheckCallBack checkCallBack = new CheckCallBack() {
            @Override
            public void getUUID(List<UUID> lists) {
                for(UUID uuid:lists){
                    docReceive.getFileAndReceipt(uuid,new DefaultFileStore(new File("D:\\test\\receiver")),new DefaultReceiptCallBack(new File("D:\\test\\receiver")));
                }
            }
        } ;
        docReceive.asyCheckExistCommunication(checkCallBack,1000,1000);
    }
}
