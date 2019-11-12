package com.example.im.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.List;

public class GroupFragment extends Fragment {
    private ContactsAdapter mContactsAdapter;

    public ContactsAdapter getContactsAdapter() {
        return mContactsAdapter;
    }

    /**
     * 群组列表,启动界面去获取
     */
    private List<Group> mGroupList = Contacts.getInstance().getGroupList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mContactsAdapter = new ContactsAdapter(getActivity(), "group", null, mGroupList);
        recyclerView.setAdapter(mContactsAdapter);
        return view;
    }
}
