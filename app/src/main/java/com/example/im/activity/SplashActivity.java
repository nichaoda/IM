package com.example.im.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.im.info.User;
import com.example.im.util.ConnectRongIM;

import static com.example.im.info.ConstValues.HAS_LOGINED;
import static com.example.im.info.ConstValues.LOGIN_INFO;
import static com.example.im.info.ConstValues.NAME;
import static com.example.im.info.ConstValues.PASSWORD;
import static com.example.im.info.ConstValues.PORTRAIT_URI;
import static com.example.im.info.ConstValues.TOKEN;
import static com.example.im.info.ConstValues.USER_ID;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(LOGIN_INFO, MODE_PRIVATE);
        boolean hasLogined = preferences.getBoolean(HAS_LOGINED, false);
        if (!hasLogined) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            String userId = preferences.getString(USER_ID, null);
            String password = preferences.getString(PASSWORD, null);
            String name = preferences.getString(NAME, null);
            String portraitUri = preferences.getString(PORTRAIT_URI, null);
            String token = preferences.getString(TOKEN, null);
            User user = User.getInstance();
            user.setInfo(userId, password, name, portraitUri, token);
            ConnectRongIM.connectRongIM(this, user);
        }
    }
}
