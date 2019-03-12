package com.baidu.aip.robotexample;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.DialogViewHolder> {

    private List<Message> messages;
    private Context context;

    public DialogAdapter(Context context, List<Message> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position) {
        Message message = messages.get(position);
        switch (message.getType()) {
            case Message.ROBOT:
                holder.tvLeft.setVisibility(View.VISIBLE);
                holder.tvLeft.setText(message.getMessage());
                holder.tvRight.setVisibility(View.GONE);
                holder.tvUser.setVisibility(View.GONE);
                break;
            case Message.USER:
                holder.tvLeft.setVisibility(View.GONE);
                holder.tvRight.setVisibility(View.VISIBLE);
                holder.tvRight.setText(message.getMessage());
                holder.tvUser.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeft;
        TextView tvRight;
        TextView tvUser;

        public DialogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.tv_left_message);
            tvRight = itemView.findViewById(R.id.tv_right_message);
            tvUser = itemView.findViewById(R.id.tv_user_name);
        }
    }

    public static class Message {
        public static final int ROBOT = 0;
        public static final int USER = 1;

        private int type;
        private String message;

        public Message(int type, String message) {
            this.type = type;
            this.message = message;
        }

        public int getType() {
            return type;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
