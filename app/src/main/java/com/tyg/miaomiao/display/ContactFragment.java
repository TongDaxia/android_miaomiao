package com.tyg.miaomiao.display;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tyg.miaomiao.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

//todo Fragment 是个什么东西
//是一种可以嵌入在活动中的UI片段，
// 能够让程序更加合理和充分地利用大屏幕的空间，出现的初衷是为了适应大屏幕的平板电脑，
// 可以将其看成一个小型Activity，又称作Activity片段。
public class ContactFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private List<String> contactsList = new ArrayList<>();
    private View view;
    private final int READ_CONTACTS_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.display_contact_page, container, false);
        ListView contactView = view.findViewById(R.id.contact_listView);
        adapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1, contactsList);
        contactView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
        } else {
            readContacts();
        }
        return view;
    }


    /**
     * 读取通讯录的方法
     */
    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = view.getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);


            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                }
                Log.d("contactsList", String.valueOf(contactsList));
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_REQUEST_CODE:
                Log.d("onRequestPermission:", "onRequestPermissionsResult: ");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(getContext(), "未授权", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

