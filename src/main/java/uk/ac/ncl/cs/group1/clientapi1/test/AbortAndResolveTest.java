package uk.ac.ncl.cs.group1.clientapi1.test;

import org.apache.log4j.Logger;
import uk.ac.ncl.cs.group1.clientapi1.DocSender;
import uk.ac.ncl.cs.group1.clientapi1.Register;
import uk.ac.ncl.cs.group1.clientapi1.callback.defaultimpl.DefaultReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi1.core.DocSenderImpl;
import uk.ac.ncl.cs.group1.clientapi1.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi1.core.RegisterImpl;
import uk.ac.ncl.cs.group1.clientapi.test.*;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 26/03/14
 */
public class AbortAndResolveTest {
    private final static Logger log = Logger.getLogger(SenderTest.class);

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
        final String receiverName = "lzq_179@163.com";
        final File file = new File("testfile.txt");
        Register register = new RegisterImpl();
        KeyPairStore keyPairStore1;
        try {
            keyPairStore1 = KeyPairStore.getFromFile(new File(senderName),new File(senderName+".puk"),new File(senderName+".pik"));

        }catch (IllegalArgumentException e){
            keyPairStore1 = register.register(senderName);
            keyPairStore1.store2File(new File(senderName),new File(senderName+".puk"),new File(senderName+".pik"));
        }

        final DocSender docSender = new DocSenderImpl(keyPairStore1);

        UUID uuid = docSender.sendDoc(file,receiverName,true) ;
        log.info(uuid.toString());
        //docSender.receiveReceipt(1000,10,uuid,new DefaultReceiptCallBack(new File("D:\\test\\sender")));
         docSender.abort(uuid);

    }
}
