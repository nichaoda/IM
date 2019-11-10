package com.example.im.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.im.info.Contacts;
import com.example.im.info.User;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static android.content.Context.MODE_PRIVATE;
import static com.example.im.info.ConstValues.HAS_LOGINED;
import static com.example.im.info.ConstValues.LOGIN_INFO;
import static com.example.im.info.ConstValues.NAME;
import static com.example.im.info.ConstValues.PASSWORD;
import static com.example.im.info.ConstValues.PORTRAIT_URI;
import static com.example.im.info.ConstValues.TOKEN;
import static com.example.im.info.ConstValues.USER_ID;

public class ConnectRongIM {
    public static void connectRongIM(Context context, User user) {
        RongIM.connect(user.getToken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s) {
                // 登录成功,将信息保存到本地SharedPreferences中,下次
                // 可以在启动页中根据对应的值判断是否需要再次登录
                SharedPreferences preferences = context.getSharedPreferences(LOGIN_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(HAS_LOGINED, true);
                editor.putString(USER_ID, user.getUserId());
                editor.putString(PASSWORD, user.getPassword());
                editor.putString(NAME, user.getName());
                editor.putString(PORTRAIT_URI, user.getPortraitUri());
                editor.putString(TOKEN, user.getToken());
                editor.apply();

                // 获取好友列表和群组列表
                Contacts.getInstance().setFriendList(ContactsOperation.getFriendListFromMySQL());
                Contacts.getInstance().setGroupList(ContactsOperation.getGroupListFromMySQL());

                // 进入主界面
                Map<String, Boolean> supportedConversation = new HashMap<>();
                supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false);
                supportedConversation.put(Conversation.ConversationType.GROUP.getName(), false);
                supportedConversation.put(Conversation.ConversationType.SYSTEM.getName(), true);
                RongIM.getInstance().startConversationList(context, supportedConversation);
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
