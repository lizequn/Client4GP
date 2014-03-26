package uk.ac.ncl.cs.group1.clientapi1.clientserver;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.util.Collections;

/**
 * @Auther: Li Zequn
 * Date: 13/03/14
 */
public class MyRequestHeader implements ClientHttpRequestInterceptor {
    private String name;
    private String authCode;
    public MyRequestHeader(String name,String authCode){
        this.name=name;
        this.authCode= authCode;
    }
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpRequestWrapper wrapper = new HttpRequestWrapper(httpRequest);
        HttpHeaders headers = wrapper.getHeaders();
        headers.set("name", this.name);
        headers.set("auth_token", this.authCode);

        return clientHttpRequestExecution.execute(wrapper, bytes);
    }
}
