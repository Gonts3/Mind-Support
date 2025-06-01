package com.ultimate.mindsupport.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ultimate.mindsupport.R;

import java.util.List;
public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {

    private final List<UserChat> users;
    private final OnUserActionListener listener;

    public interface OnUserActionListener {
        void onEndSession(UserChat user);
        void onReassign(UserChat user);
    }

    public AssignmentAdapter(List<UserChat> users, OnUserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textUsername;
        Button btnEndSession, btnReassign;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textUsername = itemView.findViewById(R.id.textUsername);
            btnEndSession = itemView.findViewById(R.id.btnEndSession);
            btnReassign = itemView.findViewById(R.id.btnReassign);
        }
    }

    @NonNull
    @Override
    public AssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assignment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.ViewHolder holder, int position) {
        UserChat user = users.get(position);
        holder.textUsername.setText(user.getName());

        holder.btnEndSession.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEndSession(user);
            }
        });

        holder.btnReassign.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReassign(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
