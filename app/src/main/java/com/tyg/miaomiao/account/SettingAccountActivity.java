package com.tyg.miaomiao.account;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tyg.miaomiao.BaseActivity;
import com.tyg.miaomiao.Config;
import com.tyg.miaomiao.R;
import com.tyg.miaomiao.utils.OkHttp;

import java.io.IOException;

/**
 * todo 产品优化，本页面应该可以跳过！
 */
public class SettingAccountActivity extends BaseActivity {

    private EditText username;
    private EditText password;
    private EditText password_repeat;
    private Button next;
    private String mail;
    private ImageView prev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_account);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        event();
    }

    private void init() {
        WebView webView = findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        Log.d("----初始化的地方--------", "");
        webView.loadUrl(Config.server_ip);
        mail = getIntent().getStringExtra("email");
        username = findViewById(R.id.settingAccount_username);
        password = findViewById(R.id.settingAccount_password);
        password_repeat = findViewById(R.id.settingAccount_password_repeat);
        next = findViewById(R.id.settingAccount_next);
        prev = findViewById(R.id.account_prev);
    }

    private void event() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!TextUtils.isEmpty(username.getText())) &&
                        (!TextUtils.isEmpty(password.getText())) &&
                        (!TextUtils.isEmpty(password_repeat.getText())) &&
                        (String.valueOf(password.getText()).equals(String.valueOf(password_repeat.getText())))) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //todo 设置用户信息
                            StringBuilder url = new StringBuilder(Config.server_ip);
                            url.append("/maomao");
                            url.append("?");
                            url.append("username=" + username.getText());
                            url.append("&");
                            url.append("password=" + password.getText());
                            url.append("&");
                            url.append("mail=" + mail);
                            try {
                                String res = OkHttp.get(String.valueOf(url));
                                Log.d("user1", res);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    finishAll(SettingAccountActivity.this);
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((!TextUtils.isEmpty(username.getText())) &&
                        (!TextUtils.isEmpty(password.getText())) &&
                        (!TextUtils.isEmpty(password_repeat.getText())))
                    next.setTextColor(Color.parseColor("#ffffff"));
                else
                    next.setTextColor(Color.parseColor("#8a8a8a"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password_repeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((!TextUtils.isEmpty(username.getText())) &&
                        (!TextUtils.isEmpty(password.getText())) &&
                        (!TextUtils.isEmpty(password_repeat.getText())))
                    next.setTextColor(Color.parseColor("#ffffff"));
                else
                    next.setTextColor(Color.parseColor("#8a8a8a"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password_repeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ((!TextUtils.isEmpty(username.getText())) &&
                        (!TextUtils.isEmpty(password.getText())) &&
                        (!TextUtils.isEmpty(password_repeat.getText()))) {
                    next.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    next.setTextColor(Color.parseColor("#8a8a8a"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
