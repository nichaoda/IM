package com.example.im.info;

/**
 * Description:存储所有的KEY以及一些常量
 */
public class ConstValues {
    /**
     * 注册相关
     */
    public static final int HAS_REGISTERED = 0;
    public static final int REGISTER_FAILED = 1;
    public static final int REGISTER_SUCCEED = 2;
    /**
     * 登录相关
     */
    public static final int HAS_NOT_REGISTERED = 0;
    public static final int PASSWORD_IS_WRONG = 1;
    public static final String LOGIN_INFO = "login_information";
    public static final String HAS_LOGINED = "has_logined";
    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String PORTRAIT_URI = "portraitUri";
    public static final String TOKEN = "token";
    /**
     * 默认头像
     */
    public static final String DEFAULT_HEAD_PORTRAIT1 = "http://imgsrc.baidu.com/forum/pic/item/13d21630e924b89967cc4d1964061d950b7bf60d.jpg";
    public static final String DEFAULT_HEAD_PORTRAIT2 = "http://imgsrc.baidu.com/forum/pic/item/9b0601fa513d2697bd553c7c5ffbb2fb4216d873.jpg";
    public static final String DEFAULT_HEAD_PORTRAIT3 = "http://imgsrc.baidu.com/forum/pic/item/eae3bade9c82d15896556d908a0a19d8bd3e4221.jpg";
    public static final String DEFAULT_HEAD_PORTRAIT4 = "http://imgsrc.baidu.com/forum/pic/item/c9f5b7096b63f62492b30d228d44ebf81b4ca30b.jpg";
    public static final String DEFAULT_HEAD_PORTRAIT5 = "http://imgsrc.baidu.com/forum/pic/item/481a8b025aafa40fe815fafca164034f79f0192d.jpg";
    public static final String DEFAULT_HEAD_PORTRAIT6 = "http://imgsrc.baidu.com/forum/pic/item/7011f2edab64034fdeb3f527a5c379310b551dfa.jpg";
    public static final String DEFAULT_HEAD_PORTRAIT7 = "http://imgsrc.baidu.com/forum/pic/item/88349582d158ccbf3759811b13d8bc3eb0354117.jpg";
    /**
     * MySql数据库相关
     */
    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String DATABASE_URL = "jdbc:mysql://192.168.0.6:3306/";
    public static final String DATABASE_NAME = "instant_messaging";
    public static final String DATABASE_USER = "root";
    public static final String DATABASE_PASSWORD = "root";
    /**
     * 主界面
     */
    public static final String CONVERSATION_LIST_FRAGMENT_TAG = "messages";
    public static final String FRIEND_FRAGMENT_TAG = "friends";
    public static final String GROUP_FRAGMENT_TAG = "groups";
    /**
     * 好友相关
     */
    public static final int EDITTEXT_IS_EMPTY = 0;
    public static final int CAN_NOT_ADD_MYSELF = 1;
    public static final int USER_NOT_IN_THE_TABLE = 2;
    public static final int FRIEND_HAS_ADDED = 3;
    public static final int SEND_ADD_FRIEND_REQUEST_SUCCEED = 4;
    public static final int FRIEND_ADD_SUCCEED = 5;
    /**
     * 群组相关
     */
    public static final int CREATE_GROUP_INFO_NOT_COMPLETE = 6;
    public static final int GROUP_HAS_CREATED = 7;
    public static final int GROUP_CREATE_FAILED = 8;
    public static final int GROUP_CREATE_SUCCEED = 9;
    public static final int ADD_GROUP_INFO_IS_EMPTY = 10;
    public static final int GROUP_NOT_EXIST = 11;
    public static final int USER_IN_THIS_GROUP = 12;
    public static final int GROUP_ADD_FAILED = 13;
    public static final int GROUP_ADD_SUCCEED = 14;
}
