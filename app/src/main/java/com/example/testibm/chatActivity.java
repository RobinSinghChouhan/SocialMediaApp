package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {
    Button mainActivity,chatSend;
    ImageView toChat;
    String id;
    TextView username,name;
    EditText chatText;
    chatList chatListAdapter;
    String chat_sent;
    ListView listView;
    String chat_from_dp;
    private String user_name,email;
    DatabaseReference databaseChat;
    List<Chat> chats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
     //   setContentView(R.layout.activity_chat);
        setContentView(R.layout.new_activity_chat);
        mainActivity=findViewById(R.id.mainActivity);
        chatSend=findViewById(R.id.chatSend);
        toChat=findViewById(R.id.toChat);
        chatText=findViewById(R.id.chatText);
        username=findViewById(R.id.username);
        name=findViewById(R.id.name);
        listView=findViewById(R.id.chatList);
        Intent intent=getIntent();
        chats=new ArrayList<>();
        user_name=intent.getStringExtra("Chat_username");
        email=intent.getStringExtra("Chat_useremail");
        Picasso.get().load(intent.getStringExtra("Chat_userdp")).into(toChat);
        username.setText(intent.getStringExtra("Chat_useremail"));
        name.setText(intent.getStringExtra("Chat_username"));
                Log.i("NOWNEWONE",String.valueOf(chats.size()));
            chat_from_dp=intent.getStringExtra("Chat_userdp");
//        if(chats.size()==0){
//            id=UserCredentials.onlineUsername+user_name;
//        }
//        id=UserCredentials.onlineUsername+user_name;
        databaseChat= FirebaseDatabase.getInstance().getReference("Chats");
        id=intent.getStringExtra("Chat_Id");
      //  Log.i("INFORMATION",chats.get(0).getChat());
       // Log.i("DATABSE",)
        databaseChat=databaseChat.child(id);

        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chatActivity.this,MainActivity.class));
            }
        });
        chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChat();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletechat(position);
                return true;
            }
        });

        chatListAdapter=new chatList(chatActivity.this,chats);
        listView.setAdapter(chatListAdapter);
    }

    private void addChat(){
        chat_sent=chatText.getText().toString();
        String id=databaseChat.push().getKey();
        Chat chat=new Chat(id,chat_sent,UserCredentials.user_email,chat_from_dp);
        databaseChat.child(id).setValue(chat);
    }

    private void deletechat(final int pos){
        new AlertDialog.Builder(this)
                .setTitle("Are you sure")
                .setMessage("Delete for everyone")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference=databaseChat.child(chats.get(pos).getId());
                        databaseReference.removeValue();
                      //  chatListAdapter.notifyDataSetChanged();
                        Toast.makeText(chatActivity.this,"Deleted Successfully!",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No",null)
                .show();



    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    chats.add(chat);
                }

//                chatListAdapter=new chatList(chatActivity.this,chats);
//                listView.setAdapter(chatListAdapter);
                chatListAdapter.notifyDataSetChanged();
                Log.i("SIZEOFCHAT",String.valueOf(chats.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
