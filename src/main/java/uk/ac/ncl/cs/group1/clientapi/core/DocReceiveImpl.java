package uk.ac.ncl.cs.group1.clientapi.core;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import uk.ac.ncl.cs.group1.clientapi.DocReceive;
import uk.ac.ncl.cs.group1.clientapi.Resource;
import uk.ac.ncl.cs.group1.clientapi.TTPURL;
import uk.ac.ncl.cs.group1.clientapi.callback.CheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.FileStore;
import uk.ac.ncl.cs.group1.clientapi.callback.ReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.clientserver.MyRestTemplate;
import uk.ac.ncl.cs.group1.clientapi.entity.GetMyExchangeResponseEntity;
import uk.ac.ncl.cs.group1.clientapi.entity.Phase1RequestEntity;
import uk.ac.ncl.cs.group1.clientapi.entity.Phase3RequestEntity;
import uk.ac.ncl.cs.group1.clientapi.util.Base64Coder;
import uk.ac.ncl.cs.group1.clientapi.util.HashUtil;
import uk.ac.ncl.cs.group1.clientapi.util.SignUtil;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * @author ZequnLi
 *         Date: 14-3-16
 */
public class DocReceiveImpl extends Resource implements DocReceive {
    private final KeyPairStore keyPairStore;
    private final RestTemplate restTemplate;
    public DocReceiveImpl(KeyPairStore keyPairStore){
        this.keyPairStore = keyPairStore;
        restTemplate = MyRestTemplate.getTemplate(keyPairStore.getId(), Base64Coder.encode(SignUtil.sign(keyPairStore.getPrivateKey(), keyPairStore.getId().getBytes())));
    }

    @Override
    public List<UUID> checkExistCommunication() {
        log.info("get my exchange "+keyPairStore.getId());
        String myUrl = TTPURL.getMyExchangeUrl+"/"+keyPairStore.getId();
        ResponseEntity<GetMyExchangeResponseEntity> response =  restTemplate.postForEntity(myUrl,null, GetMyExchangeResponseEntity.class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException();
        }
        GetMyExchangeResponseEntity exchangeResponseEntity = response.getBody();
        List<UUID> list = exchangeResponseEntity.getLists();
//        restTemplate.postForEntity(myUrl,req, GetMyExchangeResponseEntity.class);
        if(list.size()<=0){
            return null;
        }
        log.info("number "+list.size());
        return list;
    }

    @Override
    public void asyCheckExistCommunication(final CheckCallBack callBack,final long intervalTime) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i =100;
                while(i>0)  {
                    log.info("get my exchange "+keyPairStore.getId());
                    String myUrl = TTPURL.getMyExchangeUrl+"/"+keyPairStore.getId();
                    ResponseEntity<GetMyExchangeResponseEntity> response =  restTemplate.postForEntity(myUrl,null, GetMyExchangeResponseEntity.class);
                    if(response.getStatusCode() != HttpStatus.OK){
                        throw new IllegalStateException();
                    }
                    GetMyExchangeResponseEntity exchangeResponseEntity = response.getBody();
                    List<UUID> list = exchangeResponseEntity.getLists();
                    if(list.size()<=0){
                        try {
                            Thread.sleep(intervalTime);
                        } catch (InterruptedException ignored) {
                        }
                        i++;
                        continue;
                    }
                    log.info("number "+list.size());
                    callBack.getUUID(list);
                    break;
                }
            }
        };
        new Thread(runnable).start();

    }

    @Override
    public void getFileAndReceipt(UUID uuid,FileStore fileStore, ReceiptCallBack receiptCallBack) {
        log.info("begin get file and receipt ID:"+uuid);
        String myUrl1 = TTPURL.phase2Url+"/"+keyPairStore.getId()+"/"+uuid;
        ResponseEntity<Phase1RequestEntity> responseEntity = restTemplate.postForEntity(myUrl1,null,Phase1RequestEntity.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK){
            throw new IllegalStateException();
        }
        Phase1RequestEntity requestEntity = responseEntity.getBody();


        byte[] bytes = requestEntity.getSignedHash();
        String hashedBytes = HashUtil.calHash(bytes);
        byte[] sigB = SignUtil.sign(keyPairStore.getPrivateKey(),hashedBytes.getBytes());
        String myUrl2 = TTPURL.phase3Url+"/"+keyPairStore.getId()+"/"+uuid;
        Phase3RequestEntity entity = new Phase3RequestEntity();
        entity.setReceiptHash(sigB);
//        RequestCallback requestCallback = new RequestCallback() {
//
//            @Override
//            public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
//
//                System.out.println(clientHttpRequest.getHeaders());
//                //String fileName = clientHttpRequest.getHeaders().get("FileName").get(0);
//                InputStream fis = new FileInputStream(new File("result1"));
//                IOUtils.copy(fis, clientHttpRequest.getBody());
//            }
//        };
//        final HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(String.class,restTemplate.getMessageConverters());
        // restTemplate.execute(myUrl2, HttpMethod.POST,requestCallback,responseExtractor);
        byte[] doc =  restTemplate.postForObject(myUrl2,entity,byte[].class);
        receiptCallBack.getReceipt(bytes);
        fileStore.storeFile(doc);
        log.info("finish ID "+uuid);
    }

    @Override
    public boolean verifyFileWithReceipt(File file, byte[] receipt) {
        //todo
       return true;
    }

    @Override
    public boolean verifyFileWithReceipt(File file, File receipt) {
        //todo
        return true;
    }
}
