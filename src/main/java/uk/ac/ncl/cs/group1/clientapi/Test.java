package uk.ac.ncl.cs.group1.clientapi;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultCheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.core.DocReceiveImpl;
import uk.ac.ncl.cs.group1.clientapi.core.DocSenderImpl;
import uk.ac.ncl.cs.group1.clientapi.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi.core.RegisterImpl;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author ZequnLi
 *         Date: 14-2-23
 */
public class Test {
    private final static Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
//        String senderName = "test31";
//        String receiverName = "test32";
//        Thread thread1 = new Thread(new Sender(senderName,receiverName));
//        Thread thread2 = new Thread(new Receiver(receiverName));
//        // wait finish
//        Thread.sleep(2000);
//        thread1.start();
//        thread2.start();
        final String senderName = "aaaa1";
        final String receiverName = "aaaa2";
        final File file = new File("testfile");
        Register register = new RegisterImpl();
        KeyPairStore keyPairStore1 = register.register(senderName);
        KeyPairStore keyPairStore2 = register.register(receiverName);
        final DocSender docSender = new DocSenderImpl(keyPairStore1);
        final DocReceive docReceive = new DocReceiveImpl(keyPairStore2);
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {

                try {
                    UUID uuid = docSender.sendDoc(file,receiverName) ;
                    docSender.receiveReceipt(1000,10,uuid,new DefaultReceiptCallBack(new File("D:\\")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } ;
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                docReceive.asyCheckExistCommunication(new DefaultCheckCallBack(docReceive),1000);
            }
        };
        new Thread(runnable1).start();
        new Thread(runnable2).start();

    }
}
