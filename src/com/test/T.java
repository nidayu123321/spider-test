package com.test;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

/**
 * @author nidayu
 * @Description: ����
 * @date 2015/6/17
 */
public class T {
    public static void main(String[] args) {
        String text = null;
//        text = getUrl("http://my.csdn.net/my/mycsdn");
//        System.out.println(text);
        String url = "http://www1.sxcredit.gov.cn/public/infocomquery.do;jsessionid=9q8yj1al11CgAzCgBQ?method=publicIndexQuery";
        String[][] params = { { "query.enterprisename", "��" },
                { "query.registationnumber", "" },
                { "query.organizationsymbol", "" } };
        text = postUrl(url, params);
        System.out.println(text);
    }

    public static String getUrl(String url) {
        String ret = null;
        // 1. ���� HttpClinet�������ò���
        HttpClient httpClient = new HttpClient();
        // ���� Http ���ӳ�ʱΪ5��
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(5000);

        // 2.���� GetMethod �������ò���
        GetMethod getMethod = new GetMethod(url);
        // ���� get ����ʱΪ 5 ��
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // �����������Դ����õ���Ĭ�ϵ����Դ�����������
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());

        // 3. ִ�� HTTP GET����
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 4. �жϷ��ʵ�״̬��
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + getMethod.getStatusLine());
            }
            // 5.���� HTTP��Ӧ����
            // HTTP��Ӧͷ����Ϣ������򵥴�ӡ
            Header[] headers = getMethod.getResponseHeaders();
            for (Header h : headers) {
                System.out.println(h.getName() + " " + h.getValue());
            }
            // ��ȡ HTTP ��Ӧ���ݣ�����򵥴�ӡ��ҳ����
            byte[] responseBody = getMethod.getResponseBody();// ��ȡΪ�ֽ�����
            ret = new String(responseBody);
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

    public static String postUrl(String url, String[][] params) {
        String ret = null;
        // 1.���� HttpClinet �������ò���
        HttpClient httpClient = new HttpClient();
        // ����Http���ӳ�ʱΪ5��
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(5000);

        // 2.���� PostMethod �������ò���
        PostMethod postMethod = new PostMethod(url);
        NameValuePair[] postParams = new NameValuePair[params.length];
        for (int i = 0; i < params.length; i++) {
            NameValuePair param = new NameValuePair(params[i][0], params[i][1]);
            postParams[i] = param;
        }
        postMethod.setRequestBody(postParams);
        // ����post����ʱΪ 5 ��
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
        // �����������Դ����õ���Ĭ�ϵ����Դ�����������
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());

        // 3.ִ�� HTTP POST����
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            // 4.�жϷ��ʵ�״̬��
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: "
                        + postMethod.getStatusLine());
            }
            // 5.���� HTTP��Ӧ����
            // HTTP��Ӧͷ����Ϣ������򵥴�ӡ
            Header[] headers = postMethod.getResponseHeaders();
            for (Header h : headers) {
                System.out.println(h.getName() + " " + h.getValue());
            }
            // ��ȡ HTTP ��Ӧ���ݣ�����򵥴�ӡ��ҳ����
            byte[] responseBody = postMethod.getResponseBodyAsString().getBytes("gb2312");// ��ȡΪ�ֽ�����
            ret = new String(responseBody);
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
