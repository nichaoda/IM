package com.example.im.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.im.R;
import com.example.im.fragment.FriendFragment;
import com.example.im.fragment.GroupFragment;
import com.example.im.info.Contacts;
import com.example.im.info.Friend;
import com.example.im.info.Group;
import com.example.im.info.User;
import com.example.im.util.ContactsOperation;
import com.example.im.util.PortraitUri;
import com.example.im.util.SystemMessageOperation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import static com.example.im.info.ConstValues.ADD_GROUP_INFO_IS_EMPTY;
import static com.example.im.info.ConstValues.CAN_NOT_ADD_MYSELF;
import static com.example.im.info.ConstValues.CREATE_GROUP_INFO_NOT_COMPLETE;
import static com.example.im.info.ConstValues.EDITTEXT_IS_EMPTY;
import static com.example.im.info.ConstValues.FRIEND_ADD_SUCCEED;
import static com.example.im.info.ConstValues.FRIEND_FRAGMENT_TAG;
import static com.example.im.info.ConstValues.CONVERSATION_LIST_FRAGMENT_TAG;
import static com.example.im.info.ConstValues.FRIEND_HAS_ADDED;
import static com.example.im.info.ConstValues.GROUP_ADD_FAILED;
import static com.example.im.info.ConstValues.GROUP_ADD_SUCCEED;
import static com.example.im.info.ConstValues.GROUP_CREATE_FAILED;
import static com.example.im.info.ConstValues.GROUP_CREATE_SUCCEED;
import static com.example.im.info.ConstValues.GROUP_FRAGMENT_TAG;
import static com.example.im.info.ConstValues.GROUP_HAS_CREATED;
import static com.example.im.info.ConstValues.GROUP_NOT_EXIST;
import static com.example.im.info.ConstValues.LOGIN_INFO;
import static com.example.im.info.ConstValues.SEND_ADD_FRIEND_REQUEST_SUCCEED;
import static com.example.im.info.ConstValues.USER_IN_THIS_GROUP;
import static com.example.im.info.ConstValues.USER_NOT_IN_THE_TABLE;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private NavigationView mNavigationView;
    private Fragment mConversationListFragment;
    private Fragment mFriendFragment;
    private Fragment mGroupFragment;
    /**
     * 当前的Fragment
     */
    private Fragment mNowFragment;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EDITTEXT_IS_EMPTY:
                    Toast.makeText(MainActivity.this, "id为空,无法搜索", Toast.LENGTH_SHORT).show();
                    break;
                case CAN_NOT_ADD_MYSELF:
                    Toast.makeText(MainActivity.this, "不能添加自己", Toast.LENGTH_SHORT).show();
                    break;
                case USER_NOT_IN_THE_TABLE:
                    Toast.makeText(MainActivity.this, "添加用户不存在", Toast.LENGTH_SHORT).show();
                    break;
                case FRIEND_HAS_ADDED:
                    Toast.makeText(MainActivity.this, "你已添加该用户", Toast.LENGTH_SHORT).show();
                    break;
                case SEND_ADD_FRIEND_REQUEST_SUCCEED:
                    Toast.makeText(MainActivity.this, "发送了添加好友的请求", Toast.LENGTH_SHORT).show();
                    break;
                case FRIEND_ADD_SUCCEED:
                    Friend friend = (Friend) msg.obj;
                    ((FriendFragment) getSupportFragmentManager().findFragmentByTag(FRIEND_FRAGMENT_TAG))
                            .getContactsAdapter().addFriend(friend);
                    // 刷新用户信息
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(friend.getUserId(), friend.getName(),
                            Uri.parse(friend.getPortraitUri())));
                    Toast.makeText(MainActivity.this, "好友添加成功", Toast.LENGTH_SHORT).show();
                    break;
                case CREATE_GROUP_INFO_NOT_COMPLETE:
                    Toast.makeText(MainActivity.this, "信息不完整,无法创建群组", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_HAS_CREATED:
                    Toast.makeText(MainActivity.this, "该群组id已存在,创建失败", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_CREATE_FAILED:
                    Toast.makeText(MainActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_CREATE_SUCCEED: {
                    Group group = (Group) msg.obj;
                    ((GroupFragment) getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG))
                            .getContactsAdapter().addGroup(group);
                    // 刷新群组信息
                    RongIM.getInstance().refreshGroupInfoCache(new io.rong.imlib.model.Group(group.getGroupId(),
                            group.getName(), PortraitUri.getUriFromDrawableRes(MainActivity.this, R.drawable.group)));
                    Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                }
                break;
                case ADD_GROUP_INFO_IS_EMPTY:
                    Toast.makeText(MainActivity.this, "群组id为空,无法搜索", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_NOT_EXIST:
                    Toast.makeText(MainActivity.this, "群组id不存在", Toast.LENGTH_SHORT).show();
                    break;
                case USER_IN_THIS_GROUP:
                    Toast.makeText(MainActivity.this, "你已经在该群中了", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_ADD_FAILED:
                    Toast.makeText(MainActivity.this, "加入失败", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_ADD_SUCCEED:
                    Group group = (Group) msg.obj;
                    ((GroupFragment) getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG))
                            .getContactsAdapter().addGroup(group);
                    RongIM.getInstance().refreshGroupInfoCache(new io.rong.imlib.model.Group(group.getGroupId(),
                            group.getName(), PortraitUri.getUriFromDrawableRes(MainActivity.this, R.drawable.group)));
                    Toast.makeText(MainActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState != null) {
            mConversationListFragment = getSupportFragmentManager().findFragmentByTag(CONVERSATION_LIST_FRAGMENT_TAG);
            mFriendFragment = getSupportFragmentManager().findFragmentByTag(FRIEND_FRAGMENT_TAG);
            mGroupFragment = getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG);
            getSupportFragmentManager().beginTransaction()
                    .hide(mConversationListFragment)
                    .hide(mFriendFragment)
                    .hide(mGroupFragment)
                    .show(mNowFragment)
                    .commit();
        } else {
            mConversationListFragment = getConversationListFragment();
            mFriendFragment = new FriendFragment();
            mGroupFragment = new GroupFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mConversationListFragment, CONVERSATION_LIST_FRAGMENT_TAG)
                    .add(R.id.fragment_container, mFriendFragment, FRIEND_FRAGMENT_TAG)
                    .add(R.id.fragment_container, mGroupFragment, GROUP_FRAGMENT_TAG)
                    .hide(mFriendFragment)
                    .hide(mGroupFragment)
                    .commit();
            mNowFragment = mConversationListFragment;
        }
        // 设置用户信息提供者
        RongIM.setUserInfoProvider((s) -> {
            List<Friend> friendList = Contacts.getInstance().getFriendList();
            for (Friend friend : friendList) {
                if (friend.getUserId().equals(s)) {
                    return new UserInfo(s, friend.getName(), Uri.parse(friend.getPortraitUri()));
                }
            }
            return null;
        }, true);
        // 设置群组信息提供者
        RongIM.setGroupInfoProvider((s) -> {
            List<Group> groupList = Contacts.getInstance().getGroupList();
            for (Group group : groupList) {
                if (group.getGroupId().equals(s)) {
                    return new io.rong.imlib.model.Group(s, group.getName(),
                            PortraitUri.getUriFromDrawableRes(this, R.drawable.group));
                }
            }
            return null;
        }, true);
        // 刷新自己的信息
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(User.getInstance().getUserId(),
                User.getInstance().getName(), Uri.parse(User.getInstance().getPortraitUri())));
        setListener();
    }

    private void initView() {
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mNavigationView = findViewById(R.id.navigation);
        View view = mNavigationView.getHeaderView(0);
        Glide.with(this).load(User.getInstance().getPortraitUri())
                .into((ImageView) view.findViewById(R.id.user_portrait_image));
        TextView userId, userName;
        userId = view.findViewById(R.id.user_id);
        userName = view.findViewById(R.id.user_name);
        userId.setText(userId.getText() + ": " + User.getInstance().getUserId());
        userName.setText(userName.getText() + ": " + User.getInstance().getName());
    }

    private void setListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.messages:
                    switchFragment(mNowFragment, getSupportFragmentManager().findFragmentByTag(CONVERSATION_LIST_FRAGMENT_TAG));
                    return true;
                case R.id.friends:
                    switchFragment(mNowFragment, getSupportFragmentManager().findFragmentByTag(FRIEND_FRAGMENT_TAG));
                    return true;
                case R.id.groups:
                    switchFragment(mNowFragment, getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG));
                    return true;
                default:
            }
            return false;
        });
        mNavigationView.setNavigationItemSelectedListener((menuItem) -> {
                    switch (menuItem.getItemId()) {
                        case R.id.about:
                            Toast.makeText(this, "这是一个即时通讯App", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.logout:
                            // 登出
                            RongIM.getInstance().logout();
                            // 清空登录信息
                            SharedPreferences.Editor editor = getSharedPreferences(LOGIN_INFO, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            // 跳转到登录界面
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                            return true;
                        default:
                    }
                    return false;
                }
        );
        // 监听好友相关的系统信息
        SystemMessageOperation.getSystemMessage(this, mHandler);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, mNowFragment.getTag(), mNowFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (mNowFragment != to) {
            getSupportFragmentManager().beginTransaction()
                    .hide(from).show(to).commit();
            mNowFragment = to;
        }
    }

    private Fragment getConversationListFragment() {
        if (mConversationListFragment == null) {
            mConversationListFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                    .build();
            ((ConversationListFragment) mConversationListFragment).setUri(uri);
        }
        return mConversationListFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_friend_request:
                ContactsOperation.addFriend(this, mHandler);
                return true;
            case R.id.create_group:
                ContactsOperation.createGroup(this, mHandler);
                return true;
            case R.id.join_group:
                ContactsOperation.joinGroup(this, mHandler);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
