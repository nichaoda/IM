package com.example.im.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPortraitUri {
    public static String getRandomPortraitUri() {
        List<String> portraits = new ArrayList<>();
        portraits.add("http://imgsrc.baidu.com/forum/pic/item/13d21630e924b89967cc4d1964061d950b7bf60d.jpg");
        portraits.add("http://imgsrc.baidu.com/forum/pic/item/9b0601fa513d2697bd553c7c5ffbb2fb4216d873.jpg");
        portraits.add("http://imgsrc.baidu.com/forum/pic/item/eae3bade9c82d15896556d908a0a19d8bd3e4221.jpg");
        portraits.add("http://imgsrc.baidu.com/forum/pic/item/c9f5b7096b63f62492b30d228d44ebf81b4ca30b.jpg");
        portraits.add("http://imgsrc.baidu.com/forum/pic/item/481a8b025aafa40fe815fafca164034f79f0192d.jpg");
        portraits.add("http://imgsrc.baidu.com/forum/pic/item/7011f2edab64034fdeb3f527a5c379310b551dfa.jpg");
        portraits.add("http://imgsrc.baidu.com/forum/pic/item/88349582d158ccbf3759811b13d8bc3eb0354117.jpg");
        Random random = new Random();
        int n = random.nextInt(portraits.size());
        return portraits.get(n);
    }
}
