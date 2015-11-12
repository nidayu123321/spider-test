//package com.test;
//
//import com.spider.HttpClientFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.net.URLEncoder;
//import java.util.Arrays;
//import java.util.function.Predicate;
//
//
///**
// * @author nidayu
// * @Description:
// * @date 2015/10/22
// */
//public class Test extends HttpClientFactory {
//
//    private CloseableHttpClient httpClient;
//
//    public Test(CloseableHttpClient httpClient){
//        this.httpClient = httpClient;
//    }
//
//    public static void main(String[] args){
//        CloseableHttpClient httpClient= getInstance();
//        Test Test = new Test(httpClient);
//        String name = "���𽣻�";
//        // ����
//        String TestChapter = "�ڶ�ʮ����";
//        Test.getTest(name, TestChapter, 0);
//
//        // ���
//        String url = null, chapter = null;
////        index = 115;
////        һ������
////        url = "http://read.qidian.com/BookReader/Rph5iVEas1Q1.aspx"; chapter = "��ʮ����";
////        ��������
////        url = "http://read.qidian.com/BookReader/SsH0QR3uBSU1.aspx"; chapter = "��һ��";
////        ʢ������
////        url = "http://read.qidian.com/BookReader/PHJRvEIGX-Y1.aspx"; chapter = "�ڶ�ʮ����";
////        ���㽣��
////        url = "http://read.qidian.com/BookReader/RKX-XWZ0I1k1.aspx"; chapter = "��ʮ����";
////        ������
////        url = "http://read.qidian.com/BookReader/82plFx6pHLY1.aspx"; chapter = "������";
////        Test.getQiDian(url, chapter, false);
//
//
//        // �ݺ�
//        String zonghengUrl = null, zonghengChapter = null;
////        �ռ���ʦ
////        index = 24;
////        zonghengUrl = "http://book.zongheng.com/showchapter/347511.html"; zonghengChapter = foematInteger(index)+"��";
////        �������
////        zonghengUrl = "http://book.zongheng.com/showchapter/457529.html"; zonghengChapter = "0181";
//        Test.getZongHeng(zonghengUrl, zonghengChapter);
//
//        closeHttpClient(httpClient);
//    }
//
//    // ��ȡ��������
//    private void getTest(String name, String chapter, int i){
//        String url = null;
//        try {
//            url = "http://Test.baidu.com/f?kw=" + URLEncoder.encode(name, "utf-8") + "&ie=utf-8&tab=good&cid="+i+"&pn=0";
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        String[][] TestHeader = {{"Host", "Test.baidu.com"}};
//        String TestHtml = getUrl(url, TestHeader);
//        String s = null;
//        try {
//            s = Jsoup.parse(TestHtml).select("a[title*=" + chapter + "]").get(0).attr("href");
//        }catch (Exception e){
//            if (i<5) {
//                getTest(name, chapter, i + 1);
//                return;
//            }
//        }
//        System.out.println(Jsoup.parse(TestHtml).select("a[title*=" + chapter +"]").attr("title"));
//        String[][] headers = {{"Referer", url}, {"Host", "Test.baidu.com"}};
//        if (s!= null && !s.startsWith("http")){
//            s = "http://Test.baidu.com"+s;
//        }
//        String TestInfo = getUrl(s, headers);
//        String text = Jsoup.parse(TestInfo).select("div").text();
//        String[] list = text.split(" ");
//        // ��ɾ��
//        Arrays.asList(list).stream().forEach(s1 ->  System.out.println(s1));
//        // ɾ����ͷ�ͽ�β
////        Predicate<String> limit = string -> string.contains("--------------------------")
////                || (string.contains("δ�����") || string.contains("�����ڴ�"));
////        int begin = Arrays.asList(list).stream().filter(limit).mapToInt(string -> Arrays.asList(list).indexOf(string)).min().getAsInt();
////        int end = Arrays.asList(list).stream().filter(limit).mapToInt(string -> Arrays.asList(list).indexOf(string)).max().getAsInt();
////        Arrays.asList(list).stream().skip(begin).limit(end-begin+1).forEach(s1 ->  System.out.println(s1));
//    }
//
//
//    // ��ȡ�������
//    private void getQiDian(String url, String chapter, boolean isVip){
//        String qidianHtml = getUrl(url, null);
//        String qidianInfo = Jsoup.parse(qidianHtml).select("a").stream()
//                .filter(a -> a.text().contains(chapter))
//                .map(a -> a.attr("href"))
//                .findFirst().get();
//        qidianHtml = getUrl(qidianInfo, null);
//        String des;
//        if (!isVip) {
//            // ��vip�û�
//            des = Jsoup.parse(qidianHtml).select("script[src^=http://files]").attr("src");
//            String[][] qidianHeader = {
//                    {"Host", "files.qidian.com"}, {"Referer", qidianInfo},
//                    {"Accept-Encoding", "deflate, sdch"}, {"Content-Type", "text/plain"},
//                    {"Accept-Ranges", "bytes"}, {"Content-Encoding", "gzip"}
//            };
//            qidianHtml = getUrl(des, qidianHeader, "gbk");
//            Jsoup.parse(qidianHtml).select("p").stream().forEach(p -> System.out.println(p.text()));
//        }else{
//            // vip�û�
//            des = Jsoup.parse(qidianHtml).select(".read_ma").select("a").attr("href");
//            String[][] vipHeader = {
//                    {"Host", "vipreader.qidian.com"}, {"Content-Type", "text/html;charset=utf-8"}
//            };
//            qidianHtml = getUrl(des, null);
//            Jsoup.parse(qidianHtml).select("p").stream().forEach(p -> System.out.println(p.text()));
//        }
//
//    }
//
//
//    // ��ȡ�ݺ�����
//    private void getZongHeng(String url, String chapter){
//        if (url == null || chapter == null) {
//            return;
//        }
//        String zonghengHtml = getUrl(url);
//        String chapterId = Jsoup.parse(zonghengHtml).select("td[chapterName*=" + chapter + "]").attr("chapterId");
//        String zonghengUrlDes = url.substring(0, url.length() - 5)+"/"+chapterId+".html";
//        String[][] zonghengHeader = {
//                {"Accept-Encoding", "gzip, deflate, sdch"}
//        };
//        String newUrl = zonghengUrlDes.replace("showchapter", "chapter");
//        String html = getUrl(newUrl, zonghengHeader);
//        Elements elements = Jsoup.parse(html).select("p");
//        Predicate<Element> limit = node -> node.text().contains("ע���ʻ�|�һ�����|��������") || node.text().contains("Ĭ���� ���ش�");
//        int begin = elements.stream().filter(limit).mapToInt(node -> elements.indexOf(node)).min().getAsInt();
//        int end = elements.stream().filter(limit).mapToInt(node ->elements.indexOf(node)).max().getAsInt();
//        elements.stream().skip(begin).limit(end - begin + 1).forEach(p ->  System.out.println(p.text()));
//    }
//
//    public static void sleep(int time){
//        try {
//            Thread.sleep((long)time);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // ������ת��Ϊ���ģ�
//    static String[] units = { "", "ʮ", "��", "ǧ", "��", "ʮ��", "����", "ǧ��", "��",
//            "ʮ��", "����", "ǧ��", "����" };
//    static char[] numArray = { '��', 'һ', '��', '��', '��', '��', '��', '��', '��', '��' };
//
//    private static String foematInteger(int num) {
//        String ret = null;
//        char[] val = String.valueOf(num).toCharArray();
//        int len = val.length;
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < len; i++) {
//            String m = val[i] + "";
//            int n = Integer.valueOf(m);
//            boolean isZero = n == 0;
//            String unit = units[(len - 1) - i];
//            if (isZero) {
//                if ('0' == val[i - 1]) {
//                    continue;
//                } else {
//                    sb.append(numArray[n]);
//                }
//            } else {
//                sb.append(numArray[n]);
//                sb.append(unit);
//            }
//        }
//        ret = sb.toString();
//        // ���������Ϊʮ����ʱ��ȥ��ǰ���һ
//        if (num > 9 && num <20){
//            ret = ret.substring(1);
//        }
//        return ret;
//    }
//
//}
