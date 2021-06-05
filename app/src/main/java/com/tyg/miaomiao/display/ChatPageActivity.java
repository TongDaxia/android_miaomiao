package com.tyg.miaomiao.display;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tyg.miaomiao.R;
import com.tyg.miaomiao.info.entity.ChatDO;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatPageActivity extends Activity {

    private ImageView prev;

    private List<ChatDO> chatDOList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView chatRecyclerView;
    private ChatAdapter adapter;
    private TextView username;
    private ImageView location;
    private String name;
    private int headImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_chat_page);
        setStatusBarColor(this, Color.parseColor("#9400d3"));
        String name = getIntent().getStringExtra("username");
        Log.d("chartName", name);
        createSQL();
        initChat();
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(chatDOList);
        chatRecyclerView.setAdapter(adapter);
        chatRecyclerView.scrollToPosition(chatDOList.size() - 1);
        event();
    }

    private void createSQL() {
        LitePal.getDatabase();
    }

    private void initChat() {
//        LitePal.findBySQL()
        List<ChatDO> chatDOS = LitePal.findAll(ChatDO.class);
        for (ChatDO chatDO : chatDOS) {
            chatDOList.add(chatDO);
        }
    }

    private void init() {
        prev = findViewById(R.id.chat_prev);
        inputText = findViewById(R.id.chat_text);
        send = findViewById(R.id.send_text);
        chatRecyclerView = findViewById(R.id.chat_recyclerView);
        username = findViewById(R.id.chat_username);
        location = findViewById(R.id.chat_location);
        name = getIntent().getStringExtra("username");
        username.setText(name);
        headImg = getIntent().getIntExtra("headImg", 0);
    }

    private void event() {
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {


                    NotificationManager manager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification;
                    String channelId = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        channelId = "1";
                        NotificationChannel channel = new NotificationChannel(channelId,
                                "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.enableLights(true); //是否在桌面icon右上角展示小红点
                        channel.setLightColor(Color.RED); //小红点颜色
                        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                        manager.createNotificationChannel(channel);

                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notification = new Notification.Builder(ChatPageActivity.this, channelId)
                                .setContentTitle(name)
                                .setContentText(content)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(headImg)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        headImg))
                                .build();
                    } else {
                        notification = new Notification.Builder(ChatPageActivity.this)
                                .setContentTitle(name)
                                .setContentText(content)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(headImg)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        headImg))
                                .build();
                    }
                    manager.notify(headImg, notification);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date date = new java.util.Date();
                    ChatDO chatDO = new ChatDO(content, ChatDO.TYPE_SEND, sdf.format(date));
                    chatDO.save();
                    chatDOList.add(chatDO);
                    adapter.notifyItemInserted(chatDOList.size() - 1);
                    chatRecyclerView.scrollToPosition(chatDOList.size() - 1);
                    inputText.setText("");
                }
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatPageActivity.this, LocationActivity.class));
            }
        });
    }


    private void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(statusColor);
        }
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
}
