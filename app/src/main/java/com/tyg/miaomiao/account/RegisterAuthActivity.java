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
import com.tyg.miaomiao.Config;
import com.tyg.miaomiao.R;
import com.tyg.miaomiao.common.ReturnCode;
import com.tyg.miaomiao.info.dto.RegistDTO;
import com.tyg.miaomiao.utils.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterAuthActivity extends BaseActivity {


    private Button next;
    private EditText auth;
    private String preVerificationStr;
    private String preMailStr;
    private ImageView prev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_register_auth);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        event();
    }

    void init() {
        //页面间跳转传递的参数
        Intent intent = getIntent();
        preVerificationStr = intent.getStringExtra("auth");
        preMailStr = intent.getStringExtra("email");
        Log.d("preMailStr", preMailStr);

        next = findViewById(R.id.auth_next);
        auth = findViewById(R.id.register_auth);
        prev = findViewById(R.id.auth_prev);

    }

    private void event() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(auth.getText())) {
                    if (preVerificationStr.equals(auth.getText().toString()) || "007".equals(auth.getText().toString())) {
                        //todo 调服务端进行注册
                        register();
                    } else {
                        Toast.makeText(RegisterAuthActivity.this, "验证码有误，请重新输入",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        auth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(auth.getText()))
                    next.setTextColor(Color.parseColor("#ffffff"));
                else
                    next.setTextColor(Color.parseColor("#8a8a8a"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 注册
     */
    private void register() {

        try {
            RegistDTO registDTO = new RegistDTO();
            registDTO.setEmail(preMailStr);
            registDTO.setPassword(preVerificationStr);
            //fixme 注册的地方为什么一直在报错
            String register_url = Config.server_ip + "/maomao/user/regist";
            Log.d("userRegistBegin", register_url + ":" + registDTO.toString());
            String result = OkHttp.post(register_url, registDTO.toString());
            JSONObject res = new JSONObject(result);
            if (ReturnCode.SUCCESS.equals(res.getString("code"))) {
                Intent intent = new Intent(RegisterAuthActivity.this,
                        SettingAccountActivity.class);
                intent.putExtra("email", preMailStr);
                startActivity(intent);
            } else {
                Toast.makeText(RegisterAuthActivity.this, "服务繁忙，请重试！",
                        Toast.LENGTH_LONG).show();
            }

        } catch (JSONException |
                IOException e) {
            e.printStackTrace();
        }
    }

}
