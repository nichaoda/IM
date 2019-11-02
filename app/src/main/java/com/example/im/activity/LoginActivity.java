package com.example.im.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.im.R;
import com.example.im.info.User;
import com.example.im.util.MySqlDBHelper;

import java.sql.ResultSet;
import java.sql.SQLException;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.example.im.util.ConstValues.HAS_LOGINED;
import static com.example.im.util.ConstValues.HAS_NOT_REGISTERED;
import static com.example.im.util.ConstValues.LOGIN_INFO;
import static com.example.im.util.ConstValues.NAME;
import static com.example.im.util.ConstValues.PASSWORD;
import static com.example.im.util.ConstValues.PASSWORD_IS_WRONG;
import static com.example.im.util.ConstValues.PORTRAIT_URI;
import static com.example.im.util.ConstValues.TOKEN;
import static com.example.im.util.ConstValues.USER_ID;

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
                                RongIM.connect(token, new RongIMClient.ConnectCallback() {
                                    @Override
                                    public void onTokenIncorrect() {
                                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onSuccess(String s) {
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        // 登录成功,将信息保存到本地SharedPreferences中,下次
                                        // 可以在启动页中根据对应的值判断是否需要再次登录
                                        SharedPreferences preferences = getSharedPreferences(LOGIN_INFO, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean(HAS_LOGINED, true);
                                        editor.putString(USER_ID, user.getUserId());
                                        editor.putString(PASSWORD, user.getPassword());
                                        editor.putString(NAME, user.getName());
                                        editor.putString(PORTRAIT_URI, user.getPortraitUri());
                                        editor.putString(TOKEN, user.getToken());
                                        editor.apply();

                                        // 进入主界面
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
        mHandler.removeCallbacksAndMessages(null);
    }
}
