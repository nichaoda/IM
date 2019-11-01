package com.example.im.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.im.R;
import com.example.im.bean.UserTokenBean;
import com.example.im.util.HttpUtil;
import com.example.im.util.MySqlDBHelper;
import com.example.im.util.RandomPortraitUri;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final int HAS_REGISTERED = 0;
    private static final int REGISTER_FAILED = 1;
    private static final int REGISTER_SUCCEED = 2;
    private EditText mEditTextName, mEditTextUserId, mEditTextPassword, mEditTextCheckPassword;
    private Button mBtnRegister;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HAS_REGISTERED:
                    Toast.makeText(RegisterActivity.this, "该账号已注册", Toast.LENGTH_SHORT).show();
                    break;
                case REGISTER_FAILED:
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    break;
                case REGISTER_SUCCEED:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setListener();
    }

    private void setListener() {
        mBtnRegister.setOnClickListener(v -> {
            String name = mEditTextName.getText().toString();
            String userId = mEditTextUserId.getText().toString();
            String password = mEditTextPassword.getText().toString();
            String checkPassword = mEditTextCheckPassword.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(userId) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(checkPassword)) {
                Toast.makeText(this, "注册信息不完整!", Toast.LENGTH_SHORT).show();
            } else {
                if (TextUtils.equals(password, checkPassword)) {
                    new Thread(() -> {
                        // 查询MySql中是否已经注册
                        String sql = "select * from user where userId = ?";
                        MySqlDBHelper dbHelper = new MySqlDBHelper(sql);
                        dbHelper.setData(new String[]{userId});
                        ResultSet resultSet = dbHelper.executeQuery();
                        // 已注册
                        try {
                            if (resultSet.next()) {
                                Message message = Message.obtain();
                                message.what = HAS_REGISTERED;
                                mHandler.sendMessage(message);
                                return;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            dbHelper.closeConnection();
                        }
                        // 未注册
                        // 先去融云官网注册获取token
                        Map<String, String> bodies = new HashMap<>();
                        bodies.put("userId", userId);
                        bodies.put("name", name);
                        String portraitUri = RandomPortraitUri.getRandomPortraitUri();
                        bodies.put("portraitUri", portraitUri);
                        Call<UserTokenBean> call = HttpUtil.getInterfaceInstance()
                                .getUserToken(HttpUtil.getHeaders(), bodies);
                        try {
                            Response<UserTokenBean> response = call.execute();

                            UserTokenBean userTokenBean = response.body();
                            if (userTokenBean.getCode() == 200) {
                                // 注册成功,插入到数据库中
                                sql = "insert into user(userId,password,name,portraitUri,token) values(?,?,?,?,?)";
                                dbHelper = new MySqlDBHelper(sql);
                                dbHelper.setData(new String[]{userId, password, name, portraitUri,
                                        userTokenBean.getToken()});
                                if (dbHelper.executeSQL() == -1) {
                                    // 注册失败
                                    registerFailed();
                                    dbHelper.closeConnection();
                                    return;
                                }
                                Message message = Message.obtain();
                                message.what = REGISTER_SUCCEED;
                                mHandler.sendMessage(message);
                                dbHelper.closeConnection();
                            } else {
                                registerFailed();
                            }
                        } catch (IOException e) {
                            registerFailed();
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "密码输入不一致!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        mEditTextName = findViewById(R.id.edit_register_name);
        mEditTextUserId = findViewById(R.id.edit_register_user_id);
        mEditTextPassword = findViewById(R.id.edit_register_password);
        mEditTextCheckPassword = findViewById(R.id.edit_register_check_password);
        mBtnRegister = findViewById(R.id.btn_register_register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    public void registerFailed() {
        Message message = Message.obtain();
        message.what = REGISTER_FAILED;
        mHandler.sendMessage(message);
    }
}
