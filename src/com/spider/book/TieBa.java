package com.spider.book;

import com.spider.HttpClientFactory;
import com.util.StringUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;

import java.net.URLEncoder;
import java.util.Arrays;


/**
 * @author nidayu
 * @Description:
 * @date 2015/10/22
 */
public class TieBa extends HttpClientFactory {

    private CloseableHttpClient httpClient;

    public TieBa(CloseableHttpClient httpClient){
        this.httpClient = httpClient;
    }

    public static final String name = "魔天记";
    public static final String testChapter = "1553";
    public static final String chinese = StringUtil.replaceToChinese(Integer.parseInt(testChapter));
    public static final String flag = "a[title*=" + "完本感言" + "]";
    public static final String chinaFlag = "a[title*=" + chinese + "]";
    /**
     * 获取连载贴
     * @param bookName
     * @param chapter
     */
    public void getTab(String bookName, String chapter){
        String urlName = null;
        try {
            urlName = URLEncoder.encode(bookName, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String url = "http://tieba.baidu.com/f?kw="+urlName+"&ie=utf-8&tab=good";
        String[][] tieBaHeader = {{"Host", "tieba.baidu.com"}};
        String firstHtml = getUrl(url, tieBaHeader);
//        print(firstHtml);
        //根据连载查找关键字
        if ((Jsoup.parse(firstHtml).select(flag)!=null&&Jsoup.parse(firstHtml).select(flag).size()>0)){
            String chapterHref = Jsoup.parse(firstHtml).select(flag).get(0).attr("href");
            System.out.println(Jsoup.parse(firstHtml).select(flag).attr("title"));
            String[][] headers = {{"Referer", url}, {"Host", "tieba.baidu.com"}};
            if (chapterHref != null && !chapterHref.startsWith("http")) {
                chapterHref = "http://tieba.baidu.com" + chapterHref;
            }
            String chapterInfo = getUrl(chapterHref, headers);
            String bookText = Jsoup.parse(chapterInfo).select("div").text();
            String[] list = bookText.split(" ");
            Arrays.asList(list).stream().filter(text -> text != null && !"".equals(text.replace(" ", ""))).forEach(text -> print(text));
        }else if(Jsoup.parse(firstHtml).select(chinaFlag)!=null&&Jsoup.parse(firstHtml).select(chinaFlag).size()>0){
            String chapterHref = Jsoup.parse(firstHtml).select(chinaFlag).get(0).attr("href");
            System.out.println(Jsoup.parse(firstHtml).select(chinaFlag).attr("title"));
            String[][] headers = {{"Referer", url}, {"Host", "tieba.baidu.com"}};
            if (chapterHref != null && !chapterHref.startsWith("http")) {
                chapterHref = "http://tieba.baidu.com" + chapterHref;
            }
            String chapterInfo = getUrl(chapterHref, headers);
            String bookText = Jsoup.parse(chapterInfo).select("div").text();
            String[] list = bookText.split(" ");
            Arrays.asList(list).stream().filter(text -> text != null && !"".equals(text.replace(" ", ""))).forEach(text -> print(text));
        }else {
            String jingPingTie = Jsoup.parse(firstHtml).select("a").stream().filter(a -> a.text().contains("连载")).map(a -> a.attr("href")).findFirst().get();
            String newTab = StringUtil.subStr("cid=", "#", jingPingTie + "#");
            url = "http://tieba.baidu.com/f?kw=" + urlName + "&ie=utf-8&tab=good&cid=" + newTab + "&pn=";
            printBook(url, chapter, newTab, 0);
        }
    }

    /**
     * 打印
     * @param url
     * @param chapter
     * @param tabNum
     */
    public void printBook(String url, String chapter, String tabNum, int pageNo){
        String nextHtml = getUrl(url+pageNo*50);
        String chapterHref = null;
        if (Jsoup.parse(nextHtml).select(flag)!=null&&Jsoup.parse(nextHtml).select(flag).size()>0) {
            chapterHref = Jsoup.parse(nextHtml).select(flag).get(0).attr("href");
            System.out.println(Jsoup.parse(nextHtml).select(flag).attr("title"));
        }else if (Jsoup.parse(nextHtml).select(chinaFlag)!=null&&Jsoup.parse(nextHtml).select(chinaFlag).size()>0){
            chapterHref = Jsoup.parse(nextHtml).select(chinaFlag).get(0).attr("href");
            System.out.println(Jsoup.parse(nextHtml).select(chinaFlag).attr("title"));
        }else{
            printBook(url, chapter, tabNum, pageNo+1);
            return;
        }
        String[][] headers = {{"Referer", url}, {"Host", "tieba.baidu.com"}};
        if (chapterHref != null && !chapterHref.startsWith("http")) {
            chapterHref = "http://tieba.baidu.com" + chapterHref;
        }
        String chapterInfo = getUrl(chapterHref, headers);
        if (chapter == null){
            return;
        }
        String bookText = Jsoup.parse(chapterInfo).select("div").text();
        String[] list = bookText.split(" ");
        Arrays.asList(list).stream().filter(text -> text != null && !"".equals(text.replace(" ", ""))).forEach(text -> print(text));
    }


    public static void main(String[] args) {
        CloseableHttpClient httpClient= getInstance();
        TieBa tieBa = new TieBa(httpClient);
        tieBa.getTab(name, testChapter);
    }

}
