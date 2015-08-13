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
            //����cookie����ʧ
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.getParams().setParameter("http.protocol.single-cookie-header", true);
            return httpClient;
        }
        return httpClient;
    }
    /**
     * ����GET����
     * @param httpClient
     * @param url
     * @param headers
     * @return
     */
    public static String getUrl(HttpClient httpClient, String url, String[][] headers) {
        String ret = null;
        // 2.���� GetMethod �������ò���
        GetMethod getMethod = new GetMethod(url);
        // ���� get ����ʱΪ 5 ��
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // �����������Դ����õ���Ĭ�ϵ����Դ����������
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, true));

        //��������ͷ����
        if(headers!=null) {
            for (int i = 0; i < headers.length; i++) {
                Header h = new Header(headers[i][0], headers[i][1]);
                getMethod.setRequestHeader(h);
            }
        }
        // 3. ִ�� HTTP GET����
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 4. �жϷ��ʵ�״̬��
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
            getMethod.getRequestHeaders();

            // 5.���� HTTP��Ӧ����
            // HTTP��Ӧͷ����Ϣ������򵥴�ӡ
			Header[] cheaders = getMethod.getResponseHeaders();
			for (Header h : cheaders) {
//				System.out.println(h.getName() + " " + h.getValue());
//                if(h.getValue().startsWith("JUT")){
//                    SpiderManager.cookie = h.getValue();
//                }
			}
            // ��ȡ HTTP ��Ӧ���ݣ�����򵥴�ӡ��ҳ����
            ret = getMethod.getResponseBodyAsString();
//			ret = new String(getMethod.getResponseBodyAsString().getBytes("gb2312"));
            // ��ȡΪ InputStream������ҳ������������ʱ���Ƽ�ʹ��
            // InputStream response = getMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            // �����������쳣��������Э�鲻�Ի��߷��ص�����������
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // ���������쳣
            e.printStackTrace();
        } finally {
            // 6.�ͷ�����
            getMethod.releaseConnection();
        }
        return ret;
    }

    /**
     * ����POST����
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String postUrl(HttpClient httpClient, String url, String[][] params, String[][] headers) {
        String ret = null;
        // 2.���� PostMethod �������ò���
		PostMethod postMethod = new PostMethod(url);
        // ����post����ʱΪ 5 ��
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
        // �����������Դ����õ���Ĭ�ϵ����Դ�����������
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        //��������ͷ����
        if(headers!=null) {
            for (int i = 0; i < headers.length; i++) {
                Header h = new Header(headers[i][0], headers[i][1]);
                postMethod.setRequestHeader(h);
            }
        }
//        postMethod.setRequestHeader(new Header("Cookie", SpiderManager.cookie));
        //�����������
        if (params!=null) {
            NameValuePair[] postParams = new NameValuePair[params.length];
            for (int i = 0; i < params.length; i++) {
                NameValuePair param = new NameValuePair(params[i][0], params[i][1]);
                postParams[i] = param;
            }
            postMethod.setRequestBody(postParams);
        }

        // 3.ִ�� HTTP POST����
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            // 4.�жϷ��ʵ�״̬��
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + postMethod.getStatusLine());
            }

            // 5.���� HTTP��Ӧ����
            // HTTP��Ӧͷ����Ϣ������򵥴�ӡ
//			Header[] cheaders = postMethod.getResponseHeaders();
//			for (Header h : cheaders) {
//				System.out.println(h.getName() + " " + h.getValue());
//			}

            // ��ȡ HTTP ��Ӧ���ݣ�����򵥴�ӡ��ҳ����
            ret = postMethod.getResponseBodyAsString();// ��ȡΪ�ֽ�����
            // ��ȡΪ InputStream������ҳ������������ʱ���Ƽ�ʹ��
            // InputStream response = postMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            // �����������쳣��������Э�鲻�Ի��߷��ص�����������
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // ���������쳣
            e.printStackTrace();
        } finally {
            // 6.�ͷ�����
            postMethod.releaseConnection();
        }
        return ret;
    }
}
