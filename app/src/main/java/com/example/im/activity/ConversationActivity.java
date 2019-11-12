package com.example.im.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;

import com.example.im.R;

import java.util.Locale;

import io.rong.imkit.fragment.ConversationFragment;

public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        String conversationType = getIntent().getData().getLastPathSegment().toLowerCase(Locale.US);
        String targetId = getIntent().getData().getQueryParameter("targetId");
        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationFragment fragment = (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(conversationType)
                .appendQueryParameter("targetId", targetId).build();
        fragment.setUri(uri);
    }
}
