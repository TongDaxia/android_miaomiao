package com.tyg.miaomiao.display;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyg.miaomiao.R;
import com.tyg.miaomiao.info.Message;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdpter extends RecyclerView.Adapter<MessageAdpter.ViewHolder> {

   private List<Message> messages;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View context = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_message,parent,false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_message_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Message message = messages.get(position);
                Intent intent = new Intent(context.getContext(), ChatPageActivity.class);
                intent.putExtra("username",holder.username.getText());
                intent.putExtra("headImg",message.getHeadImg());
                context.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.image.setImageResource(message.getHeadImg());
        holder.username.setText(message.getUsername());
        holder.time.setText(message.getTime());
        holder.content.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View messageView;
        ImageView image;
        TextView username;
        TextView content;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageView = itemView;
            image = itemView.findViewById(R.id.message_item_image);
            username = itemView.findViewById(R.id.message_item_username);
            content = itemView.findViewById(R.id.message_item_content);
            time = itemView.findViewById(R.id.message_item_time);
        }
   }

   public MessageAdpter(List<Message> messageList){
       messages=messageList;
   }
}
