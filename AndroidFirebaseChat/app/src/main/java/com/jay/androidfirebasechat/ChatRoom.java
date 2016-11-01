package com.jay.androidfirebasechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jay on 2016-11-01.
 */
public class ChatRoom extends AppCompatActivity {

    private Button send_message_button;
    private EditText message_field;
    private TextView chat_conversation;

    private String user_name;
    private String room_name;

    private DatabaseReference root;
    private String tempKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        send_message_button = (Button) findViewById(R.id.message_button);
        message_field = (EditText) findViewById(R.id.message_field);
        chat_conversation = (TextView) findViewById(R.id.textView);

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle("Chatroom: " + room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Map<String,Object> map = new HashMap<String, Object>();
                tempKey = root.push().getKey();

//                root.updateChildren(map);

                DatabaseReference message_root = root.child(tempKey);
                Map<String,Object> message = new HashMap<String, Object>();
                message.put("name", user_name);
                message.put("message", message_field.getText().toString());
                message_root.updateChildren(message);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_message(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                append_message(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private String chat_message;
    private String chat_user_name;

    private void append_message(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()) {
            chat_message = (String) ((DataSnapshot) i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot) i.next() ).getValue();

            chat_conversation.append(chat_user_name + ": " + chat_message + "\n");
        }
    }
}
