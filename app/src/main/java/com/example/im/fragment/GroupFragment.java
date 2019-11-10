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
import com.example.im.info.Group;
import com.example.im.util.ContactsOperation;

import java.util.List;

import static com.example.im.info.ConstValues.ADD_GROUP_INFO_IS_EMPTY;
import static com.example.im.info.ConstValues.CREATE_GROUP_INFO_NOT_COMPLETE;
import static com.example.im.info.ConstValues.GROUP_ADD_FAILED;
import static com.example.im.info.ConstValues.GROUP_ADD_SUCCEED;
import static com.example.im.info.ConstValues.GROUP_CREATE_FAILED;
import static com.example.im.info.ConstValues.GROUP_CREATE_SUCCEED;
import static com.example.im.info.ConstValues.GROUP_HAS_CREATED;
import static com.example.im.info.ConstValues.GROUP_NOT_EXIST;
import static com.example.im.info.ConstValues.USER_IN_THIS_GROUP;

public class GroupFragment extends Fragment {
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private ContactsAdapter mContactsAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CREATE_GROUP_INFO_NOT_COMPLETE:
                    Toast.makeText(getActivity(), "信息不完整,无法创建群组", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_HAS_CREATED:
                    Toast.makeText(getActivity(), "该群组id已存在,创建失败", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_CREATE_FAILED:
                    Toast.makeText(getActivity(), "创建失败", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_CREATE_SUCCEED:
                    mContactsAdapter.addGroup((Group) msg.obj);
                    mTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_SHORT).show();
                    break;
                case ADD_GROUP_INFO_IS_EMPTY:
                    Toast.makeText(getActivity(), "群组id为空,无法搜索", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_NOT_EXIST:
                    Toast.makeText(getActivity(), "群组id不存在", Toast.LENGTH_SHORT).show();
                    break;
                case USER_IN_THIS_GROUP:
                    Toast.makeText(getActivity(), "你已经在该群中了", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_ADD_FAILED:
                    Toast.makeText(getActivity(), "加入失败", Toast.LENGTH_SHORT).show();
                    break;
                case GROUP_ADD_SUCCEED:
                    mContactsAdapter.addGroup((Group) msg.obj);
                    mTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "加入成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    /**
     * 群组列表,启动界面去获取
     */
    private List<Group> mGroupList = Contacts.getInstance().getGroupList();

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
        mContactsAdapter = new ContactsAdapter(getActivity(), "group", null, mGroupList);
        mRecyclerView.setAdapter(mContactsAdapter);
        if (mGroupList.isEmpty()) {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText("没有群组");
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
        inflater.inflate(R.menu.fragment_group, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_group:
                ContactsOperation.createGroup(getActivity(), mHandler);
                return true;
            case R.id.join_group:
                ContactsOperation.joinGroup(getActivity(), mHandler);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
