package uk.ac.ncl.cs.group1.clientapi.clientserver;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: Li Zequn
 * Date: 13/03/14
 */
public class MyRestTemplate {
    public static RestTemplate getTemplate(String name,String authCode){
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
        list.add(new MyRequestHeader(name, authCode));
        restTemplate.setInterceptors(list);
        restTemplate.setErrorHandler(new CustomerErrorHandler());
        return restTemplate;
    }
}
