package com.spider.email;

import com.spider.HttpClientFactory;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author nidayu
 * @Description: 163” œ‰
 * @date 2015/12/2
 */
public class Mail163 extends HttpClientFactory {

    private CloseableHttpClient httpClient;

    public Mail163(CloseableHttpClient httpClient){
        this.httpClient = httpClient;
    }


}
