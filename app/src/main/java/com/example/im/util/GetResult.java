package com.example.im.util;

import com.example.im.bean.UserTokenBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface GetResult {
    /**
     * @param headers 请求头
     * @param bodies  请求体
     * @return 注册成功, 获取token值
     */
    @FormUrlEncoded
    @POST("user/getToken.json")
    Call<UserTokenBean> getUserToken(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> bodies);
}
