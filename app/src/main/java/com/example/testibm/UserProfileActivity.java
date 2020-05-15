package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

import static com.example.testibm.MainActivity.postList;

public class UserProfileActivity extends AppCompatActivity {
    TextView post_number,follower_number,following_number,username;
    CircleImageView profile_image;
    Button chat;
    GridView postGridView;
    Button home;
    String chat_email_id;
    String chat_user_dp;
    String chat_user_name;
    DatabaseReference databaseChatUser;
    List<ChatUser> chat_to_user;
    String chatting_name;
    boolean chat_user_exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_user_profile);
        post_number=findViewById(R.id.post_number);
        follower_number=findViewById(R.id.follower_number);
        following_number=findViewById(R.id.following_number);
        postGridView=findViewById(R.id.postGridView);
        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        home=findViewById(R.id.home);
        chat_to_user=new ArrayList<>();
        databaseChatUser= FirebaseDatabase.getInstance().getReference("ChatUsers");
        chat=findViewById(R.id.chat);
        chat_user_exist=false;
        Intent intent=getIntent();
        username.setText(intent.getStringExtra("User_name"));
        post_number.setText(intent.getStringExtra("User_posts"));
        follower_number.setText(intent.getStringExtra("User_followers"));
        following_number.setText(intent.getStringExtra("User_following"));

        chat_email_id=intent.getStringExtra("User_email");
        chat_user_dp=intent.getStringExtra("User_Image");
        chat_user_name=intent.getStringExtra("User_name");

        Picasso.get().load(intent.getStringExtra("User_Image")).into(profile_image);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this,MainActivity.class));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatActivity(chat_email_id,chat_user_dp,chat_user_name);
            }
        });
        chatting_name=UserCredentials.onlineUsername+chat_user_name;
        CustomAdapter customAdapter=new CustomAdapter(UserProfileActivity.this,postList);
        postGridView.setAdapter(customAdapter);
    }

    public void chatActivity(String chat_email_id,String chat_user_dp,String chat_user_name){
            if(chat_to_user.size()==0){
                String id=databaseChatUser.push().getKey();
                ChatUser chatUser=new ChatUser(id,chatting_name);
                databaseChatUser.child(id).setValue(chatUser);
            }

        for(int i=0;i<chat_to_user.size();i++){
            if(chat_to_user.get(i).getChat_user_name().equals(chat_user_name+UserCredentials.onlineUsername) ||
                chat_to_user.get(i).getChat_user_name().equals(UserCredentials.onlineUsername+chat_user_name)){
                chatting_name=chat_to_user.get(i).getChat_user_name();
                chat_user_exist=true;
            }else{

            }
            Log.i("DATASET",chat_to_user.get(i).getChat_user_name());
        }
        if(!chat_user_exist){
            String id=databaseChatUser.push().getKey();
            ChatUser chatUser=new ChatUser(id,chatting_name);
            databaseChatUser.child(id).setValue(chatUser);
            chat_user_exist=false;
        }

        Intent intent=new Intent(UserProfileActivity.this,chatActivity.class);
        intent.putExtra("Chat_username",chat_user_name);
        intent.putExtra("Chat_userdp",chat_user_dp);
        intent.putExtra("Chat_useremail",chat_email_id);
        intent.putExtra("Chat_Id",chatting_name);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseChatUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chat_to_user.clear();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                ChatUser chatUser=snapshot.getValue(ChatUser.class);
                chat_to_user.add(chatUser);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
