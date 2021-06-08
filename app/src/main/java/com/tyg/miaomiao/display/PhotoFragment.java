package com.tyg.miaomiao.display;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//获取照相机权限
public class PhotoFragment extends Fragment {

    private final int CAMERA_REQUEST_CODE = 123;
    private ArrayAdapter<String> adapter;
    private List<String> photoList = new ArrayList<>();


    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //todo
       return null;


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
                    "setMobileDataEnabled", new Class[] { boolean.class });
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
                        Log.e("requestPermission", "用户给权限");
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(PhotoFragment.this, permissions)) {
                            // 打开权限设置页
                            AndPermission.permissionSetting(PhotoFragment.this).execute();
                            return;
                        }
                        Log.e("requestPermission", "用户拒绝权限");
                    }
                })
                .start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                Log.d("onRequestPermission:", "onRequestPermissionsResult:");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "相机已经授权了哦！！！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "未授权", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
