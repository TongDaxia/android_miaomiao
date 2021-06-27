package com.tyg.miaomiao.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tyg.miaomiao.R;
import com.tyg.miaomiao.info.Message;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MessageFragment extends Fragment {

    private List<Message> messageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.display_message,container,false);
        initMessage();
        RecyclerView recyclerView = view.findViewById(R.id.message_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        MessageAdpter adpter = new MessageAdpter(messageList);
        recyclerView.setAdapter(adpter);
        return view;
    }
    private void initMessage() {
        Message friend1 = new Message(R.drawable.mabao, "麻豹",
                    "汪汪汪,汪汪", "20:15");
        Message friend2 = new Message(R.drawable.doudou, "豆豆",
                "我是豆豆，我通过了你的朋友眼中", "15:05");
        messageList.add(friend1);
        messageList.add(friend2);
//        for (int i=0;i<2;i++) {
//            Message friend1 = new Message(R.drawable.head_portrait, "略略略",
//                    "恩恩", "20:15");
//            messageList.add(friend1);
//            Message friend2 = new Message(R.drawable.head_portrait, "啦啦啦",
//                    "恩恩", "20:15");
//            messageList.add(friend2);
//            Message friend3 = new Message(R.drawable.head_portrait, "略略略",
//                    "恩恩", "20:15");
//            messageList.add(friend3);
//            Message friend4 = new Message(R.drawable.head_portrait, "略略略",
//                    "恩恩", "20:15");
//            messageList.add(friend4);
//            Message friend5 = new Message(R.drawable.head_portrait, "啦啦啦",
//                    "恩恩", "20:15");
//            messageList.add(friend5);
//        }
    }
}
