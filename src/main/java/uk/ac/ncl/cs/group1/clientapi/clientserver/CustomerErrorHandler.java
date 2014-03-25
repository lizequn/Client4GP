package uk.ac.ncl.cs.group1.clientapi.clientserver;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Li Zequn
 * Date: 25/03/14
 */
public class CustomerErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        String str = IOUtils.toString(clientHttpResponse.getBody());
        throw new IllegalArgumentException(clientHttpResponse.getStatusCode()+" error:"+str);
    }


}
