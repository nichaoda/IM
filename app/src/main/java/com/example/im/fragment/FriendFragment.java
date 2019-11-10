package com.example.im.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.im.R;
import com.example.im.adapter.ContactsAdapter;
import com.example.im.info.Contacts;
import com.example.im.info.Friend;
import com.example.im.util.ContactsOperation;

import java.util.List;

import static com.example.im.info.ConstValues.ADD_SUCCEED;
import static com.example.im.info.ConstValues.CAN_NOT_ADD_MYSELF;
import static com.example.im.info.ConstValues.EDITTEXT_IS_EMPTY;
import static com.example.im.info.ConstValues.FRIEND_HAS_ADDED;
import static com.example.im.info.ConstValues.USER_NOT_IN_THE_TABLE;

public class FriendFragment extends Fragment {
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private ContactsAdapter mContactsAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EDITTEXT_IS_EMPTY:
                    Toast.makeText(getActivity(), "id为空,无法搜索", Toast.LENGTH_SHORT).show();
                    break;
                case CAN_NOT_ADD_MYSELF:
                    Toast.makeText(getActivity(), "不能添加自己", Toast.LENGTH_SHORT).show();
                    break;
                case USER_NOT_IN_THE_TABLE:
                    Toast.makeText(getActivity(), "添加用户不存在", Toast.LENGTH_SHORT).show();
                    break;
                case FRIEND_HAS_ADDED:
                    Toast.makeText(getActivity(), "你已添加该用户", Toast.LENGTH_SHORT).show();
                    break;
                case ADD_SUCCEED:
                    mContactsAdapter.addFriend((Friend) msg.obj);
                    mTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };
    /**
     * 好友列表,启动界面去获取
     */
    private List<Friend> mFriendList = Contacts.getInstance().getFriendList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        initView(view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mContactsAdapter = new ContactsAdapter(getActivity(), "friend", mFriendList, null);
        mRecyclerView.setAdapter(mContactsAdapter);
        if (mFriendList.isEmpty()) {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText("没有好友");
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void initView(View view) {
        mTextView = view.findViewById(R.id.fragment_container_text);
        mRecyclerView = view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_friend, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_friend) {
            ContactsOperation.addFriend(getActivity(), mHandler);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
