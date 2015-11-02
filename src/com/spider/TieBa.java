package com.spider;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;


/**
 * @author nidayu
 * @Description:
 * @date 2015/10/22
 */
public class TieBa extends HttpClientFactory{

    private CloseableHttpClient httpClient;

    public TieBa(CloseableHttpClient httpClient){
        this.httpClient = httpClient;
    }

    public static void main(String[] args){
        CloseableHttpClient httpClient= getInstance();
        TieBa tieBa = new TieBa(httpClient);
        int index = 19;

        // 贴吧
        String tiebaUrl = null, tiebaChapter = null;
        // 灵域
        // tiebaUrl = "http://tieba.baidu.com/f?kw=%E7%81%B5%E5%9F%9F&ie=gbk&tab=good&cid=0&pn=300";tiebaChapter = "一千四百三十四章";
        // 魔天记
        tiebaUrl = "http://tieba.baidu.com/f?kw=%E9%AD%94%E5%A4%A9%E8%AE%B0&ie=utf-8&tab=good&cid=2";tiebaChapter = "1411";
        // 一世之尊
        tiebaUrl = "http://tieba.baidu.com/f?kw=%E4%B8%80%E4%B8%96%E4%B9%8B%E5%B0%8A&ie=utf-8&tab=good&cid=2"; tiebaChapter = "二十章";

        tieBa.getTieBa(tiebaUrl, tiebaChapter);


        // 起点
        String url = null, chapter = null;
        index = 115;
        // 一剑飞仙
        // url = "http://read.qidian.com/BookReader/Rph5iVEas1Q1.aspx"; chapter = "二十二、";
        // 真武世界
        // url = "http://read.qidian.com/BookReader/SsH0QR3uBSU1.aspx"; chapter = "第一章";
        // 盛唐崛起
        // url = "http://read.qidian.com/BookReader/PHJRvEIGX-Y1.aspx"; chapter = "第九章";
        // 巫神纪
        url = "http://read.qidian.com/BookReader/wjGb4uJndg01.aspx"; chapter = foematInteger(index)+"章";

        // tieBa.getQiDian(url, chapter, false);


        // 纵横
        String zonghengUrl = null, zonghengChapter = null;
        // 终极教师
        // index = 24;
        // zonghengUrl = "http://book.zongheng.com/showchapter/347511.html"; zonghengChapter = foematInteger(index)+"章";
        // 御天神帝
        // zonghengUrl = "http://book.zongheng.com/showchapter/457529.html"; zonghengChapter = "0181";
        // tieBa.getZongHeng(zonghengUrl, zonghengChapter);

        closeHttpClient(httpClient);
    }

    // 获取贴吧数据
    private void getTieBa(String url, String chapter){
        // 正文
        String[][] tiebaHeader = {{"Host", "tieba.baidu.com"}};
        String tiebaHtml = getUrl(url, tiebaHeader);
        String s = Jsoup.parse(tiebaHtml).select("a[title*=" + chapter +"]").attr("href");
        System.out.println(Jsoup.parse(tiebaHtml).select("a[title*=" + chapter +"]").attr("title"));
        String[][] headers = {{"Referer", url}, {"Host", "tieba.baidu.com"}};
        if (!s.startsWith("http")){
            s = "http://tieba.baidu.com"+s;
        }
        String tiebaInfo = getUrl(s, headers);
        String text = Jsoup.parse(tiebaInfo).select("div").text();
        String[] list = text.split(" ");
        for (int i=0; i<list.length; i++) {
            System.out.println(list[i]);
        }
    }


    // 获取起点数据
    private void getQiDian(String url, String chapter, boolean isVip){
        // 正文
        String qidianHtml = getUrl(url, null);
        String qidianInfo = Jsoup.parse(qidianHtml).select("a").stream()
                .filter(a -> a.text().contains(chapter))
                .map(a -> a.attr("href"))
                .findFirst().get();
        qidianHtml = getUrl(qidianInfo, null);
        String des;
        if (!isVip) {
            // 非vip用户
            des = Jsoup.parse(qidianHtml).select("script[src^=http://files]").attr("src");
            String[][] qidianHeader = {
                    {"Host", "files.qidian.com"}, {"Referer", qidianInfo},
                    {"Accept-Encoding", "deflate, sdch"}, {"Content-Type", "text/plain"},
                    {"Accept-Ranges", "bytes"}, {"Content-Encoding", "gzip"}
            };
            qidianHtml = getUrl(des, qidianHeader, "gbk");
            Jsoup.parse(qidianHtml).select("p").stream().forEach(p -> System.out.println(p.text()));
        }else{
            // vip用户
            des = Jsoup.parse(qidianHtml).select(".read_ma").select("a").attr("href");
            String[][] vipHeader = {
                    {"Host", "vipreader.qidian.com"}, {"Content-Type", "text/html;charset=utf-8"}
            };
            qidianHtml = getUrl(des, null);
            Jsoup.parse(qidianHtml).select("p").stream().forEach(p -> System.out.println(p.text()));
        }

    }


    // 获取纵横数据
    private void getZongHeng(String url, String chapter){
        // 正文
        String[][] headers = {
                {"Host", "book.zongheng.com"}, {"Content-Type", "text/html; charset=UTF-8"}
        };
        String zonghengHtml = getUrl(url, headers);
        String chapterId = Jsoup.parse(zonghengHtml).select("td[chapterName*=" + chapter + "]").attr("chapterId");
        String zonghengUrlDes = url.substring(0, url.length() - 5)+"/"+chapterId+".html";
        String[][] zonghengHeader = {
                {"Referer", url}, {"Host", "book.zongheng.com"}, {"Content-Type", "text/html; charset=UTF-8"},
                {"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"}
        };
        String newUrl = zonghengUrlDes.replace("showchapter", "chapter");
//        sleep(1000);
        String html = getUrl(newUrl, zonghengHeader);
        Jsoup.parse(html).select("p").stream().forEach(p -> System.out.println(p.text()));
    }

    public static void sleep(int time){
        try {
            Thread.sleep((long)time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    // 把数字转换为中文！
    static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
            "十亿", "百亿", "千亿", "万亿" };
    static char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };

    private static String foematInteger(int num) {
        String ret = null;
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        ret = sb.toString();
        // 当传入参数为十几的时候，去掉前面的一
        if (num > 9 && num <20){
            ret = ret.substring(1);
        }
        return ret;
    }

}
