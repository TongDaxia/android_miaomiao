package com.tyg.miaomiao.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tyg.miaomiao.BaseActivity;
import com.tyg.miaomiao.R;

import java.util.Random;

/**
 * 注册页面
 */
public class RegisterMailActivity extends BaseActivity {

    private Button next;
    private EditText mail;
    private char[] code = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'v', 'u',
            'w', 'x', 'y', 'z'};
    private String verification;
    private ImageView prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_register_mail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        event();
    }

    void init() {
        next = findViewById(R.id.mail_next);
        prev = findViewById(R.id.mail_prev);
        mail = findViewById(R.id.register_mail);
    }

    private void event() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mail.getText())) {
                    verification = authCode();
                    Log.d("SendMailUtil：", verification);
//                    SendMailUtil.send(mail.getText().toString(), verification);
                    //todo 服务端发送邮件

                    //页面之间传递参数的实例
                    Intent intent = new Intent(RegisterMailActivity.this,
                            RegisterAuthActivity.class);
                    intent.putExtra("email", mail.getText().toString());
                    intent.putExtra("auth", verification);
                    startActivity(intent);
                } else
                    Toast.makeText(RegisterMailActivity.this, "请输入邮箱",
                            Toast.LENGTH_LONG).show();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(mail.getText()))
                    next.setTextColor(Color.parseColor("#ffffff"));
                else
                    next.setTextColor(Color.parseColor("#8a8a8a"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private String authCode() {
        Random ra = new Random();
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            res.append(code[ra.nextInt(36)]);
        }
        return res.toString();
    }

}
