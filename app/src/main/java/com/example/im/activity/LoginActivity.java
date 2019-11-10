package com.example.im.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.im.R;
import com.example.im.info.User;
import com.example.im.util.ConnectRongIM;
import com.example.im.util.MySqlDBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.im.info.ConstValues.HAS_NOT_REGISTERED;
import static com.example.im.info.ConstValues.PASSWORD_IS_WRONG;

public class LoginActivity extends AppCompatActivity {
    private EditText mEditTextUserId, mEditTextPassword;
    private Button mBtnLogin, mBtnRegister;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HAS_NOT_REGISTERED:
                    Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    break;
                case PASSWORD_IS_WRONG:
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
    }

    private void setListener() {
        mBtnLogin.setOnClickListener(v -> {
            String userId = mEditTextUserId.getText().toString();
            String password = mEditTextPassword.getText().toString();
            if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "登录信息不完整!", Toast.LENGTH_SHORT).show();
            } else {
                // 查询账号是否已经存在
                new Thread(() -> {
                    String sql = "select * from user where userId = ?";
                    MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
                    dbHelper.setData(new String[]{userId});
                    ResultSet resultSet = dbHelper.executeQuery();
                    try {
                        // 已存在,判断密码是否正确
                        if (resultSet.next()) {
                            // 密码正确
                            if (TextUtils.equals(resultSet.getString("password"), password)) {
                                String name = resultSet.getString("name");
                                String portraitUri = resultSet.getString("portraitUri");
                                String token = resultSet.getString("token");
                                // 获取对应的值并存入到User信息中
                                User user = User.getInstance();
                                user.setInfo(userId, password, name, portraitUri, token);
                                // 登录
                                ConnectRongIM.connectRongIM(LoginActivity.this, user);
                            } else {
                                // 密码错误
                                Message message = Message.obtain();
                                message.what = PASSWORD_IS_WRONG;
                                mHandler.sendMessage(message);
                            }
                        } else {
                            // 不存在
                            Message message = Message.obtain();
                            message.what = HAS_NOT_REGISTERED;
                            mHandler.sendMessage(message);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        dbHelper.closeConnection();
                    }
                }).start();
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
