package com.spider;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

/**
 * @author nidayu
 * @Description:
 * @date 2015/7/23
 */
public class SpiderManager {
    private static HttpClient httpClient;
    private static String cookie;

    public static HttpClient getInstance(){
        if (httpClient==null){
            httpClient = new HttpClient();
            HttpClientParams h = new HttpClientParams();
            h.setHttpElementCharset("UTF-8");
            h.setContentCharset("UTF-8");
            httpClient.setParams(h);
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
            //保持cookie不丢失
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.getParams().setParameter("http.protocol.single-cookie-header", true);
            return httpClient;
        }
        return httpClient;
    }
    /**
     * 发送GET请求
     * @param httpClient
     * @param url
     * @param headers
     * @return
     */
    public static String getUrl(HttpClient httpClient, String url, String[][] headers) {
        String ret = null;
        // 2.生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        // 设置 get 请求超时为 5 秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理，用的是默认的重试处理：请求五次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, true));

        //设置请求头参数
        if(headers!=null) {
            for (int i = 0; i < headers.length; i++) {
                Header h = new Header(headers[i][0], headers[i][1]);
                getMethod.setRequestHeader(h);
            }
        }
        // 3. 执行 HTTP GET请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 4. 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
            getMethod.getRequestHeaders();

            // 5.处理 HTTP响应内容
            // HTTP响应头部信息，这里简单打印
			Header[] cheaders = getMethod.getResponseHeaders();
			for (Header h : cheaders) {
//				System.out.println(h.getName() + " " + h.getValue());
//                if(h.getValue().startsWith("JUT")){
//                    SpiderManager.cookie = h.getValue();
//                }
			}
            // 读取 HTTP 响应内容，这里简单打印网页内容
            ret = getMethod.getResponseBodyAsString();
//			ret = new String(getMethod.getResponseBodyAsString().getBytes("gb2312"));
            // 读取为 InputStream，在网页内容数据量大时候推荐使用
            // InputStream response = getMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            e.printStackTrace();
        } finally {
            // 6.释放连接
            getMethod.releaseConnection();
        }
        return ret;
    }

    /**
     * 发送POST请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String postUrl(HttpClient httpClient, String url, String[][] params, String[][] headers) {
        String ret = null;
        // 2.生成 PostMethod 对象并设置参数
		PostMethod postMethod = new PostMethod(url);
        // 设置post请求超时为 5 秒
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        //设置请求头参数
        if(headers!=null) {
            for (int i = 0; i < headers.length; i++) {
                Header h = new Header(headers[i][0], headers[i][1]);
                postMethod.setRequestHeader(h);
            }
        }
//        postMethod.setRequestHeader(new Header("Cookie", SpiderManager.cookie));
        //设置请求参数
        if (params!=null) {
            NameValuePair[] postParams = new NameValuePair[params.length];
            for (int i = 0; i < params.length; i++) {
                NameValuePair param = new NameValuePair(params[i][0], params[i][1]);
                postParams[i] = param;
            }
            postMethod.setRequestBody(postParams);
        }

        // 3.执行 HTTP POST请求
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            // 4.判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + postMethod.getStatusLine());
            }

            // 5.处理 HTTP响应内容
            // HTTP响应头部信息，这里简单打印
//			Header[] cheaders = postMethod.getResponseHeaders();
//			for (Header h : cheaders) {
//				System.out.println(h.getName() + " " + h.getValue());
//			}

            // 读取 HTTP 响应内容，这里简单打印网页内容
            ret = postMethod.getResponseBodyAsString();// 读取为字节数组
            // 读取为 InputStream，在网页内容数据量大时候推荐使用
            // InputStream response = postMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            e.printStackTrace();
        } finally {
            // 6.释放连接
            postMethod.releaseConnection();
        }
        return ret;
    }
}
