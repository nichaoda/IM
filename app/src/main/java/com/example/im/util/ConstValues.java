package com.example.im.util;

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
    public static final String CONTACTS_FRAGMENT_TAG = "contacts";
}
