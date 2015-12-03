package com.spider.email;

import com.spider.HttpClientFactory;

import javax.mail.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author nidayu
 * @Description: 邮箱
 * @date 2015/12/1
 */
public class Email extends HttpClientFactory {


    public static void main(String[] args) {
        String email = "18801320760@163.com";
        String password = "nidayu163";
        //收邮件的参数配置
        MailReceiverInfo receiverInfo = new MailReceiverInfo();
        //默认设置
        receiverInfo.setValidate(true);
        receiverInfo.setMailServerHost("pop.163.com");
        receiverInfo.setUserName(email);//即：@符号前面的部分
        receiverInfo.setPassword(password);
        //与邮件服务器连接后得到的邮箱
        //与邮件服务器连接后得到的邮箱
        Store store;
        // 收件箱
        Folder folder;
        // 收件箱中的邮件消息
        Message[] messages;
        // 当前正在处理的邮件消息
        Message currentMessage;
        MyAuthenticator authenticator = null;
        if (receiverInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(receiverInfo.getUserName(), receiverInfo.getPassword());
        }
        //创建session
        Session session = Session.getInstance(receiverInfo.getProperties(), authenticator);
        try {
            store = session.getStore(receiverInfo.getProtocal());
            store.connect();

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            messages = folder.getMessages();
            print("Messages's length: " + messages.length);
            if(messages.length > 0){
                Message message = messages[0];
                print(" subject: " + message.getSubject());
                print(" sentdate: " + message.getSentDate());
                print(" form: " + message.getFrom());
                // 获得邮件内容===============
                print("获得邮件内容======================");
                try {
                    String text = getMailContent((Part) message);
                    print(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static StringBuffer bodytext = new StringBuffer();//存放邮件内容
    private static String getMailContent(Part part){
        String contenttype = "";
        try {
            contenttype = part.getContentType();
            int nameindex = contenttype.indexOf("name");
            boolean conname = false;
            if (nameindex != -1)
                conname = true;
            print("CONTENTTYPE: " + contenttype);
            if (part.isMimeType("text/plain") && !conname) {
                bodytext.append((String) part.getContent());
            } else if (part.isMimeType("text/html") && !conname) {
                bodytext.append((String) part.getContent());
            } else if (part.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part.getContent();
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    getMailContent(multipart.getBodyPart(i));
                }
            } else if (part.isMimeType("message/rfc822")) {
                getMailContent((Part) part.getContent());
            } else {}
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bodytext.toString();
    }

}


class MailReceiverInfo {
    // 邮件服务器的IP、端口和协议
    private String mailServerHost;
    private String mailServerPort = "110";
    private String protocal = "pop3";
    // 登陆邮件服务器的用户名和密码
    private String userName;
    private String password;
    // 是否需要身份验证
    private boolean validate = true;
    private Map<String, Object> prop = new HashMap<String, Object>();

    public Properties getProperties(){
        Properties p = new Properties();
        p.setProperty("mail.pop3.timeout", "60000");
        p.put("mail.pop3.host", this.mailServerHost);
        p.put("mail.pop3.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        p.putAll(prop);
        return p;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getProp() {
        return prop;
    }

    public void setProp(Map<String, Object> prop) {
        this.prop = prop;
    }
}

class MyAuthenticator extends Authenticator {
    private String username;

    private String password;

    public MyAuthenticator(String username, String pwd) {
        this.username = username;
        this.password = pwd;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username, this.password);
    }

}

