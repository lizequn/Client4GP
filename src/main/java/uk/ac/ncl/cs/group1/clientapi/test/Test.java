package uk.ac.ncl.cs.group1.clientapi.test;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.DocReceive;
import uk.ac.ncl.cs.group1.clientapi.DocSender;
import uk.ac.ncl.cs.group1.clientapi.Register;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultCheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.core.DocReceiveImpl;
import uk.ac.ncl.cs.group1.clientapi.core.DocSenderImpl;
import uk.ac.ncl.cs.group1.clientapi.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi.core.RegisterImpl;

import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.UUID;

/**
 * @author ZequnLi
 *         Date: 14-2-23
 */
public class Test {
    private final static Logger log = Logger.getLogger(Test.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        DefaultHttpClient httpClient = (DefaultHttpClient) requestFactory.getHttpClient();
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] certificate, String authType) {
                return true;
            }
        };
        SSLSocketFactory sf = new SSLSocketFactory(acceptingTrustStrategy,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 8443, sf));




        String url = "https://localhost:8443";
        RestTemplate template = new RestTemplate(requestFactory);
        ResponseEntity<String> responseEntity = template.getForEntity(url,String.class);
        log.info(responseEntity.getBody());


//        String senderName = "test31";
//        String receiverName = "test32";
//        Thread thread1 = new Thread(new Sender(senderName,receiverName));
//        Thread thread2 = new Thread(new Receiver(receiverName));
//        // wait finish
//        Thread.sleep(2000);
//        thread1.start();
//        thread2.start();
//        final String senderName = "sendera1";
//        final String receiverName = "receiveb1";
//        final File file = new File("testfile.txt");
//        Register register = new RegisterImpl();
//        KeyPairStore keyPairStore1;
//        try {
//            keyPairStore1 = KeyPairStore.getFromFile(new File(senderName),new File(senderName+".puk"),new File(senderName+".pik"));
//
//        }catch (IllegalArgumentException e){
//            keyPairStore1 = register.register(senderName);
//            keyPairStore1.store2File(new File(senderName),new File(senderName+".puk"),new File(senderName+".pik"));
//        }
//        KeyPairStore keyPairStore2;
//        try {
//            keyPairStore2 = KeyPairStore.getFromFile(new File(receiverName),new File(receiverName+".puk"),new File(receiverName+".pik"));
//
//        }catch (IllegalArgumentException e){
//            keyPairStore2 = register.register(receiverName);
//            keyPairStore2.store2File(new File(receiverName),new File(receiverName+".puk"),new File(receiverName+".pik"));
//        }
//
//        final DocSender docSender = new DocSenderImpl(keyPairStore1);
//        final DocReceive docReceive = new DocReceiveImpl(keyPairStore2);
//        Runnable runnable1 = new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    UUID uuid = docSender.sendDoc(file,receiverName) ;
//                    docSender.receiveReceipt(1000,10,uuid,new DefaultReceiptCallBack(new File("D:\\")));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } ;
//        Runnable runnable2 = new Runnable() {
//            @Override
//            public void run() {
//                docReceive.asyCheckExistCommunication(new DefaultCheckCallBack(docReceive),1000,1000);
//            }
//        };
//       new Thread(runnable1).start();
//       new Thread(runnable2).start();
//
    }
}
