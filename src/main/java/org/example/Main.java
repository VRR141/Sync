package org.example;

import java.util.*;

public class Main {

    private static String letters = "RLRFR";
    private static int length = 100;
    private static int threadCount = 1000;
    private static final Map<Integer, Integer> sizeToFreq = new TreeMap<>();

    public static void main(String[] args) throws InterruptedException {

        Runnable logic =() -> {
            String temp = generateRoute(letters, length);
            int result = (int) temp.chars().filter(ch -> ch == 'R').count();
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(result)){
                    var value = sizeToFreq.get(result);
                    value++;
                    sizeToFreq.put((int) result, value);
                } else {
                    sizeToFreq.put((int) result, 1);
                }
            }
        };

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++){
            threads.add(new Thread(logic));
        }

        for (Thread thread: threads){
            thread.start();
        }

        for (Thread thread: threads){
            thread.join();
        }

        System.out.println(printMap(sizeToFreq));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static String printMap(Map<Integer, Integer> map){
        StringBuilder stringBuilder = new StringBuilder();
        int max = 0;
        int maxKey = 0;
        for (Integer i : map.keySet()){
            if (map.get(i) > max) {
                max = map.get(i);
                maxKey = i;
            }
        }
        stringBuilder.append("key: ").append(maxKey).append(" max: ").append(max).append("\nAny: ");
        for (Integer i : map.keySet()){
            if (i == maxKey){
                continue;
            }
            stringBuilder.append("\n").append("- ").append(i).append("(").append(map.get(i)).append(")");
        }
        return stringBuilder.toString();
    }
}