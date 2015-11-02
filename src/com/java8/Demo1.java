package com.java8;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author nidayu
 * @Description: ͳ�Ƶ��ʸ����� ��ʼ���� �������ǣ� ���� ѭ������
 * @date 2015/11/2
 */
public class Demo1 {
    //�������ļ��ϣ�
    private static Set NON_WORDS = new HashSet<String>(){{add("ni"); add("DA"); add("yu");}};

    public Map wordFreq(String words){
        TreeMap<String, Integer> wordMap = new TreeMap<String, Integer>();
        Matcher m = Pattern.compile("\\w+").matcher(words);
        while(m.find()){
            String word = m.group().toLowerCase();
            if (!NON_WORDS.contains(word)){
                if (wordMap.get(word) == null){
                    wordMap.put(word, 1);
                }else{
                    wordMap.put(word, wordMap.get(word) + 1);
                }
            }
        }
        return wordMap;
    }

    public static void main(String[] args){
        //��ʼ��List
        List<String> places = Arrays.asList("Buenos Aires", "Cordoba", "La Plata");
        places.stream().filter(name -> name.startsWith("C")).forEach(name -> System.out.println(name));

        //����
        List<Integer> comList = new ArrayList<>();
        comList.add(1);comList.add(0);comList.add(3);comList.add(7);comList.add(6);comList.add(5);
        Comparator<Integer> c1 = (x, y) -> x-y;
        Comparator<Integer> c2 = c1.reversed();
        Comparator<Integer> c3 = (x, y) -> y-x;
        System.out.println(
                comList.stream()
                        .min((x, y) -> x - y)
                        .get()
        );
        System.out.println("min = " + comList.stream().min(c1).get());
        System.out.println("max = " + comList.stream().max(c1).get());
        System.out.println("max = " + comList.stream().min(c2).get());
        System.out.println("max = " + comList.stream().min(c3).get());

        //��������
        new Thread(() -> System.out.println("hello, world")).start();

        //����ѭ�����
        Predicate<String> p = name -> !name.startsWith("Mr");
        List l = new ArrayList<String>();
        l.add("Mr Ni");l.add("Ms Zhang");l.add("Ms Lv");l.add("Ms Wang");
        l.stream().filter(p).forEach(name -> System.out.println(name));

        //��������
        System.out.println(
                IntStream.range(1, 12).filter(t -> 12%t == 0).sum()
        );

        //����ƴ��
        List<String> list = new ArrayList<String>();
        list.add("ni");
        list.add("da");
        list.add("yu");
        list.add("ni");
        System.out.println(
                list.parallelStream() //�����ģ���stream()��һ��
                        .filter(name -> name!=null)
                        .filter(name -> name.length()>1)
                        .map(name -> name.substring(0, 1).toUpperCase()+name.substring(1, name.length()))
                        .collect(Collectors.joining(","))
        );

        //ѭ������
        List<String> list1 = new ArrayList<String>();
        list.stream().filter(w -> !NON_WORDS.contains(w)).forEach(w -> list1.add(w));
        for (int i=0;i<list1.size();i++){
            System.out.println(list1.get(i));
        }

        //ͳ�ƣ�
        TreeMap<String, Integer> wordMap = new TreeMap<String, Integer>();
        list.stream().map(w -> w.toUpperCase())
                .filter(w -> !NON_WORDS.contains(w))
                .forEach(w -> wordMap.put(w, wordMap.getOrDefault(w, 0) + 1));
        for (String key: wordMap.keySet()){
            System.out.println(key + " ... "+wordMap.get(key));
        }
    }
}
