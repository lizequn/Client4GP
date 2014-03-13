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
import uk.ac.ncl.cs.group1.clientapi.test.Receiver;
import uk.ac.ncl.cs.group1.clientapi.test.Sender;
import uk.ac.ncl.cs.group1.clientapi.uitl.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.uitl.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.uitl.KeyGenerator;
import uk.ac.ncl.cs.group1.clientapi.uitl.SignUtil;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ZequnLi
 *         Date: 14-2-23
 */
public class Test {
    private final static Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        String senderName = "test13";
        String receiverName = "test14";
        Thread thread1 = new Thread(new Sender(senderName,receiverName));
        Thread thread2 = new Thread(new Receiver(receiverName));
        // wait finish
        Thread.sleep(2000);
        thread1.start();
        thread2.start();


    }
}
