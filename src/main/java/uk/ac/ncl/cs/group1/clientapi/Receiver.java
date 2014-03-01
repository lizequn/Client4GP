package uk.ac.ncl.cs.group1.clientapi;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther: Li Zequn
 * Date: 01/03/14
 */
public class Receiver {
    private static Logger log = Logger.getLogger(Receiver.class);
    private final String name;
    private RestTemplate restTemplate;
    private final static String url = "http://localhost:8080";
    private final static String registerUrl = url+"/register";
    private final static String initRequestUrl = url+"/initRequest";
    public Receiver(String name){
        this.name = name;
    }
}
