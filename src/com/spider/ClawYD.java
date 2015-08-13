package com.spider;

import com.test.DateUtil;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;

/**
 * @author nidayu
 * @Description:移动测试
 * @date 2015/7/23
 */
public class ClawYD {

    private HttpClient httpClient;
    private String phoneNo;
    private String password;
    private String authCode;
    private String smsCode;

    public ClawYD(HttpClient httpClient, String phoneNo, String password, String authCode){
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
    public static void saveFile(HttpClient httpClient, String url, String filePath){
        // 2.生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        // 设置 get 请求超时为 5 秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        //设置请求头参数
//		getMethod.setRequestHeader(new Header("Accept", "*/*"));
//		getMethod.setRequestHeader(new Header("Accept-Encoding", "gzip, deflate"));
//		getMethod.setRequestHeader(new Header("Accept-Language", "zh-CN"));
//		getMethod.setRequestHeader(new Header("Connection", "Keep-Alive"));
        // 3. 执行 HTTP GET请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            // 4. 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
            // 读取 HTTP 响应内容
            // 读取为 InputStream，在网页内容数据量大时候推荐使用
            InputStream response = getMethod.getResponseBodyAsStream();
            File file=new File(filePath);
            OutputStream os=null;
            try{
                os=new FileOutputStream(file);
                byte buffer[]=new byte[4*1024];
                int len = 0;
                while((len = response.read(buffer)) != -1){
                    os.write(buffer,0,len);
                }
                os.flush();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{
                    os.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
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
    }

    /**
     * 登录抓取余额
     */
    private void goLogin(){
        String url = "https://login.10086.cn/login.htm?accountType=01&account="+phoneNo+"&password="+password+"&pwdType=01&inputCode="+authCode+"&backUrl=http://shop.10086.cn/i/&rememberMe=0&channelID=12003&protocol=https:";
        String[][] headers = {{"Referer", "https://login.10086.cn/login.html?channelID=12003&backUrl=http://shop.10086.cn/i/"}, {"Accept", "application/json, text/javascript, */*; q=0.01"},
                {"Host", "login.10086.cn"}, {"Content-Type", "application/json; charset=utf-8"}, {"Accept-Encoding", "gzip, deflate"}};
        String text = SpiderManager.getUrl(httpClient, url, headers);
        if(text!=null){
            JSONObject obj = new JSONObject(text);
            if(obj.optString("code").contains("0000")){
                String artifact = obj.optString("artifact");
                url = obj.optString("assertAcceptURL");
                url = url +"?backUrl=http://shop.10086.cn/i/&artifact="+artifact;
                SpiderManager.getUrl(httpClient, url, null);
                url = "http://shop.10086.cn/i/?welcome="+System.currentTimeMillis();
                SpiderManager.getUrl(httpClient, url, null);
                url = "http://shop.10086.cn/i/v1/fee/real/"+phoneNo+"?time="+System.currentTimeMillis();
                text = SpiderManager.getUrl(httpClient, url, null);
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
        String text = SpiderManager.getUrl(httpClient, url, null);
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
        SpiderManager.getUrl(httpClient, url, null);
        url = "https://login.10086.cn/sendSMSpwd.action?callback=result&userName=" + phoneNo + "&_=" + System.currentTimeMillis();
        String text = SpiderManager.getUrl(httpClient, url, null);
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
        String text = SpiderManager.getUrl(httpClient, url, null);
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
        String text = SpiderManager.getUrl(httpClient, url, null);
        System.out.println("通话记录...");
        System.out.println(text);
    }

    public static void main(String[] args) {
//        HttpClient httpClient = SpiderManager.getInstance();
//        String phoneNo = "18317042860";
//        String password = "163163";
//        String authCode = null;
//        saveFile(httpClient, "https://login.10086.cn/captchazh.htm?type=05&timestamp=" + System.currentTimeMillis(), "C:\\a.jpg");
//        System.out.println("请输入验证码:");
//        Scanner in = new Scanner(System.in);
//        authCode = in.nextLine();
//        ClawYD clawYD = new ClawYD(httpClient, phoneNo, password, authCode);
//        clawYD.goLogin();
//        clawYD.reqMonthBill();
//        clawYD.sendSms();
//        System.out.println("请输入验证码:");
//        in = new Scanner(System.in);
//        clawYD.smsCode = in.nextLine();
//        clawYD.checkSms(clawYD.smsCode);
//        clawYD.reqCallDetail("201507", 1);
        String[][] headers = {{"Hsot", "gd.ac.10086.cn"}};
        HttpClient httpClient = SpiderManager.getInstance();
        String text = SpiderManager.getUrl(httpClient, "https://gd.ac.10086.cn/ucs/login/signup.jsps", null);
        System.out.println(text);
    }
}
