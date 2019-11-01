package com.example.im.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    private static String getSHA1(String request) {
        StringBuffer buffer = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(request.getBytes());
            byte[] bits = md.digest();
            buffer = new StringBuffer();
            for (byte bit : bits) {
                String shaHex = Integer.toHexString(bit & 0xFF);
                if (shaHex.length() < 2) {
                    buffer.append(0);
                }
                buffer.append(shaHex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static Map<String, String> getHeaders() {
        String App_Key = "vnroth0kvbimo";
        String App_Secret = "TlDOoNDdng2xqK";
        Random random = new Random();
        String Nonce = random.nextInt(10000) + "";
        String Timestamp = System.currentTimeMillis() + "";
        String Signature = getSHA1(App_Secret + Nonce + Timestamp);

        Map<String, String> headers = new HashMap<>();
        headers.put("App-Key", App_Key);
        headers.put("Nonce", Nonce);
        headers.put("Timestamp", Timestamp);
        headers.put("Signature", Signature);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    public static GetResult getInterfaceInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-cn.ronghub.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // 创建网络接口实例
        return retrofit.create(GetResult.class);
    }
}
