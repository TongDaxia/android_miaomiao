package com.tyg.miaomiao.display;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyg.miaomiao.R;
import com.tyg.miaomiao.info.entity.ChatDO;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//todo 适配器的作用我还不太清楚
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatDO> chatDOList;

    public ChatAdapter(List<ChatDO> chatDOS){
        chatDOList = chatDOS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_chat_page_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatDO chatDO = chatDOList.get(position);
        if(chatDO.getType()== ChatDO.TYPE_RECEIVED){
            holder.left_bubble.setVisibility(View.VISIBLE);
            holder.right_bubble.setVisibility(View.GONE);
            holder.left_msg.setText(chatDO.getContent());
        }else if(chatDO.getType()== ChatDO.TYPE_SEND){
            holder.right_bubble.setVisibility(View.VISIBLE);
            holder.left_bubble.setVisibility(View.GONE);
            holder.right_msg.setText(chatDO.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return chatDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout left_bubble;
        LinearLayout right_bubble;
        TextView left_msg;
        TextView right_msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            left_bubble = itemView.findViewById(R.id.left_bubble);
            right_bubble = itemView.findViewById(R.id.right_bubble);
            left_msg = itemView.findViewById(R.id.left_bubble_msg);
            right_msg = itemView.findViewById(R.id.right_bubble_msg);
        }
    }


}
