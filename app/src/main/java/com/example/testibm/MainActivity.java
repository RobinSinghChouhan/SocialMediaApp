package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button Post;
    EditText editTextName;
    EditText editTextCaption;
    ListView postListView;
    DatabaseReference databasePost;
    List<Post> postList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Post=findViewById(R.id.Post);
        editTextName=findViewById(R.id.editTextName);
        editTextCaption=findViewById(R.id.editTextCaption);
        postListView=findViewById(R.id.postList);
        postList=new ArrayList<>();
        databasePost= FirebaseDatabase.getInstance().getReference("Post");
        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databasePost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post=snapshot.getValue(Post.class);
                    postList.add(post);
                }
                PostList adapter=new PostList(MainActivity.this,postList);
                postListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addPost(){
        String name=editTextName.getText().toString();
        String caption=editTextCaption.getText().toString();
        if(!TextUtils.isEmpty(caption)) {
            String id = databasePost.push().getKey();
            Post post=new Post(id,name,caption);
            databasePost.child(id).setValue(post);
            Toast.makeText(this,"Post successfull",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"An error occured",Toast.LENGTH_LONG).show();
        }
    }
}
