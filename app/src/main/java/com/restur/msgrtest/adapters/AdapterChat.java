package com.restur.msgrtest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restur.msgrtest.R;
import com.restur.msgrtest.consts.ApplicationData;
import com.restur.msgrtest.models.ModelChat;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ChatViewHolder> {

    Context context;
    List<ModelChat> chats;

    public AdapterChat(Context context, List<ModelChat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatItems = LayoutInflater.from(context).inflate(R.layout.chat_window_item, parent, // R.layout.chat_window_item here should be pasted item layout to be shown in recycler view
                false);
         ChatViewHolder chatViewHolder = new ChatViewHolder(chatItems);
         return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder currentChatWindow, int position) {
        ModelChat currentChatModel = chats.get(position);
        String chatName = currentChatModel.getChatName();
        //Checking chat name
        if (chatName.equals("Chat")) {
            if (currentChatModel.getCreatorId().equals(ApplicationData.getOwner().getId())) {
                Long invitedId = currentChatModel.getInvitedUserId();
                chatName =
                        ApplicationData.getUserModelFromUserMap(invitedId).getUserName();
            } else {
                Long creatorId = currentChatModel.getCreatorId();
                chatName =
                        ApplicationData.getUserModelFromUserMap(creatorId).getUserName();
            }
        }

        //Setting text in labels;
        currentChatWindow.chatUserName.setText(chatName);
        currentChatWindow.chatLastMsg.setText(chats.get(position).getLastMessageString());

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static final class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView chatUserName;
        TextView chatLastMsg;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            chatUserName = itemView.findViewById(R.id.chat_user_name);
            chatLastMsg = itemView.findViewById(R.id.chat_last_msg);
        }

    }
}
