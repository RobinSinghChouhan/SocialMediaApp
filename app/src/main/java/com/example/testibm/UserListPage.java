package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import okhttp3.internal.cache.DiskLruCache;

import static com.example.testibm.MainActivity.postList;
import static com.example.testibm.MainActivity.userPostList;


public class UserListPage extends AppCompatActivity {
    Button mainActivity;
    ListView UserListView;
    DatabaseReference databaseUsers;

    public static ArrayList<UserAccount> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_user_list_page);
        mainActivity=findViewById(R.id.mainActivity);
        UserListView=findViewById(R.id.UserListView);
        databaseUsers= FirebaseDatabase.getInstance().getReference("Users");
        userList=new ArrayList<>();
        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserListPage.this,MainActivity.class));
            }
        });
        UserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //     startActivity(new Intent(this,Conta));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
            databaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UserAccount userAccount=snapshot.getValue(UserAccount.class);
                        if(!userAccount.getUserId().equals(UserCredentials.user_email))
                        {userList.add(userAccount);}
                    }
                    UserList adapter=new UserList(UserListPage.this,userList);
                    UserListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
//    public void getUserInfo(String user_id){
//        userPostList.clear();
//        for(int i=0;i<postList.size();i++){
//            if(postList.get(i).getUser_email().equals(user_id)){
//                Post post=postList.get(i);
//                userPostList.add(post);
//            }
//        }
//    }

}
