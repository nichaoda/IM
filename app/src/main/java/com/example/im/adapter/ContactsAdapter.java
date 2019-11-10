package com.example.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.im.R;
import com.example.im.info.Friend;
import com.example.im.info.Group;

import java.util.List;

import io.rong.imkit.RongIM;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {

    private Context mContext;
    private String mFragmentName;
    private List<Friend> mFriendList;
    private List<Group> mGroupList;

    /**
     * @param context      Context对象
     * @param fragmentName 对应的Fragment是Friend还是Group
     * @param friendList   Friend列表,如果是FriendFragment,就添加该参数,否则为null
     * @param groupList    Group列表,如果是GroupFragment,就添加该参数,否则为null
     */
    public ContactsAdapter(Context context, String fragmentName,
                           List<Friend> friendList, List<Group> groupList) {
        mContext = context;
        mFragmentName = fragmentName;
        mFriendList = friendList;
        mGroupList = groupList;
    }

    @NonNull
    @Override
    public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactsHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.contacts_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsHolder holder, int position) {
        if ("friend".equals(mFragmentName)) {
            // 好友列表
            Friend friend = mFriendList.get(position);
            holder.bind(friend);
//            RongIM.getInstance().startPrivateChat(mContext, friend.getUserId(), friend.getName());
            return;
        }
        // 群组列表
        Group group = mGroupList.get(position);
        holder.bind(group);
//            RongIM.getInstance().startGroupChat(mContext, group.getGroupId(), group.getName());
    }

    public void addFriend(Friend friend) {
        mFriendList.add(friend);
        this.notifyDataSetChanged();
    }

    public void addGroup(Group group) {
        mGroupList.add(group);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if ("friend".equals(mFragmentName)) {
            return mFriendList.size();
        } else if ("group".equals(mFragmentName)) {
            return mGroupList.size();
        }
        return 0;
    }

    class ContactsHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public ContactsHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.contacts_item_portrait);
            mTextView = itemView.findViewById(R.id.contacts_item_name);
        }

        public void bind(Friend friend) {
            mTextView.setText(friend.getName());
            Glide.with(mContext).load(friend.getPortraitUri()).into(mImageView);
        }

        public void bind(Group group) {
            mTextView.setText(group.getName());
            Glide.with(mContext).load(R.drawable.group).into(mImageView);
        }
    }
}
