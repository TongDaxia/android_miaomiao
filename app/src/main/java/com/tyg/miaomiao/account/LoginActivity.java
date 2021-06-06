package com.tyg.miaomiao.account;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tyg.miaomiao.BaseActivity;
import com.tyg.miaomiao.Config;
import com.tyg.miaomiao.MainActivity;
import com.tyg.miaomiao.R;
import com.tyg.miaomiao.common.ReturnCode;
import com.tyg.miaomiao.info.dto.LoginDTO;
import com.tyg.miaomiao.utils.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends BaseActivity {

    public static final int UPDATE_TEXT = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    Toast.makeText(LoginActivity.this, "账号或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private EditText username;
    private EditText password;
    private ImageButton login;
    private ImageButton showHide;
    private TextView register;
    private CheckBox rememberPassword;
    private ImageView home;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        event();
    }

    void init() {
        preferences = this.getSharedPreferences("online", Context.MODE_MULTI_PROCESS);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        showHide = findViewById(R.id.showHide);
        register = findViewById(R.id.register);
        rememberPassword = findViewById(R.id.remember_password);
        username.setText(preferences.getString("username", ""));
        boolean isRemember = preferences.getBoolean("remember_password", false);

        if (isRemember) {
            password.setText(preferences.getString("password", ""));
            rememberPassword.setChecked(true);
        }
        Spannable spanText = username.getText();
        if (spanText != null) {
            Selection.setSelection(spanText, spanText.length());
        }
        if ((!TextUtils.isEmpty(password.getText())) && (!TextUtils.isEmpty(username.getText()))) {
            login.setImageResource(R.drawable.login_active);
        } else {
            login.setImageResource(R.drawable.login);
        }

        home = findViewById(R.id.home);

    }

    private void event() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getInputType() == (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT)) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showHide.setImageResource(R.drawable.show);
                } else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    showHide.setImageResource(R.drawable.hide);
                }
                // 保证切换后光标位于文本末尾
                Spannable spanText = password.getText();
                if (spanText != null) {
                    Selection.setSelection(spanText, spanText.length());
                }
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((!TextUtils.isEmpty(password.getText())) && (!TextUtils.isEmpty(username.getText())))
                    login.setImageResource(R.drawable.login_active);
                else
                    login.setImageResource(R.drawable.login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if ((!TextUtils.isEmpty(password.getText())) && (!TextUtils.isEmpty(username.getText())))
                    login.setImageResource(R.drawable.login_active);
                else
                    login.setImageResource(R.drawable.login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterMailActivity.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

    }

    /**
     * 用户登录
     */
    private void login() {
        final String userStr = String.valueOf(username.getText());
        final String passStr = String.valueOf(password.getText());

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    LoginDTO loginDTO = new LoginDTO();
                    loginDTO.setPhone(userStr);
                    loginDTO.setPassword(passStr);

                    //todo 登录的地方
                    String login_url = Config.server_ip + "/maomao/user/login";
                    Log.d("userLoginBegin", login_url + ":" + loginDTO.toString());
                    result = OkHttp.post(login_url, loginDTO.toString());
                    JSONObject res = new JSONObject(result);
                    if (ReturnCode.SUCCESS.equals(res.getString("code"))) {
                        editor = preferences.edit();
                        if (rememberPassword.isChecked()) {
                            editor.putBoolean("remember_password", true);
                            editor.putString("password", passStr);
                        } else
                            editor.clear();
                        editor.putString("username", userStr);

                        editor.putBoolean("online", true);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
