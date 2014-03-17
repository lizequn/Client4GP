//package uk.ac.ncl.cs.group1.clientapi.test;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.log4j.Logger;
//import org.springframework.http.*;
//import org.springframework.http.client.ClientHttpRequest;
//import org.springframework.web.client.HttpMessageConverterExtractor;
//import org.springframework.web.client.RequestCallback;
//import org.springframework.web.client.RestTemplate;
//import org.apache.commons.io.IOUtils;
//import uk.ac.ncl.cs.group1.clientapi.clientserver.MyRestTemplate;
//import uk.ac.ncl.cs.group1.clientapi.entity.GetMyExchangeResponseEntity;
//import uk.ac.ncl.cs.group1.clientapi.entity.Phase1RequestEntity;
//import uk.ac.ncl.cs.group1.clientapi.entity.Phase3RequestEntity;
//import uk.ac.ncl.cs.group1.clientapi.entity.RegisterResponseEntity;
//import uk.ac.ncl.cs.group1.clientapi.util.Base64Coder;
//import uk.ac.ncl.cs.group1.clientapi.util.HashUtil;
//import uk.ac.ncl.cs.group1.clientapi.util.KeyGenerator;
//import uk.ac.ncl.cs.group1.clientapi.util.SignUtil;
//
//import java.io.*;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.util.List;
//import java.util.UUID;
//
///**
// * @author ZequnLi
// *         Date: 14-3-2
// */
//public class Receiver implements Runnable {
//    private static Logger log = Logger.getLogger(Receiver.class);
//    private final String name;
//    private RestTemplate restTemplate;
//    private final static String url = "http://localhost:8080";
//    private final static String registerUrl = url+"/register";
//    private final static String initRequestUrl = url+"/phase1";
//    private final static String getMyExchangeUrl = url+"/getmyexchange" ;
//    private final static String phase2Url = url+"/phase2";
//    private final static String phase3Url = url+"/phase3";
//    private PrivateKey privateKey;
//    private PublicKey publicKey;
//    private HttpHeaders headers;
//    public Receiver(String name){
//        this.name = name;
//        restTemplate = new RestTemplate();
//        register();
//    }
//
//    public void register(){
//        log.info("register");
//        //register
//        //register
//        String newUrl = registerUrl+"/"+this.name;
//        ResponseEntity<RegisterResponseEntity> infoEntity = restTemplate.postForEntity(newUrl,null, RegisterResponseEntity.class);
//        if (infoEntity.getStatusCode()!= HttpStatus.OK){
//            throw new IllegalArgumentException("1");
//        }
//        log.info("1-->register end");
//        //get private key and public key
//        RegisterResponseEntity entity1 = infoEntity.getBody();
////        log.info(entity1.getPrivateKey().length);
//        publicKey = KeyGenerator.unserializedPublicKey(Base64Coder.decode(entity1.getPublicKey()));
//        privateKey = KeyGenerator.unserializeedPrivateKey(Base64Coder.decode(entity1.getPrivateKey()));
//        log.info("generate Header");
//        headers = new HttpHeaders();
//        headers.add("name",name);
//        headers.add("auth_token", Base64Coder.encode(SignUtil.sign(privateKey, name.getBytes())));
//        log.info(headers.get("auth_token"));
//    }
//
//    public boolean begin() throws IOException {
//        restTemplate = MyRestTemplate.getTemplate(this.name,Base64Coder.encode(SignUtil.sign(privateKey, name.getBytes())));
//        log.info("begin");
//        log.info("get my exchange");
//        String myUrl = getMyExchangeUrl+"/"+name;
//        HttpEntity req = new HttpEntity(headers);
//        ResponseEntity<GetMyExchangeResponseEntity> response =  restTemplate.postForEntity(myUrl,req, GetMyExchangeResponseEntity.class);
//        if(response.getStatusCode() != HttpStatus.OK){
//            throw new IllegalStateException();
//        }
//        GetMyExchangeResponseEntity exchangeResponseEntity = response.getBody();
//        List<UUID> list = exchangeResponseEntity.getLists();
////        restTemplate.postForEntity(myUrl,req, GetMyExchangeResponseEntity.class);
//        log.info("get my exchange end");
//        if(list.size()<=0){
//            return false;
//        }
//
//        log.info("begin phase2");
//        UUID uuid = list.get(0);
//        log.info("ID:"+uuid);
//        String myUrl1 = phase2Url+"/"+name+"/"+uuid;
//        ResponseEntity<Phase1RequestEntity> responseEntity = restTemplate.postForEntity(myUrl1,req,Phase1RequestEntity.class);
//        if(responseEntity.getStatusCode() != HttpStatus.OK){
//            throw new IllegalStateException();
//        }
//        Phase1RequestEntity requestEntity = responseEntity.getBody();
//        log.info("finish phase2");
//        log.info("begin phase3");
//
//        byte[] bytes = requestEntity.getSignedHash();
//        String hashedBytes = HashUtil.calHash(bytes);
//        log.info("the length of bytes: " + bytes.length);
//        byte[] sigB = SignUtil.sign(privateKey,hashedBytes.getBytes());
//        String myUrl2 = phase3Url+"/"+name+"/"+uuid;
//        Phase3RequestEntity entity = new Phase3RequestEntity();
//        entity.setReceiptHash(sigB);
//        RequestCallback requestCallback = new RequestCallback() {
//
//            @Override
//            public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
//
//                System.out.println(clientHttpRequest.getHeaders());
//                //String fileName = clientHttpRequest.getHeaders().get("FileName").get(0);
//                InputStream fis = new FileInputStream(new File("result1"));
//                IOUtils.copy(fis,clientHttpRequest.getBody());
//            }
//        };
//        final HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(String.class,restTemplate.getMessageConverters());
//       // restTemplate.execute(myUrl2, HttpMethod.POST,requestCallback,responseExtractor);
//        byte[] doc =  restTemplate.postForObject(myUrl2,entity,byte[].class);
//        OutputStream fis = new FileOutputStream(new File("result1"));
//        fis.write(doc);
//        fis.close();
//        log.info("end phase3");
//        log.info("finish");
//        return true;
//    }
//
//    @Override
//    public void run() {
//        int i =0;
//        try {
//            while (!begin())  {
//                i++;
//                log.info("counter:"+i);
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException ignored) {
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//    }
//}
