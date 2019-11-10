package com.example.im.info;

import java.util.ArrayList;
import java.util.List;

public class Contacts {
    private static Contacts instance;
    private List<Friend> mFriendList = new ArrayList<>();
    private List<Group> mGroupList = new ArrayList<>();

    private Contacts() {
    }

    public static Contacts getInstance() {
        if (instance == null) {
            instance = new Contacts();
        }
        return instance;
    }

    public List<Friend> getFriendList() {
        return mFriendList;
    }

    public void setFriendList(List<Friend> friendList) {
        mFriendList = friendList;
    }

    public List<Group> getGroupList() {
        return mGroupList;
    }

    public void setGroupList(List<Group> groupList) {
        mGroupList = groupList;
    }

}