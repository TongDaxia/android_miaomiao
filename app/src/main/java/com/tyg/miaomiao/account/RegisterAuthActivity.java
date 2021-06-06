package com.tyg.miaomiao.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import androidx.annotation.NonNull;

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

    public static final int UPDATE_TEXT = 1;

    /**
     * todo 这个做什么用的还需要研究
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    Toast.makeText(RegisterAuthActivity.this, "注册失败，请重试！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


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
//                        login();
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
        final String mailSTr = preMailStr;
        final String verificationStr = preVerificationStr;

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result;
                try {
                    RegistDTO registDTO = new RegistDTO();
                    registDTO.setEmail(mailSTr);
                    registDTO.setPassword(verificationStr);
                    String register_url = Config.server_ip + "/maomao/user/regist";
                    Log.d("userRegistBegin", register_url + ":" + registDTO.toString());
                    // 为什么提示这里不是新建线程，是main线程
                    //大蠢驴！！new Thread().start() 不是new Thread().run()
                    result = OkHttp.post(register_url, registDTO.toString());
                    Log.d("userRegistresult", result);

                    JSONObject resObject = new JSONObject(result);

                    if (ReturnCode.SUCCESS.equals(resObject.getString("code"))) {

                        Intent intent = new Intent(RegisterAuthActivity.this, SettingAccountActivity.class);
                        intent.putExtra("email", mailSTr);
                        startActivity(intent);
                        finish();

                    } else {
                        //todo 注册失败的提示
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                        Looper.prepare();
//                        Toast.makeText(RegisterAuthActivity.this, "服务繁忙，请重试！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                } catch (JSONException |
                        IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}