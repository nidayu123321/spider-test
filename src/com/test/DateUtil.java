package com.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author nidayu
 * @Description: 时间格式的转换
 * @date 2015/6/18
 */
public class DateUtil {
    public static final int PERIOD_TYPE_YEAR = 0;
    public static final int PERIOD_TYPE_MONTH = 1;
    public static final int PERIOD_TYPE_HALFMONTH = 2;
    public static final int PERIOD_TYPE_WEEK = 3;

    /**
     * 获取起始月份和结束月份中间的所有月份，包含起始月和结束月
     * @param start yyyy-MM
     * @param end yyyy-MM
     * @return yyyy-MM的数组
     */
    public static String[] getAllMonths(String start, String end){
        String splitSign="-";
        String regex="\\d{4}"+splitSign+"(([0][1-9])|([1][012]))"; //判断YYYY-MM时间格式的正则表达式
        if(!start.matches(regex) || !end.matches(regex)){
            return new String[0];
        }
        List<String> list=new ArrayList<String>();
        if(start.compareTo(end)>0){
            //start大于end日期时，互换
            String temp=start;
            start=end;
            end=temp;
        }
        String temp=start; //从最小月份开始
        while(temp.compareTo(start)>=0 && temp.compareTo(end)<=0){
            list.add(temp); //首先加上最小月份,接着计算下一个月份
            String[] arr=temp.split(splitSign);
            int year=Integer.valueOf(arr[0]);
            int month=Integer.valueOf(arr[1])+1;
            if(month>12){
                month=1;
                year++;
            }
            if(month<10){//补0操作
                temp=year+splitSign+"0"+month;
            }else{
                temp=year+splitSign+month;
            }
        }
        int size=list.size();
        String[] result=new String[size];
        for(int i=0;i<size;i++){
            result[i]=list.get(i);
        }
        return result;
    }

    /**
     * 输出从本月到本月以前的连续num个月份
     * @param num
     * @return yyyyMM的集合
     */
    public static List<String> getMonth(int num){
        List<String> objectTmp = new ArrayList<String>();
        java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        for (int i = 0; i < num; i++) {
            c.add(Calendar.MONTH, -1);
            Date date = c.getTime();
            String date2 = format2.format(date);
            objectTmp.add(date2);
        }
        return objectTmp;
    }

    /**
     * 按照format的格式输出从本月到本月以前的连续num个月份
     * @param num
     * @param format
     * @return
     */
    public static List<String> getMonths(int num,String format){
        List<String> objectTmp = new ArrayList<String>();
        java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        for (int i = 0; i < num; i++) {
            c.add(Calendar.MONTH, -1);
            Date date = c.getTime();
            String date2 = format2.format(date);
            objectTmp.add(date2);
        }
        return objectTmp;
    }

    /**
     * 按照format的格式输出从上个月到上个月以前的连续num个月份（不包含本月）
     * @param num
     * @param format
     * @return
     */
    public static List<String> getMonthsNotInclude(int num,String format){
        List<String> objectTmp = new ArrayList<String>();
        java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        for (int i = 0; i < num; i++) {
            c.add(Calendar.MONTH, -1);
            Date date = c.getTime();
            String date2 = format2.format(date);
            objectTmp.add(date2);
        }
        return objectTmp;
    }

    /**
     * 计算从dateStr距离今天多少天
     * @param dateStr
     * @param format 输入格式
     * @return
     * @throws Exception
     */
    public static int dayDist(String dateStr, String format) throws Exception{
        SimpleDateFormat df=new SimpleDateFormat(format);
        Date date=df.parse(dateStr);
        long timeMillion=new Date().getTime()-date.getTime();
        return (int)(timeMillion/(24l*60*60*1000));
    }

    /**
     * 按照format的格式输出上个月的今天
     * @param format
     * @return
     */
    public static String getLMDay(String format){
        java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        Date date = c.getTime();
        String date2 = format2.format(date);
        return date2;
    }

    /**
     * 按照format的格式输出去年的今天
     * @param format
     * @return
     */
    public static String getLYDay(String format){
        java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        Date date = c.getTime();
        String date2 = format2.format(date);
        return date2;
    }

    /**
     * 按照format的格式输出去n个月前的今天
     * @param n
     * @param format
     * @return
     */
    public static String getLMDay(int n,String format){
        java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -n);
        Date date = c.getTime();
        String date2 = format2.format(date);
        return date2;
    }


    /**
     * 把Date按照format的格式转换为String
     * @param d
     * @param format 默认 yyyy-MM-dd HH:mm:ss 可自定义
     * @return
     */
    public static String formatDate(Date d,String format){
        if (d == null) {
            return null;
        }
        if(format==null||"".equals(format)){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    /**
     * 把String转换为Date
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date stringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        if (dateStr!=null&&!"".equals(dateStr)) {
            try {
                date = dd.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 是否是本月
     * @param month
     * @return
     */
    public static Boolean isEqual(String month){
        Date d = new Date();
        String data = formatDate(d, "yyyyMM");
        Boolean flag = false;
        if(data.equals(month)){
            flag = true;
        }
        return flag;
    }

    /**
     * 日期的加减法
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static Date add(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    /**
     * 获取首尾时间
     * @param currentDate
     * @param type
     * @return
     */
    public static Date[] getPeriodByType(Date currentDate, int type) {
        Date fromDate = currentDate;
        Date toDate = currentDate;
        Calendar cal;
        switch (type) {
            //获取年首和年尾，0
            case PERIOD_TYPE_YEAR:
                cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.DATE, 1);
                fromDate = cal.getTime();
                cal.add(Calendar.YEAR, 1);
                toDate = add(cal.getTime(), Calendar.DATE, -1);
                break;
            //获取月首和月尾，1
            case PERIOD_TYPE_MONTH:
                cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.set(Calendar.DATE, 1);
                fromDate = cal.getTime();
                cal.add(Calendar.MONTH, 1);
                toDate = add(cal.getTime(), Calendar.DATE, -1);
                break;
            //获取半月，2
            case PERIOD_TYPE_HALFMONTH:
                int dayOfMonth = getDayOfMonth(currentDate);
                fromDate = add(currentDate, Calendar.DATE, -1 * dayOfMonth + 1);
                if (dayOfMonth > 15) {
                    cal = Calendar.getInstance();
                    cal.setTime(fromDate);
                    cal.add(Calendar.MONTH, 1);
                    toDate = add(cal.getTime(), Calendar.DATE, -1);
                    fromDate = add(fromDate, Calendar.DATE, 15);
                } else {
                    toDate = add(fromDate, Calendar.DATE, 14);
                }
                break;
            //查找周，从周末开始，周六结束，3
            case PERIOD_TYPE_WEEK:
                int dayOfWeek = getWeekDay(currentDate);
                int seg = -1 * dayOfWeek;
                fromDate = add(currentDate, Calendar.DATE, seg);
                toDate = add(fromDate, Calendar.DATE, 6);
                break;
            default:
                break;
        }

        Date[] period = new Date[2];
        period[0] = fromDate;
        period[1] = toDate;
        return period;
    }

    /**
     * 获取日期
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 查看是周几
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static int getWeekDay(int weekNumber) {
        switch (weekNumber) {
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            case Calendar.SUNDAY:
                return 0;
            default:
                return -1;
        }
    }

    /**
     * 返回几月前的日期字符串   如201408 前两个月就是201406
     * @param d  日期
     * @param format 返回格式
     * @param month  几月前
     * @return
     */
    public static String getBeforeMonth(Date d,String format,int month){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -month);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }


    /**
     * 根据输入的日期返回该月中的最后一天的日期
     * @param d
     * @return
     */
    public static String lastDayOfMonth(String d) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMM").parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 根据输入的日期返回该月中的最后一天的日期
     * @param inputFormat 例如 yyyy-MM
     * @param outFormat   例如 yyyy-MM-dd
     * @return
     */
    public static String lastDayOfMonth(String d,String inputFormat,String outFormat) {
        Date date = null;
        try {
            date = new SimpleDateFormat(inputFormat).parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(outFormat);
        return sdf.format(time);
    }

    /**
     * 根据输入的日期返回该月中的第一天的日期
     * @param d
     * @return
     */
    public static String firstDayOfMonth(String d) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM").parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 根据输入的日期返回该月中的第一天的日期
     * @param inputFormat 例如 yyyy-MM
     * @param OutputFormat   例如 yyyy-MM-dd
     * @return
     */
    public static String firstDayOfMonth(String d, String inputFormat, String OutputFormat) {
        Date date = null;
        try {
            date = new SimpleDateFormat(inputFormat).parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(OutputFormat);
        return sdf.format(time);
    }


    public static String getDependCycle(String d, String inputFormat){
        String format = "yyyy-MM";
        if(inputFormat!=null){
            format = inputFormat;
        }
        String start = DateUtil.firstDayOfMonth(d, format, "yyyy-MM-dd");
        String end = DateUtil.lastDayOfMonth(d, format, "yyyy-MM-dd");
        return  start+"至"+end;
    }

    /**
     * 获得指定日期的下一月的第一天
     * @param d
     * @param format
     * @return
     */
    public static String nextMonthFirstDay(String d, String format){
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 获得指定日期的下一月的最后一天
     * @param d
     * @param format
     * @return
     */
    public static String nextMonthLastDay(String d, String format){
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        Date time = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 将HH:mm:ss格式的时间转化为秒
     * @param time
     * @return
     */
    public static int transform(String time) {
        String temp[] = time.split(":");
        int allSeconds=0;
        if (temp.length==3) {
            int hours = Integer.valueOf(temp[0]);
            int minutes = Integer.valueOf(temp[1]);
            int seconds = Integer.valueOf(temp[2]);
            allSeconds = hours * 60 * 60 + minutes * 60 + seconds;
        }else if (temp.length==2) {
            int minutes = Integer.valueOf(temp[0]);
            int seconds = Integer.valueOf(temp[1]);
            allSeconds =  minutes * 60 + seconds;
        }else if (temp.length==1) {
            int seconds = Integer.valueOf(temp[0]);
            allSeconds =  seconds;
        }
        //System.out.println("秒数：" + allSeconds);
        return allSeconds;
    }


    /**
     * 获取今天的日期，并格式化为所指定的日期格式
     * @param format
     * @return
     */
    public static String getToday(String format){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String result = sdf.format(date);
        return result;
    }


    /**
     * 将Tue Oct 21 12:24:26 CST 2014格式的字符串日期转换为yyyyMMdd格式的字符串日期
     * @param strDate 例：Tue Oct 21 12:24:26 CST 2014
     * @param fm 例：yyyyMMdd
     * @return
     */
    public static String strDateToStr(String strDate, String fm){
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            Date date = sdf.parse(strDate);

            SimpleDateFormat dd = new SimpleDateFormat(fm);
            result = dd.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {

//        String[] list = getAllMonths("2012-11", "2015-03");
//        for(String i : list){
//            System.out.println(i);
//        }

//        List<String> list = getMonth(6);
//        for(String i : list){
//            System.out.println(i);
//        }

//        List<String> list = getMonths(6, "yyyy-MM");
//        for(String i : list){
//            System.out.println(i);
//        }

//        List<String> list = getMonthsNotInclude(6, "yyyy-MM");
//        for(String i : list){
//            System.out.println(i);
//        }

//        try {
//            int dist = dayDist("20140604", "yyyyMMdd");
//            System.out.println(dist);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        System.out.println(getLMDay("yyyy/MM/dd"));
//        System.out.println(getLYDay("yyyy/MM/dd"));
//        System.out.println(getLMDay(3, "yyyy/MM/dd"));

//        System.out.println(formatDate(new Date(), "yyyyMMdd HHmmssss"));

//        System.out.println(stringToDate("2015-06-12", "yyyy-MM-dd"));

//        System.out.println(isEqual("201501"));

//        System.out.println(add(new Date(), Calendar.MONTH, -2));

//        Date[] getPeriodByType = getPeriodByType(stringToDate("2015-06-11", "yyyy-MM-dd"), 3);
//        for(Date i : getPeriodByType){
//            System.out.println(formatDate(i, "yyyy-MM-dd HH:mm:ss"));
//        }

//        System.out.println(getDayOfMonth(new Date()));

//        System.out.println(getWeekDay(new Date()));

//        System.out.println(getWeekDay(12));

//        System.out.println(getBeforeMonth(new Date(), "yyyy-MM-dd", 2));

//        System.out.println(lastDayOfMonth("201506", "yyyyMM", "yyyy/MM/dd"));

    }
}
