
package com.spider;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author nidayu
 * @Description:
 * @date 2015/8/27
 */
public class LianTong extends HttpClientFactory{

    private String phoneNo;
    private String password;
    private String authCode;
    private CloseableHttpClient httpClient;

    public LianTong(CloseableHttpClient httpClient, String phoneNo, String password, String authCode){
        this.httpClient = httpClient;
        this.phoneNo = phoneNo;
        this.password = password;
        this.authCode = authCode;
    }

    private void goLogin(){
        String url = "https://uac.10010.com/portal/Service/MallLogin?redirectURL=http%3A%2F%2Fwww.10010.com&userName="+phoneNo+"&password="+password+"&pwdType=01&productType=01&redirectType=01&rememberMe=1&_="+System.currentTimeMillis();
        String text = getUrl(url, null);
        System.out.println(text);
        if (text.contains("0000")) {
            url = "http://iservice.10010.com/e3/static/check/checklogin/";
            String[][] params = {{"_", System.currentTimeMillis() + "" }};
            String[][] headers = {{"Accept", "application/json, text/javascript, */*; q=0.01"}};
            text = postUrl(url, params, headers);
            System.out.println(text);
        }
    }

    public static void main(String[] args) {
        CloseableHttpClient httpClient = getInstance();
        String phoneNo = "18570913849";
        String password = "198888";
        LianTong lianTong = new LianTong(httpClient, phoneNo, password, null);
        lianTong.goLogin();
        //打印当前cookie信息
        print(getCookie());
        closeHttpClient(httpClient);
    }
}
