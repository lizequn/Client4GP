package uk.ac.ncl.cs.group1.clientapi.test;

import org.apache.log4j.Logger;
import uk.ac.ncl.cs.group1.clientapi.DocReceive;
import uk.ac.ncl.cs.group1.clientapi.Register;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultCheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.core.DocReceiveImpl;
import uk.ac.ncl.cs.group1.clientapi.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi.core.RegisterImpl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
        final String senderName = "sendera1";
        final String receiverName = "receiveb1";

        Register register = new RegisterImpl();

        KeyPairStore keyPairStore2;
        try {
            keyPairStore2 = KeyPairStore.getFromFile(new File(receiverName),new File(receiverName+".puk"),new File(receiverName+".pik"));

        }catch (IllegalArgumentException e){
            keyPairStore2 = register.register(receiverName);
            keyPairStore2.store2File(new File(receiverName),new File(receiverName+".puk"),new File(receiverName+".pik"));
        }

        final DocReceive docReceive = new DocReceiveImpl(keyPairStore2);

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                docReceive.asyCheckExistCommunication(new DefaultCheckCallBack(docReceive),1000);
            }
        };
        new Thread(runnable2).start();

    }
}
