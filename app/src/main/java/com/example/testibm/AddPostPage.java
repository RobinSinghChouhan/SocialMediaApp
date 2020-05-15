package com.example.testibm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;


public class AddPostPage extends AppCompatActivity {

    ImageView post_image;
    ImageView fetchedImage;

    Button post_button;
    Button postImage;
    EditText post_caption;
    private Uri ImageURI;
    DatabaseReference databasePost;
    StorageReference storageReference;
    private StorageTask UploadTask;
    private static final int PICK_IMAGE_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        setContentView(R.layout.activity_add_post_page);
        post_image=findViewById(R.id.post_image);
        post_button=findViewById(R.id.post_button);
        post_caption=findViewById(R.id.post_caption);
        databasePost= FirebaseDatabase.getInstance().getReference("Post");
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        postImage=findViewById(R.id.post_Image);

        fetchedImage=findViewById(R.id.fetchedImage);

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addPost();
            }
        });
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagePost();
            }
        });
    }
    private void selectImage(){
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
            ImageURI=data.getData();
            post_image.setImageURI(ImageURI);

        }
    }
    private void addPost(){
    }

    private void addImagePost(){
        if(ImageURI!=null){
            final String imageDownloadUrl=System.currentTimeMillis()+
                    "."+getFileExtension(ImageURI);
            final StorageReference fileReference=storageReference.child(imageDownloadUrl);
            final String username = UserCredentials.onlineUsername;
//            final String username=UserAccount.accountUser;
            final String caption = post_caption.getText().toString();


            UploadTask=fileReference.putFile(ImageURI).addOnSuccessListener(new OnSuccessListener
                    <com.google.firebase.storage.UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                 // databasePost.child("Post").setValue(fileReference.getDownloadUrl().toString());
                    Toast.makeText(AddPostPage.this,"Success!"+imageDownloadUrl,Toast.LENGTH_SHORT).show();
                    final String fetchDownloadUrl;
                    storageReference.child(imageDownloadUrl).getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            if (!TextUtils.isEmpty(username)) {
                                String imageUri=uri.toString();
                                String id = databasePost.push().getKey();
                                Post post = new Post(id, UserCredentials.onlineUsername, caption,imageUri,UserCredentials.userImage,
                                        UserCredentials.user_email,"0");
                                databasePost.child(id).setValue(post);
                                Toast.makeText(AddPostPage.this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                databasePost.child(databasePost.push().getKey()).setValue("test");
                                Toast.makeText(AddPostPage.this, "An error occured!", Toast.LENGTH_SHORT).show();
                            }
                            Picasso.get().load(uri.toString()).into(fetchedImage);
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
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
