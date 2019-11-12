package com.example.im.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.im.R;
import com.example.im.fragment.FriendFragment;
import com.example.im.fragment.GroupFragment;
import com.example.im.info.Friend;
import com.example.im.info.Group;
import com.example.im.util.ContactsOperation;
import com.example.im.util.SystemMessageOperation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

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
import static com.example.im.info.ConstValues.SEND_ADD_FRIEND_REQUEST_SUCCEED;
import static com.example.im.info.ConstValues.USER_IN_THIS_GROUP;
import static com.example.im.info.ConstValues.USER_NOT_IN_THE_TABLE;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
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
                    ((FriendFragment) getSupportFragmentManager().findFragmentByTag(FRIEND_FRAGMENT_TAG))
                            .getContactsAdapter().addFriend((Friend) msg.obj);
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
                case GROUP_CREATE_SUCCEED:
                    ((GroupFragment) getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG))
                            .getContactsAdapter().addGroup((Group) msg.obj);
                    Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
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
                    ((GroupFragment) getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG))
                            .getContactsAdapter().addGroup((Group) msg.obj);
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
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        if (savedInstanceState != null) {
            mConversationListFragment = getSupportFragmentManager().findFragmentByTag(CONVERSATION_LIST_FRAGMENT_TAG);
            mFriendFragment = getSupportFragmentManager().findFragmentByTag(FRIEND_FRAGMENT_TAG);
            mGroupFragment = getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG);
            getSupportFragmentManager().beginTransaction()
                    .show(mConversationListFragment)
                    .hide(mFriendFragment)
                    .hide(mGroupFragment)
                    .commit();
            mNowFragment = mConversationListFragment;
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
        setListener();
    }

    private void setListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.messages:
                    switchFragment(mNowFragment, getSupportFragmentManager()
                            .findFragmentByTag(CONVERSATION_LIST_FRAGMENT_TAG));
                    mNowFragment = getSupportFragmentManager().findFragmentByTag(CONVERSATION_LIST_FRAGMENT_TAG);
                    return true;
                case R.id.friends:
                    switchFragment(mNowFragment, getSupportFragmentManager()
                            .findFragmentByTag(FRIEND_FRAGMENT_TAG));
                    mNowFragment = getSupportFragmentManager().findFragmentByTag(FRIEND_FRAGMENT_TAG);
                    return true;
                case R.id.groups:
                    switchFragment(mNowFragment, getSupportFragmentManager()
                            .findFragmentByTag(GROUP_FRAGMENT_TAG));
                    mNowFragment = getSupportFragmentManager().findFragmentByTag(GROUP_FRAGMENT_TAG);
                    return true;
                default:
            }
            return false;
        });
        SystemMessageOperation.getSystemMessage(this, mHandler);
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
            mNowFragment = to;
            getSupportFragmentManager().beginTransaction()
                    .hide(from).show(to).commit();
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
