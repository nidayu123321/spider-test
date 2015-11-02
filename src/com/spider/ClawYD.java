
package com.spider;

import com.util.DateUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;

import java.util.Date;
import java.util.Scanner;

/**
 * @author nidayu
 * @Description:
 * @date 2015/8/27
 */
public class ClawYD extends HttpClientFactory{

    private CloseableHttpClient httpClient;
    private String phoneNo;
    private String password;
    private String authCode;
    private String smsCode;

    public ClawYD(CloseableHttpClient httpClient, String phoneNo, String password, String authCode){
        this.httpClient = httpClient;
        this.phoneNo = phoneNo;
        this.password = password;
        this.authCode = authCode;
    }

    /**
     * 下载图片验证码
     * @param httpClient
     * @param url
     * @param filePath
     */
    public void saveFile(CloseableHttpClient httpClient, String url, String filePath){
        downloadImgByGet(url, filePath);
    }

    /**
     * 登录抓取余额
     */
    private void goLogin(String authCode){
        String url = "https://login.10086.cn/login.htm?accountType=01&account="+phoneNo+"&password="+password+"&pwdType=01&inputCode="+authCode+"&backUrl=http://shop.10086.cn/i/&rememberMe=0&channelID=12003&protocol=https:";
        String[][] headers = {{"Referer", "https://login.10086.cn/login.html?channelID=12003&backUrl=http://shop.10086.cn/i/"}, {"Accept", "application/json, text/javascript, */*; q=0.01"},
                {"Host", "login.10086.cn"}, {"Content-Type", "application/json; charset=utf-8"}, {"Accept-Encoding", "gzip, deflate"}};
        String text = getUrl(url, headers);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(text!=null){
            JSONObject obj = new JSONObject(text);
            if(obj.optString("code").contains("0000")){
                String artifact = obj.optString("artifact");
                url = obj.optString("assertAcceptURL");
                url = url +"?backUrl=http://shop.10086.cn/i/&artifact="+artifact;
                getUrl(url);
                url = "http://shop.10086.cn/i/?welcome="+System.currentTimeMillis();
                getUrl(url);
                url = "http://shop.10086.cn/i/v1/fee/real/"+phoneNo+"?time="+System.currentTimeMillis();
                text = getUrl(url);
                System.out.println("余额抓取...");
                System.out.println(text);
                obj = new JSONObject(text);
                if (obj.optString("retCode").contains("000000")) {
                    System.out.println("登录成功...");
                }
            }
        }
    }

    /**
     * 查询账单
     */
    private void reqMonthBill(){
        String url = "http://shop.10086.cn/i/v1/fee/billinfo/" + phoneNo + "?time=" + DateUtil.formatDate(new Date(), "yyyyMdHHmmsss");
        String text = getUrl(url);
        System.out.println("账单抓取...");
        System.out.println(text);
    }

    /**
     * 发送短信
     */
    private void sendSms(){
        String time = DateUtil.formatDate(new Date(), "yyyyMdHHmmssssss");
        if (time.length() > 16) {
            time = time.substring(0, 16);
        }
        String url = "https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/" + phoneNo + "?callback=result&curCuror=1&step=100&qryMonth=" + DateUtil.getToday("yyyyMM") + "&billType=02&time=" + time + "&_=" + System.currentTimeMillis();
        getUrl(url);
        url = "https://login.10086.cn/sendSMSpwd.action?callback=result&userName=" + phoneNo + "&_=" + System.currentTimeMillis();
        String text = getUrl(url);
        if (text != null && text.contains("\"resultCode\":\"0\"")) {
            System.out.println("发送成功...");
        } else {
            System.out.println("发送失败...");
        }
    }

    /**
     * 验证短信
     * @param smsCode
     */
    private void checkSms(String smsCode){
        String url = "https://login.10086.cn/temporaryauthSMSandService.action?callback=result&account=" + phoneNo + "&servicePwd=" + password + "&smsPwd=" + smsCode + "&accountType=01&backUrl=&channelID=12003&businessCode=01&_=" + System.currentTimeMillis();
        String text = getUrl(url);
        System.out.println("验证短信...");
        if (text != null && text.contains("\"code\":\"0000\"")) {
            System.out.println("验证通过...");
        }
    }

    /**
     * 抓取当月通话
     * @param month
     * @param pageNo
     */
    private void reqCallDetail(String month, int pageNo){
        String time = DateUtil.formatDate(new Date(), "yyyyMdHHmmssssss");
        if (time.length() > 16) {
            time = time.substring(0, 16);
        }
        String url = "https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/" + phoneNo + "?callback=result&curCuror=" + ((pageNo - 1) * 100 + 1) + "&step=100&qryMonth=" + month + "&billType=02&time=" + time + "&_=" + System.currentTimeMillis();
        String text = getUrl(url);
        System.out.println("通话记录...");
        System.out.println(text);
    }

    public static void main(String[] args) {
        CloseableHttpClient httpClient= getInstance();
        String phoneNo = "18317042860";
        String password = "163163";
        String authCode = null;
        ClawYD clawYD = new ClawYD(httpClient, phoneNo, password, authCode);
        clawYD.saveFile(httpClient, "https://login.10086.cn/captchazh.htm?type=05&timestamp=" + System.currentTimeMillis(), "C:\\a.jpg");
        System.out.println("请输入验证码:");
        Scanner in = new Scanner(System.in);
        authCode = in.nextLine();
        clawYD.goLogin(authCode);
        clawYD.reqMonthBill();
        clawYD.sendSms();
        System.out.println("请输入验证码:");
        in = new Scanner(System.in);
        clawYD.smsCode = in.nextLine();
        clawYD.checkSms(clawYD.smsCode);
        clawYD.reqCallDetail("201507", 1);
        closeHttpClient(httpClient);
    }

}
