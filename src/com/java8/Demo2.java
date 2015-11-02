package com.java8;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;

/**
 * @author nidayu
 * @Description:
 * @date 2015/11/2
 */
public class Demo2 {
    public static void main(String[] args){
        List<String> collected = Stream.of("a", "b", "c").collect(Collectors.toList());
        //����
        assertEquals(Arrays.asList("a", "b", "c"), collected);

        List<String> collected2 = Stream.of("a", "b", "hello")
                .map(string -> string.substring(0, 1).toUpperCase() + string.substring(1, string.length()))
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("A", "B", "Hello"), collected2);

        List<String> beginWithNumbers = Stream.of("a", "1abc", "abc1")
                .filter(value -> Character.isDigit(value.charAt(0)))
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("1abc"), beginWithNumbers);

        List<Integer> together = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4))
                .flatMap(numbers -> numbers.stream())
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2, 3, 4), together);

        //����������ԣ������С��һ���࣡
        List<Track> tracks = Arrays.asList(
                new Track("ni", 17),
                new Track("da", 20),
                new Track("yu", 25),
                new Track("zhangyu", 22),
                new Track("da", 22)
        );
        Track minLengthTrack = tracks.stream()
                .min(Comparator.comparing(track -> track.getLength()))
                .get();
        System.out.println(minLengthTrack.getLength() +" "+ minLengthTrack.getNeme());
        assertEquals(tracks.get(0), minLengthTrack);

        //�������
        tracks.stream()
                .sorted(Comparator.comparing(track -> track.getLength()))
                .forEach(track1 -> System.out.println(track1.getNeme()));

        //����ת��
        IntSummaryStatistics intSummaryStatistics = tracks.stream()
                .mapToInt(track -> track.getLength()) //ת����IntStream����
                .summaryStatistics();
        System.out.println(String.format("min: %d \nmax: %d \nave: %f \nsum: %d",
                intSummaryStatistics.getMin(),
                intSummaryStatistics.getMax(),
                intSummaryStatistics.getAverage(),
                intSummaryStatistics.getSum()));

        //���
        BinaryOperator<Integer> accumulator = (x, y) -> x + y;
        int count = Stream.of(1, 2, 3)
                .reduce(0, accumulator);
        assertEquals(6, count);

        //���Է���
        Demo2 java8 = new Demo2();
        //������������
        Map<String, List<Track>> map = java8.groupByTrackName(tracks.stream());
        for (Map.Entry<String, List<Track>> entry: map.entrySet()){
            System.out.println(entry.getKey() +" "+ entry.getValue().size());
        }
        //���Է���
        java8.test(() -> "test info ...");

        System.out.printf("hello %s", "world!");
    }

    public void test(Supplier<String> message){
        if (true){
            System.out.println(message.get());
        }
    }

    public Map<String, List<Track>> groupByTrackName(Stream<Track> trackStream){
        return trackStream.collect(Collectors.groupingBy(track -> track.getNeme()));
    }
}

class Track{
    String neme;
    int length;

    public Track(String neme, int length) {
        this.neme = neme;
        this.length = length;
    }

    public String getNeme() {
        return neme;
    }

    public void setNeme(String neme) {
        this.neme = neme;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}