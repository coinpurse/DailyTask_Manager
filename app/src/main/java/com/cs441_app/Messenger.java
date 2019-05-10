package com.cs441_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.scaledrone.lib.HistoryRoomListener;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import com.scaledrone.lib.SubscribeOptions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Messenger extends AppCompatActivity {

    private EditText editText;
    private Scaledrone scaledrone;

    private static MessageAdapter messageAdapter;
    private static ListView messagesView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_layout);

        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        //MainActivity.dh.readMessageList(MainActivity.group.getGroupID());

        MainActivity.dh.getDb().collection("Groups").document(MainActivity.group.getGroupID()).collection("Messages").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                ArrayList<Message> list = new ArrayList();
                messageAdapter.clear();
                for (QueryDocumentSnapshot doc : value) {
                        Map info = doc.getData();
                        Message m = new Message((String) info.get("content"), (String) info.get("authorName"), (String) info.get("authorID"), (String) info.get("timestamp"));
                    messageAdapter.add(m);
                    messageAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if(message.length() > 0) {
            MainActivity.dh.writeMessage(MainActivity.group.getGroupID(), new Message(message, MainActivity.user.getName(), MainActivity.user.getUserID()));
            editText.getText().clear();
        }

        //messageAdapter.add(new Message(message, MainActivity.user.getName(), MainActivity.user.getUserID(), 0, 0, 0, 0, 0));
        //messagesView.setSelection(messagesView.getCount() - 1);
    }

    public static void readMessageList(ArrayList<Message> list){
        for(int i = 0; i < list.size(); i++){
            messageAdapter.add(list.get(i));
            messageAdapter.notifyDataSetChanged();
        }
    }
}
