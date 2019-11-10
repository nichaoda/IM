package com.example.im.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.im.R;
import com.example.im.bean.GroupOperationBean;
import com.example.im.info.Contacts;
import com.example.im.info.Friend;
import com.example.im.info.Group;
import com.example.im.info.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.im.info.ConstValues.ADD_GROUP_INFO_IS_EMPTY;
import static com.example.im.info.ConstValues.ADD_SUCCEED;
import static com.example.im.info.ConstValues.CAN_NOT_ADD_MYSELF;
import static com.example.im.info.ConstValues.CREATE_GROUP_INFO_NOT_COMPLETE;
import static com.example.im.info.ConstValues.EDITTEXT_IS_EMPTY;
import static com.example.im.info.ConstValues.FRIEND_HAS_ADDED;
import static com.example.im.info.ConstValues.GROUP_ADD_FAILED;
import static com.example.im.info.ConstValues.GROUP_ADD_SUCCEED;
import static com.example.im.info.ConstValues.GROUP_CREATE_FAILED;
import static com.example.im.info.ConstValues.GROUP_CREATE_SUCCEED;
import static com.example.im.info.ConstValues.GROUP_HAS_CREATED;
import static com.example.im.info.ConstValues.GROUP_NOT_EXIST;
import static com.example.im.info.ConstValues.USER_IN_THIS_GROUP;
import static com.example.im.info.ConstValues.USER_NOT_IN_THE_TABLE;

public class ContactsOperation {
    public static void addFriend(Context context, Handler handler) {
        Message message = Message.obtain();
        View dialogSearch = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);
        Dialog alertDialog = new AlertDialog.Builder(context)
                .setView(dialogSearch)
                .setPositiveButton("添加好友", (dialog, which) -> {
                    EditText editTextSearch = dialogSearch.findViewById(R.id.dialog_edittext_search);
                    String friendId = editTextSearch.getText().toString();
                    if (TextUtils.isEmpty(friendId)) {
                        message.what = EDITTEXT_IS_EMPTY;
                        handler.sendMessage(message);
                        return;
                    }
                    // 首先判断是否是自己
                    String userId = User.getInstance().getUserId();
                    if (friendId.equals(userId)) {
                        message.what = CAN_NOT_ADD_MYSELF;
                        handler.sendMessage(message);
                    } else {
                        // 搜索是否已经注册
                        new Thread(() -> {
                            String sql = "select * from user where userId = ?";
                            MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
                            dbHelper.setData(new String[]{friendId});
                            ResultSet resultSet = dbHelper.executeQuery();
                            MySqlDBHelper dbHelper2 = null;
                            MySqlDBHelper dbHelper3 = null;
                            try {
                                // 不存在
                                if (!resultSet.next()) {
                                    message.what = USER_NOT_IN_THE_TABLE;
                                    handler.sendMessage(message);
                                } else {
                                    // 获取好友信息
                                    String name = resultSet.getString("name");
                                    String portraitUri = resultSet.getString("portraitUri");
                                    // 是否已经是好友
                                    String sql2 = "select * from friend where userId = ? and friendId = ?";
                                    dbHelper2 = new MySqlDBHelper(sql2);
                                    dbHelper2.setData(new String[]{userId, friendId});
                                    ResultSet resultSet2 = dbHelper2.executeQuery();
                                    if (resultSet2.next()) {
                                        // 已经是好友了
                                        message.what = FRIEND_HAS_ADDED;
                                        handler.sendMessage(message);
                                    } else {
                                        // 添加好友
                                        String sql3 = "insert into friend(userId,friendId) values(?,?),(?,?)";
                                        dbHelper3 = new MySqlDBHelper(sql3);
                                        dbHelper3.setData(new String[]{userId, friendId, friendId, userId});
                                        int result = dbHelper3.executeSQL();
                                        if (result == 2) {
                                            // 成功
                                            message.what = ADD_SUCCEED;
                                            message.obj = new Friend(friendId, name, portraitUri);
                                            handler.sendMessage(message);
                                            // 发起聊天
//                                            RongIM.getInstance().startPrivateChat(context, friendId, name);
                                        }
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } finally {
                                dbHelper.closeConnection();
                                if (dbHelper2 != null) {
                                    dbHelper2.closeConnection();
                                }
                                if (dbHelper3 != null) {
                                    dbHelper3.closeConnection();
                                }
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        alertDialog.show();
    }

    public static void createGroup(Context context, Handler handler) {
        Message message = Message.obtain();
        View dialogCreate = LayoutInflater.from(context).inflate(R.layout.dialog_create_group, null);
        Dialog alertDialog = new AlertDialog.Builder(context)
                .setView(dialogCreate)
                .setPositiveButton("创建群组", (dialog, which) -> {
                    EditText editTextGroupId = dialogCreate.findViewById(R.id.dialog_group_id);
                    EditText editTextGroupName = dialogCreate.findViewById(R.id.dialog_group_name);
                    String groupId = editTextGroupId.getText().toString();
                    String groupName = editTextGroupName.getText().toString();
                    if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(groupName)) {
                        message.what = CREATE_GROUP_INFO_NOT_COMPLETE;
                        handler.sendMessage(message);
                        return;
                    }
                    // 查询群组是否已经创建
                    new Thread(() -> {
                        String sql = "select * from chat_group where groupId = ?";
                        MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
                        dbHelper.setData(new String[]{groupId});
                        ResultSet resultSet = dbHelper.executeQuery();
                        MySqlDBHelper dbHelper2 = null;
                        MySqlDBHelper dbHelper3 = null;
                        MySqlDBHelper dbHelper4 = null;
                        try {
                            if (resultSet.next()) {
                                // 已创建
                                message.what = GROUP_HAS_CREATED;
                                handler.sendMessage(message);
                            } else {
                                // 调用融云api创建Group
                                Map<String, String> bodies = new HashMap<>();
                                bodies.put("userId", User.getInstance().getUserId());
                                bodies.put("groupId", groupId);
                                bodies.put("groupName", groupName);
                                Call<GroupOperationBean> call = HttpUtil.getInterfaceInstance()
                                        .getGroupCreateResult(HttpUtil.getHeaders(), bodies);
                                try {
                                    Response<GroupOperationBean> response = call.execute();
                                    GroupOperationBean groupCreateBean = response.body();
                                    if (groupCreateBean.getCode() == 200) {
                                        // 创建成功,插入到数据库两张表中
                                        String sql2 = "insert into chat_group(groupId,groupName,userId) values(?,?,?)";
                                        dbHelper2 = new MySqlDBHelper(sql2);
                                        dbHelper2.setData(new String[]{groupId, groupName, User.getInstance().getUserId()});
                                        if (dbHelper2.executeSQL() == 1) {
                                            // 插入到chat_group表成功
                                            String sql3 = "insert into group_member(groupId,userId) values(?,?)";
                                            dbHelper3 = new MySqlDBHelper(sql3);
                                            dbHelper3.setData(new String[]{groupId, User.getInstance().getUserId()});
                                            if (dbHelper3.executeSQL() == 1) {
                                                // 插入到group_member表成功
                                                message.what = GROUP_CREATE_SUCCEED;
                                                message.obj = new Group(groupId, groupName);
                                                handler.sendMessage(message);
                                                // 群聊开启 startGroupChat();
                                                return;
                                            }
                                            // 插入到group_member表失败,删除chat_group表的对应信息
                                            String sql4 = "delete from chat_group where groupId = ?";
                                            dbHelper4 = new MySqlDBHelper(sql4);
                                            dbHelper4.setData(new String[]{groupId});
                                            for (; ; ) {
                                                if (dbHelper4.executeSQL() == 1) {
                                                    // 从chat_group表删除对应内容成功
                                                    message.what = GROUP_CREATE_FAILED;
                                                    handler.sendMessage(message);
                                                    return;
                                                }
                                            }
                                        }
                                        message.what = GROUP_CREATE_FAILED;
                                        handler.sendMessage(message);
                                    } else {
                                        // 创建失败
                                        message.what = GROUP_CREATE_FAILED;
                                        handler.sendMessage(message);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            dbHelper.closeConnection();
                            if (dbHelper2 != null) {
                                dbHelper2.closeConnection();
                            }
                            if (dbHelper3 != null) {
                                dbHelper3.closeConnection();
                            }
                            if (dbHelper4 != null) {
                                dbHelper4.closeConnection();
                            }
                        }
                    }).start();
                })
                .setNegativeButton("取消", null)
                .create();
        alertDialog.show();
    }

    public static void joinGroup(Context context, Handler handler) {
        Message message = Message.obtain();
        View dialogSearch = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);
        Dialog alertDialog = new AlertDialog.Builder(context)
                .setView(dialogSearch)
                .setPositiveButton("加入群组", (dialog, which) -> {
                    EditText editTextSearch = dialogSearch.findViewById(R.id.dialog_edittext_search);
                    String groupId = editTextSearch.getText().toString();
                    if (TextUtils.isEmpty(groupId)) {
                        message.what = ADD_GROUP_INFO_IS_EMPTY;
                        handler.sendMessage(message);
                        return;
                    }
                    new Thread(() -> {
                        String sql = "select * from chat_group where groupId = ?";
                        MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
                        dbHelper.setData(new String[]{groupId});
                        ResultSet resultSet = dbHelper.executeQuery();
                        MySqlDBHelper dbHelper2 = null;
                        MySqlDBHelper dbHelper3 = null;
                        try {
                            if (!resultSet.next()) {
                                message.what = GROUP_NOT_EXIST;
                                handler.sendMessage(message);
                                return;
                            }
                            // 群存在,获取群名
                            String groupName = resultSet.getString("groupName");
                            // 是否已经加入
                            String sql2 = "select * from group_member where groupId = ? and userId = ?";
                            dbHelper2 = new MySqlDBHelper(sql2);
                            dbHelper2.setData(new String[]{groupId, User.getInstance().getUserId()});
                            ResultSet resultSet2 = dbHelper2.executeQuery();
                            if (resultSet2.next()) {
                                message.what = USER_IN_THIS_GROUP;
                                handler.sendMessage(message);
                                return;
                            }
                            // 该群组存在,并且user还没加入
                            // 调用融云api
                            Map<String, String> bodies = new HashMap<>();
                            bodies.put("userId", User.getInstance().getUserId());
                            bodies.put("groupId", groupId);
                            bodies.put("groupName", groupName);
                            Call<GroupOperationBean> call = HttpUtil.getInterfaceInstance()
                                    .getGroupJoinResult(HttpUtil.getHeaders(), bodies);
                            try {
                                Response<GroupOperationBean> response = call.execute();
                                GroupOperationBean groupJoinBean = response.body();
                                if (groupJoinBean.getCode() == 200) {
                                    // 插入到group_member表中
                                    String sql3 = "insert into group_member(groupId,userId) values(?,?)";
                                    dbHelper3 = new MySqlDBHelper(sql3);
                                    dbHelper3.setData(new String[]{groupId, User.getInstance().getUserId()});
                                    if (dbHelper3.executeSQL() == 1) {
                                        // 群组加入成功
                                        message.what = GROUP_ADD_SUCCEED;
                                        message.obj = new Group(groupId, groupName);
                                        handler.sendMessage(message);
                                        // 发起聊天 startGroupChat
                                        return;
                                    }
                                    message.what = GROUP_ADD_FAILED;
                                    handler.sendMessage(message);
                                    return;
                                }
                                message.what = GROUP_ADD_FAILED;
                                handler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            dbHelper.closeConnection();
                            if (dbHelper2 != null) {
                                dbHelper2.closeConnection();
                            }
                            if (dbHelper3 != null) {
                                dbHelper3.closeConnection();
                            }
                        }

                    }).start();
                })
                .setNegativeButton("取消", null)
                .create();
        alertDialog.show();
    }

    public static List<Friend> getFriendListFromMySQL() {
        List<Friend> friendList = new ArrayList<>();
        new Thread(() -> {
            String sql = "select userId,name,portraitUri from user where userId in " +
                    "(select friendId from friend where userId = ?)";
            MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
            dbHelper.setData(new String[]{User.getInstance().getUserId()});
            ResultSet resultSet = dbHelper.executeQuery();
            try {
                while (resultSet.next()) {
                    String userId = resultSet.getString("userId");
                    String name = resultSet.getString("name");
                    String portraitUri = resultSet.getString("portraitUri");
                    friendList.add(new Friend(userId, name, portraitUri));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbHelper.closeConnection();
            }
        }).start();
        return friendList;
    }

    public static List<Group> getGroupListFromMySQL() {
        List<Group> groupList = new ArrayList<>();
        new Thread(() -> {
            String sql = "select groupId,groupName from chat_group where groupId " +
                    "in (select groupId from group_member where userId = ?)";
            MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
            dbHelper.setData(new String[]{User.getInstance().getUserId()});
            ResultSet resultSet = dbHelper.executeQuery();
            try {
                while (resultSet.next()) {
                    String groupId = resultSet.getString("groupId");
                    String groupName = resultSet.getString("groupName");
                    groupList.add(new Group(groupId, groupName));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                dbHelper.closeConnection();
            }
        }).start();
        return groupList;
    }
}
