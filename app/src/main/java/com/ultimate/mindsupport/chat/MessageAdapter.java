package com.ultimate.mindsupport.chat;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultimate.mindsupport.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LEFT = 0;
    private static final int VIEW_TYPE_RIGHT = 1;

    private List<Message> messages;
    private int currentUserId;

    public MessageAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).senderId == currentUserId) {
            return VIEW_TYPE_RIGHT;
        } else {
            return VIEW_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new RightMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new LeftMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof RightMessageViewHolder) {
            ((RightMessageViewHolder) holder).bind(message);
        } else {
            ((LeftMessageViewHolder) holder).bind(message);
        }
    }

    static class LeftMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textTime;

        public LeftMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
        }

        void bind(Message message) {
            textMessage.setText(message.content);
            textTime.setText(message.timestamp != null ? message.timestamp : "");
        }
    }

    static class RightMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textTime;

        public RightMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
        }

        void bind(Message message) {
            textMessage.setText(message.content);
            textTime.setText(message.timestamp != null ? message.timestamp : "");
        }
    }
}


