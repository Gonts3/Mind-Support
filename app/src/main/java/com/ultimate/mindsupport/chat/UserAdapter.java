package com.ultimate.mindsupport.chat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ultimate.mindsupport.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnUserClickListener {
        void onUserClick(UserChat userChat);
    }

    private Context context;
    private List<UserChat> userChatList;
    private OnUserClickListener listener;

    public UserAdapter(Context context, List<UserChat> userChatList, OnUserClickListener listener) {
        this.context = context;
        this.userChatList = userChatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserChat userChat = userChatList.get(position);

        holder.textUsername.setText(userChat.getName());
        holder.textLastMessage.setText(userChat.getLastMessage());
        holder.textTime.setText(userChat.getTime());

        // Load image using Glide
        Glide.with(context)
                .load(userChat.getProfileImageUrl())
                .placeholder(R.drawable.sample_profile) // fallback image
                .circleCrop()
                .into(holder.imageProfile);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(userChat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textUsername, textLastMessage, textTime;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textUsername = itemView.findViewById(R.id.textUsername);
            textLastMessage = itemView.findViewById(R.id.textLastMessage);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }
}

