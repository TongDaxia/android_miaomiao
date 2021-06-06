package com.tyg.miaomiao.display;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tyg.miaomiao.R;

import java.util.ArrayList;
import java.util.List;

//获取照相机权限
public class PhotoFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private List<String> contactsList = new ArrayList<>();

    private static final String TAG = "CameraView";

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.display_contact, container, false);
        ListView contactView = view.findViewById(R.id.photo_view);

        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, contactsList);

        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {


            if (this == null) {
                ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }

            cameraIsCanUse();
            ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CAMERA}, 1);
//            ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.READ_CONTACTS}, 1);

        } else {
            getImageFromAlbum();
        }
        return contactView;

    }


    public static boolean checkPermission(Fragment fragment, final Activity activity, String permission,
                                          String hint, int requestCode) {
        // 检查权限
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // 显示我们自定义的一个窗口引导用户开启权限
//                showPermissionSettingDialog(activity, hint);
            } else {
                // 申请权限
                if (fragment == null) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                } else {
                    fragment.requestPermissions(new String[]{permission}, requestCode);
                }
            }
            return false;
        } else {  // 已经拥有权限
            return true;
        }
    }

    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }
        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, 1);
    }


    private void camera() {
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CAMERA}, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                Log.d("onRequestPermission:", "onRequestPermissionsResult: ");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "相机已经授权了哦！！！", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "未授权", Toast.LENGTH_SHORT).show();
                }
        }
    }

//    protected void getImageFromCamera() {
//        String state = Environment.getExternalStorageState();
//        if (state.equals(Environment.MEDIA_MOUNTED)) {
//            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
//            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
//        } else {
//            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
//            Uri uri = data.getData();
//            //to do find the path of pic
//
//        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
//            Uri uri = data.getData();
//            //to do find the path of pic
//        }
//    }
}
