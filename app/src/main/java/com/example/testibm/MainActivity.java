package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String POST_NAME="postcreator";
    public static final String POST_CAPTION="postcaption";
    public static final String POST_ID="postid";
    public static final String POST_USER_PROFILE="post_creator_image";
    public static int positionShow=0;
    ImageView post_button;
    ListView postListView;
    Button logout;
    DatabaseReference databasePost;
    FirebaseAuth mFirebaseAuth;

    TextView textViewTest;

    int initialPosts=0;
    int finalPosts=0;

    DatabaseReference databaseUser;
    Button textView;

    private FirebaseAuth.AuthStateListener authStateListener;
    public static ArrayList<Post> postList;
    public static ArrayList<Post> userPostList;
    private int num=0;
    PostList adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
      //  setContentView(R.layout.activity_main);
        setContentView(R.layout.new_activity_main);
        //Appearance();
        textView=findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentUser=new Intent(MainActivity.this,UserProfileActivity.class);
                intentUser.putExtra("User_Image",UserCredentials.userImage);
                intentUser.putExtra("User_posts",UserCredentials.posts_number);
                intentUser.putExtra("User_followers",UserCredentials.follower_number);
                intentUser.putExtra("User_following",UserCredentials.following_number);
                intentUser.putExtra("User_name",UserCredentials.onlineUsername);
                startActivity(intentUser);
            }
        });

        textViewTest=findViewById(R.id.textViewTest);
        textViewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UserListPage.class));
            }
        });
        mFirebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
        post_button=findViewById(R.id.post_img_button);
        postList=new ArrayList<>();
        userPostList=new ArrayList<>();
        postListView=findViewById(R.id.postLists);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,SignIn.class));
            }
        });
        databasePost= FirebaseDatabase.getInstance().getReference("Post");
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPostActivity();
            }
        });


        databaseUser=FirebaseDatabase.getInstance().getReference("Users");
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserAccount userAccount=snapshot.getValue(UserAccount.class);
                    //   userList.add(userAccount);
                    if(userAccount.getUserId().equals(firebaseUser.getEmail())){
                        UserCredentials.onlineUsername=userAccount.getUserProfile();
                        UserCredentials.userImage=userAccount.getProfileImage();
                        UserCredentials.user_email=userAccount.getUserId();
                        UserCredentials.posts_number=userAccount.getPosts_number();
                        UserCredentials.follower_number=userAccount.getFollowers_number();
                        UserCredentials.following_number=userAccount.getFollowing_number();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                userPostList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post=snapshot.getValue(Post.class);
                    if(post.getUser_email().equals(UserCredentials.user_email)){
                        userPostList.add(post);
                    }
                    postList.add(post);
                }

            /*    PostList  */ adapter=new PostList(MainActivity.this,postList);
                postListView.setAdapter(adapter);
                postListView.setSelection(positionShow);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddPostActivity(){
        Intent intent=new Intent(MainActivity.this,AddPostPage.class);
        startActivity(intent);
    }
}
