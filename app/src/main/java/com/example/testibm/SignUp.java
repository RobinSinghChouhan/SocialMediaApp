package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {
    private Button SignUp;
    private TextView SignIn;
    private EditText account_user;
    private ImageView profile_image;
    private EditText username;
    private EditText password;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mdatabaseUser;
    StorageReference UserDp;
    private Uri imageUri;
    private StorageTask UploadDp;
    String profile_username;
    String profile_password;
    String profile_name;
    private static final int PICK_IMAGE_REQUEST=1;

    private Button Test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SignUp=findViewById(R.id.SignUp);
        SignIn=findViewById(R.id.SignIn);
        profile_image=findViewById(R.id.profile_image);
        account_user=findViewById(R.id.account_user);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);


        Test=findViewById(R.id.Test);
        mdatabaseUser=FirebaseDatabase.getInstance().getReference("Users");
        UserDp= FirebaseStorage.getInstance().getReference("Users");
        mFirebaseAuth=FirebaseAuth.getInstance();
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInActivity();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDp();
            }
        });
    }
    private void signInActivity(){
        Intent intent=new Intent(SignUp.this,SignIn.class);
        startActivity(intent);
    }
    private void signUp(){
        final String email=username.getText().toString();
        final String pwd=password.getText().toString();
        final String User=account_user.getText().toString();
        profile_username=email;
        profile_password=pwd;
        profile_name=User;

        if(email.isEmpty()){
            username.setError("Please enter email");
            username.requestFocus();
        }else if(pwd.isEmpty()){
            password.setError("Please enter password");
            password.requestFocus();
        }else if(email.isEmpty() && pwd.isEmpty()){
            Toast.makeText(this,"Fields are empty",Toast.LENGTH_SHORT).show();
        }else if(!(email.isEmpty() && pwd.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
 //                   if(!task.isSuccessful()){
//                        Toast.makeText(SignUp.this,"Sign Up unsuccessful, Please try again later!",Toast.LENGTH_SHORT).show();
//                    }else{
//                        String id=mdatabaseUser.push().getKey();
//                        mdatabaseUser.child(id).setValue("yooooo");
//                        startActivity(new Intent(SignUp.this,MainActivity.class));
//                    }
                    addDp();
//                    Test test=new Test(profile_name,profile_username,profile_password);
//                    String id=mdatabaseUser.push().getKey();
//                    Log.i("KEEEEYEYYEYEYEYYE",id);
//                    mdatabaseUser.child(id).setValue(test);
//                    Toast.makeText(SignUp.this,"Succccccccccc",Toast.LENGTH_SHORT).show();
                }

            });
        }else{
                Toast.makeText(SignUp.this,"Error occured!",Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadProfileImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
        && data!=null && data.getData()!=null){
            imageUri=data.getData();
            profile_image.setImageURI(imageUri);
        }
    }

   private String getFileExtension(Uri uri){
       ContentResolver contentResolver=getContentResolver();
       MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
   }

    private void addDp(){
        if(imageUri!=null){
            final String imageDownload=System.currentTimeMillis()+"."+getFileExtension(imageUri);
            final StorageReference storageReference=UserDp.child(imageDownload);
            UploadDp=storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SignUp.this,"Success!",Toast.LENGTH_SHORT).show();
                    UserDp.child(imageDownload).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //   signUp();
                          //  UserCredentials.onlineUsername=profile_name;
                          //  UserCredentials.userImage=uri.toString();
                                 UserAccount ua=new UserAccount(profile_username,profile_password,
                                         profile_name,uri.toString(),"0","0","0");
                            //UserAccount ua=new UserAccount("user","pass","name",uri.toString());
                            String id=mdatabaseUser.push().getKey();
                            mdatabaseUser.child(id).setValue(ua);
                            Toast.makeText(SignUp.this,"Database Success! \n"+ua.getUserProfile(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
            }
}
