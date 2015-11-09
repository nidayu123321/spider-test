package com.spider;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.function.Predicate;


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
        String name = "完美世界小说";
        // 贴吧
        String tiebaChapter = "一千五百一十七章";
        tieBa.getTieBa(name, tiebaChapter, 0);

        // 起点
        String url = null, chapter = null;
//        index = 115;
//        一剑飞仙
//        url = "http://read.qidian.com/BookReader/Rph5iVEas1Q1.aspx"; chapter = "二十二、";
//        真武世界
//        url = "http://read.qidian.com/BookReader/SsH0QR3uBSU1.aspx"; chapter = "第一章";
//        盛唐崛起
//        url = "http://read.qidian.com/BookReader/PHJRvEIGX-Y1.aspx"; chapter = "第二十八章";
//        永恒剑主
//        url = "http://read.qidian.com/BookReader/RKX-XWZ0I1k1.aspx"; chapter = "八十三章";
//        五行天
//        url = "http://read.qidian.com/BookReader/82plFx6pHLY1.aspx"; chapter = "第三章";
//        tieBa.getQiDian(url, chapter, false);


        // 纵横
        String zonghengUrl = null, zonghengChapter = null;
//        终极教师
//        index = 24;
//        zonghengUrl = "http://book.zongheng.com/showchapter/347511.html"; zonghengChapter = foematInteger(index)+"章";
//        御天神帝
//        zonghengUrl = "http://book.zongheng.com/showchapter/457529.html"; zonghengChapter = "0181";
        tieBa.getZongHeng(zonghengUrl, zonghengChapter);

        closeHttpClient(httpClient);
    }

    // 获取贴吧数据
    private void getTieBa(String name, String chapter, int i){
        String url = null;
        try {
            url = "http://tieba.baidu.com/f?kw=" + URLEncoder.encode(name, "utf-8") + "&ie=utf-8&tab=good&cid="+i+"&pn=0";
        }catch (Exception e){
            e.printStackTrace();
        }
        String[][] tiebaHeader = {{"Host", "tieba.baidu.com"}};
        String tiebaHtml = getUrl(url, tiebaHeader);
        String s = null;
        try {
            s = Jsoup.parse(tiebaHtml).select("a[title*=" + chapter + "]").get(0).attr("href");
        }catch (Exception e){
            if (i<6) {
                getTieBa(name, chapter, i + 1);
                return;
            }
        }
        System.out.println(Jsoup.parse(tiebaHtml).select("a[title*=" + chapter +"]").attr("title"));
        String[][] headers = {{"Referer", url}, {"Host", "tieba.baidu.com"}};
        if (s!= null && !s.startsWith("http")){
            s = "http://tieba.baidu.com"+s;
        }
        String tiebaInfo = getUrl(s, headers);
        String text = Jsoup.parse(tiebaInfo).select("div").text();
        String[] list = text.split(" ");
        // 不删除
        Arrays.asList(list).stream().forEach(s1 ->  System.out.println(s1));
        // 删除开头和结尾
//        Predicate<String> limit = string -> string.contains("--------------------------")
//                || (string.contains("未完待续") || string.contains("敬请期待"));
//        int begin = Arrays.asList(list).stream().filter(limit).mapToInt(string -> Arrays.asList(list).indexOf(string)).min().getAsInt();
//        int end = Arrays.asList(list).stream().filter(limit).mapToInt(string -> Arrays.asList(list).indexOf(string)).max().getAsInt();
//        Arrays.asList(list).stream().skip(begin).limit(end-begin+1).forEach(s1 ->  System.out.println(s1));
    }


    // 获取起点数据
    private void getQiDian(String url, String chapter, boolean isVip){
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
        if (url == null || chapter == null) {
            return;
        }
        String zonghengHtml = getUrl(url);
        String chapterId = Jsoup.parse(zonghengHtml).select("td[chapterName*=" + chapter + "]").attr("chapterId");
        String zonghengUrlDes = url.substring(0, url.length() - 5)+"/"+chapterId+".html";
        String[][] zonghengHeader = {
                {"Accept-Encoding", "gzip, deflate, sdch"}
        };
        String newUrl = zonghengUrlDes.replace("showchapter", "chapter");
        String html = getUrl(newUrl, zonghengHeader);
        Elements elements = Jsoup.parse(html).select("p");
        Predicate<Element> limit = node -> node.text().contains("注册帐户|找回密码|帮助中心") || node.text().contains("默认中 大特大");
        int begin = elements.stream().filter(limit).mapToInt(node -> elements.indexOf(node)).min().getAsInt();
        int end = elements.stream().filter(limit).mapToInt(node ->elements.indexOf(node)).max().getAsInt();
        elements.stream().skip(begin).limit(end - begin + 1).forEach(p ->  System.out.println(p.text()));
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
