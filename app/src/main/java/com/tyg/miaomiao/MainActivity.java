package com.tyg.miaomiao;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tyg.miaomiao.account.LoginActivity;
import com.tyg.miaomiao.display.ContactFragment;
import com.tyg.miaomiao.display.GuangchangFragment;
import com.tyg.miaomiao.display.MessageFragment;
import com.tyg.miaomiao.display.photo.CameraVideoActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends BaseFragment implements View.OnClickListener {

    private Fragment xinxiangFragment;
    private Fragment contactFragment;
    private Fragment guangchangFragment;
    private Fragment photoFragment;


    private ImageView xinxiangBtn;
    private ImageView contactBtn;
    private ImageView photoBtn;
    private ImageView guangchangBtn;

    private TextView messageText;
    private TextView contactText;
    private TextView photoText;
    private TextView guangchangText;

    private LinearLayout messageLayout;
    private LinearLayout contactLayout;
    private LinearLayout photoLayout;
    private LinearLayout guangchangLayout;
    private Button offline;
    private FragmentTransaction fragmentTransaction;

    private TextView headerText;
    private boolean online;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(this, Color.parseColor("#9400d3"));
        initView();//初始化数据控件
        initEvent();//初始化事件
        setSelected(0);//进入界面，显示第一个
    }

    private void initView() {
        preferences = this.getSharedPreferences("online", Context.MODE_MULTI_PROCESS);
        online = preferences.getBoolean("online", false);
        Log.i("preferencesonline", String.valueOf(online));
        Boolean online2 = preferences.getBoolean("online", false);
        Log.i("preferencesonline2", String.valueOf(online2));

        //没有登录强制跳转登录页面
//        if(!online){
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//        }


        xinxiangBtn = findViewById(R.id.footer_message_icon);
        contactBtn = findViewById(R.id.footer_contact_icon);
        photoBtn = findViewById(R.id.footer_photo_icon);
        guangchangBtn = findViewById(R.id.footer_guangchang_icon);

        messageText = findViewById(R.id.footer_message_text);
        contactText = findViewById(R.id.footer_contact_text);
        photoText = findViewById(R.id.footer_photo_text);
        guangchangText = findViewById(R.id.footer_guangchang_text);

        messageLayout = findViewById(R.id.footer_message_layout);
        contactLayout = findViewById(R.id.footer_contact_layout);
        photoLayout = findViewById(R.id.footer_photo_layout);
        guangchangLayout = findViewById(R.id.footer_guangchang_layout);

        headerText = findViewById(R.id.header_text);
        offline = findViewById(R.id.offline);

        if (online) {
            offline.setVisibility(View.GONE);
        }

    }

    private void initEvent() {
        messageLayout.setOnClickListener(this);
        contactLayout.setOnClickListener(this);
        photoLayout.setOnClickListener(this);
        guangchangLayout.setOnClickListener(this);
        offline.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        resetImgText();//将按钮文字复位
        switch (view.getId()) {
            case R.id.footer_message_layout:
                setSelected(0);
                break;
            case R.id.footer_contact_layout:
                setSelected(1);
                break;
            case R.id.footer_photo_layout:
                setSelected(2);
                break;
            case R.id.footer_guangchang_layout:
                setSelected(3);
                break;
            case R.id.offline:
                editor = preferences.edit();
                editor.putBoolean("online", false);
                editor.apply();
                finishAll();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;

        }
    }


    private void setSelected(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);//隐藏所有fragment
        switch (i) {
            case 0:
                if (xinxiangFragment == null) {
                    xinxiangFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.main_layout_frame, xinxiangFragment);
                }
                fragmentTransaction.show(xinxiangFragment);
                xinxiangBtn.setImageResource(R.drawable.xinxiang_active);
                messageText.setTextColor(Color.parseColor("#60008A"));
                headerText.setText(messageText.getText());
                break;
            case 1:
                if (contactFragment == null) {
                    contactFragment = new ContactFragment();
                    fragmentTransaction.add(R.id.main_layout_frame, contactFragment);
                }
                fragmentTransaction.show(contactFragment);
                contactBtn.setImageResource(R.drawable.maogou_active);
                contactText.setTextColor(Color.parseColor("#60008A"));
                headerText.setText(contactText.getText());
                break;
            case 2:
                //申请相机权限
                requestPermission();

//                if (photoFragment == null) {
//                     photoFragment = new PhotoFragment();
//                     fragmentTransaction.add(R.id.main_layout_frame, photoFragment);
//                 }
//                fragmentTransaction.show(photoFragment);
//
//
//                photoBtn.setImageResource(R.drawable.photo_active);
//                photoText.setTextColor(Color.parseColor("#60008A"));
//                headerText.setText(photoText.getText());

                // todo  设置关闭 移动网络 ?
//                setDataConnectionState(this, false);

                //todo 打开相机照相
                Intent intentVideo = new Intent(this, CameraVideoActivity.class);
                startActivity(intentVideo);
                break;
            case 3:
                if (guangchangFragment == null) {
                    guangchangFragment = new GuangchangFragment();
                    fragmentTransaction.add(R.id.main_layout_frame, guangchangFragment);
                }
                fragmentTransaction.show(guangchangFragment);
                guangchangBtn.setImageResource(R.drawable.guangchang_active);
                //todo 颜色统一
                guangchangText.setTextColor(Color.parseColor("#60008A"));
                headerText.setText(guangchangText.getText());
                break;
        }
        fragmentTransaction.commit();
    }

    @SuppressLint("WrongConstant")
    public static void setDataConnectionState(Context cxt, boolean state) {
        ConnectivityManager connectivityManager = null;
        Class connectivityManagerClz = null;
        try {
            connectivityManager = (ConnectivityManager) cxt
                    .getSystemService("connectivity");
            connectivityManagerClz = connectivityManager.getClass();
            Method method = connectivityManagerClz.getMethod(
                    "setMobileDataEnabled", new Class[]{boolean.class});
            method.invoke(connectivityManager, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 动态申请
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {
        AndPermission.with(this)
                .permission(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        executor.execute();
                    }
                })
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Log.e("requestCAMERAPermission", "用户给权限");
                    }
                })
//                .onDenied(new Action() {
//                    @Override
//                    public void onAction(List<String> permissions) {
//                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
//                            // 打开权限设置页
//                            AndPermission.permissionSetting(MainActivity.this).execute();
//                            return;
//                        }
//                        Log.e("requestPermission", "用户拒绝权限");
//                    }
//                })
                .start();
    }


    //隐藏Fragment
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (xinxiangFragment != null) {
            fragmentTransaction.hide(xinxiangFragment);
        }
        if (contactFragment != null) {
            fragmentTransaction.hide(contactFragment);
        }
        if (photoFragment != null) {
            fragmentTransaction.hide(photoFragment);
        }
        if (guangchangFragment != null) {
            fragmentTransaction.hide(guangchangFragment);
        }
    }

    private void resetImgText() {
        xinxiangBtn.setImageResource(R.drawable.xinxiang);
        contactBtn.setImageResource(R.drawable.maogou);
        photoBtn.setImageResource(R.drawable.photo);
        guangchangBtn.setImageResource(R.drawable.guangchang);

        messageText.setTextColor(Color.parseColor("#cdcdcd"));
        contactText.setTextColor(Color.parseColor("#cdcdcd"));
        photoText.setTextColor(Color.parseColor("#cdcdcd"));
        guangchangText.setTextColor(Color.parseColor("#cdcdcd"));
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
    }

//    在6.0時Google推出运行时权限，只有在需要权限的时候，才告知用户是否授权，
//    是在runtime时候授权，而不是在原来安装的时候 。至于需要申请的权限以及申请
//    权限的过程网上有很多，搜一下就有了。这里只是记录我们在Fragment中申请权限时
//    发现Fragment中不会回调onRequestPermissionsResult方法，如果我们在Activity中
//    重写onRequestPermissionsResult方法你会发现它会回调到Activity中的
//        onRequestPermissionsResult方法，所以我们需要在Activity中的
//    onRequestPermissionsResult方法中进行处理让它把改事件传递到我们的fragment中。

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) {
            return;
        }
        // 查找在Fragment中onRequestPermissionsResult方法并调用
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
