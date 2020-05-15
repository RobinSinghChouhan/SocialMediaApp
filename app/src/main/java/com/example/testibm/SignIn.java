package com.example.testibm;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.Window;
        import android.view.WindowManager;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;

public class SignIn extends AppCompatActivity implements View.OnKeyListener{
    EditText editText;
    EditText editText2;
    TextView textView;
    private TextView SignUp;
    Button button;
    FirebaseAuth mFirebaseAuth;
    ImageView test;
    LinearLayout SignInBackground;
    ArrayList<UserAccount> users;
    DatabaseReference databaseUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            signIn();
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        setContentView(R.layout.activity_sign_in);
        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        textView=findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });
        button=findViewById(R.id.button);
        SignUp=findViewById(R.id.SignUp);
        mFirebaseAuth=FirebaseAuth.getInstance();
        databaseUser= FirebaseDatabase.getInstance().getReference("Users");
        users=new ArrayList<>();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    Toast.makeText(SignIn.this, "You are logged In", Toast.LENGTH_SHORT).show();

//                    Intent i=new Intent(SignIn.this,MainActivity.class);
//                    startActivity(i);
                    moveOn();
                }else{
                    Toast.makeText(SignIn.this,"Please Sign In",Toast.LENGTH_SHORT).show();
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity();
            }
        });
    }
    private void signUpActivity(){
        Intent intent=new Intent(SignIn.this,SignUp.class);
        startActivity(intent);
    }
    private void signIn(){
        final String email=editText.getText().toString();
        String pwd=editText2.getText().toString();
        if(email.isEmpty()){
            editText.setError("Please enter email");
            editText.requestFocus();
        }else if(pwd.isEmpty()){
            editText2.setError("Please enter password");
            editText2.requestFocus();
        }else if(email.isEmpty() && pwd.isEmpty()){
            Toast.makeText(this,"Fields are empty",Toast.LENGTH_SHORT).show();
        }else if(!(email.isEmpty() && pwd.isEmpty())){
                mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignIn.this,"Login Error, Please try again",Toast.LENGTH_SHORT).show();

                           // Intent intent=new Intent(SignIn.this,MainActivity.class);
                           // startActivity(intent);

                        }else{
                            for(int i=0;i<users.size();i++){
                                if(users.get(i).getUserId().equals(email)){
                                    UserCredentials.userImage=users.get(i).getProfileImage();
                                    UserCredentials.onlineUsername=users.get(i).getUserProfile();
                                }
                            }
                        //    accessUserAccount();
//                            Handler handler=new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Intent intent=new Intent(SignIn.this,MainActivity.class);
//                                    //  Intent intent=new Intent(SignIn.this,UserProfileActivity.class);
//                                    startActivity(intent);
//                                }
//                            },1000);
                            moveOn();
                        }
                    }
                });
        }
    }

    private void moveOn(){
   //     Handler handler=new Handler();
    //    handler.postDelayed(new Runnable() {
      //      @Override
      //      public void run() {
                Intent intent=new Intent(SignIn.this,MainActivity.class);
                //  Intent intent=new Intent(SignIn.this,UserProfileActivity.class);
                startActivity(intent);
      //      }
      //  },1000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(authStateListener);
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserAccount userAccount=snapshot.getValue(UserAccount.class);
                    users.add(userAccount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

