package com.example.im.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;

import com.example.im.R;
import com.example.im.fragment.FriendFragment;
import com.example.im.fragment.GroupFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;

import static com.example.im.info.ConstValues.CONTACTS_FRAGMENT_TAG;
import static com.example.im.info.ConstValues.CONVERSATION_LIST_FRAGMENT_TAG;
import static com.example.im.info.ConstValues.GROUP_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private ConversationListFragment mConversationListFragment;

    private Fragment mFriendFragment = new FriendFragment();
    private Fragment mGroupFragment = new GroupFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addFragment(getConversationListFragment(), CONVERSATION_LIST_FRAGMENT_TAG);
        setListener();
    }

    private void setListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.messages:
                    changeToFragment(mConversationListFragment, CONVERSATION_LIST_FRAGMENT_TAG);
                    return true;
                case R.id.contacts:
                    changeToFragment(mFriendFragment, CONTACTS_FRAGMENT_TAG);
                    return true;
                case R.id.groups:
                    changeToFragment(mGroupFragment, GROUP_FRAGMENT_TAG);
                    return true;
                default:
            }
            return false;
        });
    }

    /**
     * @param future 将要显示的Fragment
     * @param tag    将要显示的Fragment对应tag
     */
    private void changeToFragment(Fragment future, String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        hideAllFragments(fragmentTransaction);
        // 当前Fragment不在事务中
        if (!future.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, future, tag).commit();
        } else {
            fragmentTransaction.show(future).commit();
        }
    }

    /**
     * @param fragmentTransaction Fragment事务
     */
    private void hideAllFragments(FragmentTransaction fragmentTransaction) {
        List<Fragment> fragmentList = mFragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment != null) {
                fragmentTransaction.hide(fragment);
            }
        }
    }

    /**
     * @param fragment 要添加的Fragment
     * @param tag      对应Fragment的Tag
     */
    private void addFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment, tag).commit();
    }

    private Fragment getConversationListFragment() {
        if (mConversationListFragment == null) {
            mConversationListFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
//                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
//                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
//                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")
//                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")
//                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                    .build();
            mConversationListFragment.setUri(uri);
        }
        return mConversationListFragment;
    }

    private void initView() {
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
    }
}
