package com.example.im.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.im.info.ConstValues.DEFAULT_HEAD_PORTRAIT1;
import static com.example.im.info.ConstValues.DEFAULT_HEAD_PORTRAIT2;
import static com.example.im.info.ConstValues.DEFAULT_HEAD_PORTRAIT3;
import static com.example.im.info.ConstValues.DEFAULT_HEAD_PORTRAIT4;
import static com.example.im.info.ConstValues.DEFAULT_HEAD_PORTRAIT5;
import static com.example.im.info.ConstValues.DEFAULT_HEAD_PORTRAIT6;
import static com.example.im.info.ConstValues.DEFAULT_HEAD_PORTRAIT7;

public class PortraitUri {
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

    /**
     * 得到资源文件中图片的Uri
     *
     * @param context 上下文对象
     * @param id      资源id
     * @return Uri
     */
    public static Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }
}
