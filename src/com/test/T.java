package com.test;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

/**
 * @author nidayu
 * @Description: 测试
 * @date 2015/6/17
 */
public class T {
    public static void main(String[] args) {
        String text = null;
//        text = getUrl("http://my.csdn.net/my/mycsdn");
//        System.out.println(text);
        String url = "http://www1.sxcredit.gov.cn/public/infocomquery.do;jsessionid=9q8yj1al11CgAzCgBQ?method=publicIndexQuery";
        String[][] params = { { "query.enterprisename", "网" },
                { "query.registationnumber", "" },
                { "query.organizationsymbol", "" } };
        text = postUrl(url, params);
        System.out.println(text);
    }

    public static String getUrl(String url) {
        String ret = null;
        // 1. 生成 HttpClinet对象并设置参数
        HttpClient httpClient = new HttpClient();
        // 设置 Http 连接超时为5秒
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(5000);

        // 2.生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        // 设置 get 请求超时为 5 秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());

        // 3. 执行 HTTP GET请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 4. 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + getMethod.getStatusLine());
            }
            // 5.处理 HTTP响应内容
            // HTTP响应头部信息，这里简单打印
            Header[] headers = getMethod.getResponseHeaders();
            for (Header h : headers) {
                System.out.println(h.getName() + " " + h.getValue());
            }
            // 读取 HTTP 响应内容，这里简单打印网页内容
            byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
            ret = new String(responseBody);
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

    public static String postUrl(String url, String[][] params) {
        String ret = null;
        // 1.生成 HttpClinet 对象并设置参数
        HttpClient httpClient = new HttpClient();
        // 设置Http连接超时为5秒
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(5000);

        // 2.生成 PostMethod 对象并设置参数
        PostMethod postMethod = new PostMethod(url);
        NameValuePair[] postParams = new NameValuePair[params.length];
        for (int i = 0; i < params.length; i++) {
            NameValuePair param = new NameValuePair(params[i][0], params[i][1]);
            postParams[i] = param;
        }
        postMethod.setRequestBody(postParams);
        // 设置post请求超时为 5 秒
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());

        // 3.执行 HTTP POST请求
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            // 4.判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + postMethod.getStatusLine());
            }
            // 5.处理 HTTP响应内容
            // HTTP响应头部信息，这里简单打印
            Header[] headers = postMethod.getResponseHeaders();
            for (Header h : headers) {
                System.out.println(h.getName() + " " + h.getValue());
            }
            // 读取 HTTP 响应内容，这里简单打印网页内容
            byte[] responseBody = postMethod.getResponseBodyAsString().getBytes("gb2312");// 读取为字节数组
            ret = new String(responseBody);
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
