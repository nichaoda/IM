package com.example.im.info;

public class User {
    private static User instance;
    private String userId;
    private String password;
    private String name;
    private String portraitUri;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setInfo(String userId, String password, String name,
                        String portraitUri, String token) {
        setUserId(userId);
        setPassword(password);
        setName(name);
        setPortraitUri(portraitUri);
        setToken(token);
    }

    private User() {
    }

    public static synchronized User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

}