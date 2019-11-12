package com.example.im.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.im.bean.CodeBean;
import com.example.im.bean.ContactNtfBean;
import com.example.im.info.Friend;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.im.info.ConstValues.FRIEND_ADD_SUCCEED;

public class SystemMessageOperation {
    public static void getSystemMessage(Context context, Handler handler) {
        RongIM.setOnReceiveMessageListener(((message, i) -> {
            if (message.getConversationType() == Conversation.ConversationType.SYSTEM) {
                MessageContent messageContent = message.getContent();
                String json = new String(messageContent.encode());
                Gson gson = new Gson();
                ContactNtfBean contactNtfBean = gson.fromJson(json, ContactNtfBean.class);
                String operation = contactNtfBean.getOperation();
                String friendId = contactNtfBean.getSourceUserId();
                String userId = contactNtfBean.getTargetUserId();
                if ("Request".equals(operation)) {
                    Dialog alertDialog = new AlertDialog.Builder(context)
                            .setMessage(contactNtfBean.getMessage() + ",是否添加")
                            .setPositiveButton("是", (dialog, which) ->
                                    new Thread(() -> {
                                        // 插入数据库
                                        String sql = "insert into friend(userId,friendId) values(?,?),(?,?)";
                                        MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
                                        dbHelper.setData(new String[]{friendId, userId, userId, friendId});
                                        for (; ; ) {
                                            if (dbHelper.executeSQL() == 2) {
                                                dbHelper.closeConnection();
                                                String sql2 = "select name,portraitUri from user where userId = ?";
                                                MySqlDBHelper dbHelper2 = new MySqlDBHelper(sql2);
                                                dbHelper2.setData(new String[]{friendId});
                                                ResultSet resultSet = dbHelper2.executeQuery();
                                                try {
                                                    if (resultSet.next()) {
                                                        // 获取对方的昵称和头像
                                                        String friendName = resultSet.getString("name");
                                                        String friendPortraitUri = resultSet.getString("portraitUri");
                                                        Message msg = Message.obtain();
                                                        msg.what = FRIEND_ADD_SUCCEED;
                                                        msg.obj = new Friend(friendId, friendName, friendPortraitUri);
                                                        handler.sendMessage(msg);
                                                        // 通知对方好友已经添加成功
                                                        sendSystemMessage(userId, friendId, "AcceptResponse", "", 0,
                                                                null);
                                                    }
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    dbHelper2.closeConnection();
                                                }
                                                break;
                                            }
                                        }
                                    }).start())
                            .setNegativeButton("否", (dialog, which) -> new Thread(() ->
                                    sendSystemMessage(userId, friendId, "RejectResponse", "",
                                            0, null)).start())
                            .setCancelable(false)
                            .create();
                    alertDialog.show();
                } else if ("RejectResponse".equals(operation)) {
                    Toast.makeText(context, "对方拒绝了好友添加", Toast.LENGTH_SHORT).show();
                } else if ("AcceptResponse".equals(operation)) {
                    new Thread(() -> {
                        String sql = "select name,portraitUri from user where userId = ?";
                        MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
                        dbHelper.setData(new String[]{friendId});
                        ResultSet resultSet = dbHelper.executeQuery();
                        try {
                            if (resultSet.next()) {
                                String friendName = resultSet.getString("name");
                                String friendPortraitUri = resultSet.getString("portraitUri");
                                Message msg = Message.obtain();
                                msg.what = FRIEND_ADD_SUCCEED;
                                msg.obj = new Friend(friendId, friendName, friendPortraitUri);
                                handler.sendMessage(msg);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            dbHelper.closeConnection();
                        }
                    }).start();
                }
                return true;
            }
            return false;
        }));
    }

    /**
     * @param from      发出通知的用户 Id
     * @param to        接收通知的id
     * @param operation 三种选择:"Request", "AcceptResponse", "RejectResponse"
     * @param message   表示请求或者响应消息，如添加理由或拒绝理由
     * @param msgCode   android.os.Message对象的对应code
     * @param handler   Handler对象
     */
    public static void sendSystemMessage(String from, String to, String operation, String message, int msgCode, Handler handler) {
        Map<String, String> bodies = new HashMap<>();
        bodies.put("fromUserId", from);
        bodies.put("toUserId", to);
        bodies.put("objectName", "RC:ContactNtf");
        Gson gson = new Gson();
        ContactNtfBean contactNtfBean = new ContactNtfBean(operation, from, to, message);
        String json = gson.toJson(contactNtfBean);
        bodies.put("content", json);
        Call<CodeBean> call = HttpUtil.getInterfaceInstance()
                .getSystemMessageSendResult(HttpUtil.getHeaders(), bodies);
        try {
            for (; ; ) {
                Response<CodeBean> response = call.execute();
                CodeBean codeBean = response.body();
                if (codeBean.getCode() == 200) {
                    if (handler != null) {
                        Message msg = Message.obtain();
                        msg.what = msgCode;
                        handler.sendMessage(msg);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
