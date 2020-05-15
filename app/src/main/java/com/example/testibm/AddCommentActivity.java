package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddCommentActivity extends AppCompatActivity {
    TextView textViewUser;
    TextView textView;
    EditText editTextComment;
    ListView listViewComments;
    List<Comment> comments;
    DatabaseReference databaseComments;
    Button addComent;
    Button backButton;
    CommentList commentListAdapter;
    String postToDeleteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        setContentView(R.layout.activity_add_comment);
        textViewUser=findViewById(R.id.textViewUser);
        textView=findViewById(R.id.textView);
        editTextComment=findViewById(R.id.editTextComment);

        editTextComment.setHint("Comment as "+UserCredentials.onlineUsername);

        listViewComments=findViewById(R.id.listViewComments);
        addComent=findViewById(R.id.buttonAddComment);
        backButton=findViewById(R.id.backButton);
        comments=new ArrayList<>();
        Intent intent=getIntent();
        String id=intent.getStringExtra(MainActivity.POST_ID);
        String name=intent.getStringExtra(MainActivity.POST_NAME);
        String caption=intent.getStringExtra(MainActivity.POST_CAPTION);
        postToDeleteId=id;
        textViewUser.setText(name);
        textView.setText(caption);
        databaseComments= FirebaseDatabase.getInstance().getReference("comments").child(id);
        addComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivity();
            }
        });
        listViewComments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                deleteActivity(position);
                return true;
            }
        });
    }

    public void deleteActivity(final int position){
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("You sure to delete this comment?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("comments").child(postToDeleteId).child(comments.get(position).getCommentId());
                        databaseReference.removeValue();
                        commentListAdapter.notifyDataSetChanged();
                        Toast.makeText(AddCommentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            comments.clear();
            for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()){
            Comment comment=commentSnapshot.getValue(Comment.class);
            comments.add(comment);
            }
             /*   CommentList   */ commentListAdapter=new CommentList(AddCommentActivity.this,comments);
                listViewComments.setAdapter(commentListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveComment(){
 //   String commentUser=editTextName.getText().toString();
        String commentUser=UserCredentials.onlineUsername;
    String commentAdded=editTextComment.getText().toString();
    if(!TextUtils.isEmpty(commentUser)){
        String id=databaseComments.push().getKey();
        Comment comment=new Comment(id,commentAdded,commentUser,UserCredentials.userImage,UserCredentials.user_email);
        databaseComments.child(id).setValue(comment);
        Toast.makeText(this,"Comment added Successfully!",Toast.LENGTH_SHORT).show();
    }else{
        Toast.makeText(this,"An error occured!",Toast.LENGTH_SHORT).show();
    }
    editTextComment.setText("");
    }

    private void postActivity(){
        Intent intent=new Intent(AddCommentActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
