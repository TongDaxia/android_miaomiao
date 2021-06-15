package com.tyg.miaomiao.display.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tyg.miaomiao.R;
import com.tyg.miaomiao.info.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class PhotoFragment extends Fragment {

    private List<Message> messageList = new ArrayList<>();

    @BindView(R.id.video_photo)
    ImageView videoPhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.display_message,container,false);
//        initMessage();
//        RecyclerView recyclerView = view.findViewById(R.id.message_recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        MessageAdpter adpter = new MessageAdpter(messageList);
//        recyclerView.setAdapter(adpter);
//        return view;

        View view=inflater.inflate(R.layout.activity_camera_video,container,false);
        return view;
    }

    private void initMessage() {
        Message friend1 = new Message(R.drawable.head_portrait, "麻豹",
                "汪汪汪", "20:15");
        messageList.add(friend1);
    }
}
