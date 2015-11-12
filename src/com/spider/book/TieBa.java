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

    /**
     * 获取连载贴
     * @param bookName
     * @param chapter
     * @param tabNum
     */
    public void getTab(String bookName, String chapter, int tabNum){
        String urlName = null;
        try {
            urlName = URLEncoder.encode(bookName, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String url = "http://tieba.baidu.com/f?kw=" + urlName + "&ie=utf-8&tab=good&cid=0&pn=0";
        String[][] tieBaHeader = {{"Host", "tieba.baidu.com"}};
        String firstHtml = getUrl(url, tieBaHeader);
        //根据连载查找关键字
        String jingPingTie = Jsoup.parse(firstHtml).select("a").stream().filter(a -> a.text().contains("连载")).map(a -> a.attr("href")).findFirst().get();
        String newTab = StringUtil.subStr("cid=", "#", jingPingTie+"#");
        url = "http://tieba.baidu.com/f?kw=" + urlName + "&ie=utf-8&tab=good&cid="+newTab+"&pn=";
        printBook(url, chapter, newTab, 0);
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
        String chinese = StringUtil.replaceToChinese(Integer.parseInt(chapter));
        if (Jsoup.parse(nextHtml).select("a[title*=第" + chapter + "章]")!=null&&Jsoup.parse(nextHtml).select("a[title*=第" + chapter + "章]").size()>0) {
            chapterHref = Jsoup.parse(nextHtml).select("a[title*=" + chapter + "]").get(0).attr("href");
            System.out.println(Jsoup.parse(nextHtml).select("a[title*=" + chapter + "]").attr("title"));
        }else if (Jsoup.parse(nextHtml).select("a[title*=第" + chinese + "章]")!=null&&Jsoup.parse(nextHtml).select("a[title*=第" + chinese + "章]").size()>0){
            chapterHref = Jsoup.parse(nextHtml).select("a[title*=" + chinese + "]").get(0).attr("href");
            System.out.println(Jsoup.parse(nextHtml).select("a[title*=" + chinese + "]").attr("title"));
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
        String name = "永恒剑主";
        // 贴吧
        String TestChapter = "108";
        tieBa.getTab(name, TestChapter, 0);
    }

}
