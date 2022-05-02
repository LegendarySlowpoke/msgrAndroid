package com.restur.msgrtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.restur.msgrtest.adapters.AdapterChat;
import com.restur.msgrtest.consts.ApplicationData;
import com.restur.msgrtest.models.ModelChat;

import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    RecyclerView chatRecycler;
    AdapterChat chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        List<ModelChat> modelChats = ApplicationData.getChatListOrdered();
        setChatRecycler(modelChats);
    }


    private void setChatRecycler(List<ModelChat> chats) {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        chatRecycler = findViewById(R.id.chat_recycler);
        chatRecycler.setLayoutManager(layoutManager);

        chatAdapter = new AdapterChat(this, chats);
        chatRecycler.setAdapter(chatAdapter);
    }
}