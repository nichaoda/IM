package com.example.im.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.im.R;

public class LoginActivity extends AppCompatActivity {
    private EditText mEditTextUserId, mEditTextPassword;
    private Button mBtnLogin, mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
    }

    private void setListener() {
        mBtnLogin.setOnClickListener(v -> {

        });
        mBtnRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    private void initView() {
        mEditTextUserId = findViewById(R.id.edit_login_user_id);
        mEditTextPassword = findViewById(R.id.edit_login_password);
        mBtnLogin = findViewById(R.id.btn_login_login);
        mBtnRegister = findViewById(R.id.btn_login_register);
    }
}
