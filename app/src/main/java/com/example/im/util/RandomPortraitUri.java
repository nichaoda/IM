package com.example.im.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.im.util.ConstValues.DEFAULT_HEAD_PORTRAIT1;
import static com.example.im.util.ConstValues.DEFAULT_HEAD_PORTRAIT2;
import static com.example.im.util.ConstValues.DEFAULT_HEAD_PORTRAIT3;
import static com.example.im.util.ConstValues.DEFAULT_HEAD_PORTRAIT4;
import static com.example.im.util.ConstValues.DEFAULT_HEAD_PORTRAIT5;
import static com.example.im.util.ConstValues.DEFAULT_HEAD_PORTRAIT6;
import static com.example.im.util.ConstValues.DEFAULT_HEAD_PORTRAIT7;

public class RandomPortraitUri {
    public static String getRandomPortraitUri() {
        List<String> portraits = new ArrayList<>();
        portraits.add(DEFAULT_HEAD_PORTRAIT1);
        portraits.add(DEFAULT_HEAD_PORTRAIT2);
        portraits.add(DEFAULT_HEAD_PORTRAIT3);
        portraits.add(DEFAULT_HEAD_PORTRAIT4);
        portraits.add(DEFAULT_HEAD_PORTRAIT5);
        portraits.add(DEFAULT_HEAD_PORTRAIT6);
        portraits.add(DEFAULT_HEAD_PORTRAIT7);
        Random random = new Random();
        int n = random.nextInt(portraits.size());
        return portraits.get(n);
    }
}
