package com.example.testibm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.testibm.MainActivity.POST_CAPTION;
import static com.example.testibm.MainActivity.POST_ID;
import static com.example.testibm.MainActivity.POST_NAME;
import static com.example.testibm.MainActivity.positionShow;
import static com.example.testibm.MainActivity.postList;

public class PostList extends BaseAdapter{
    private Context context;
    FirebaseAuth mFirebaseAuth=FirebaseAuth.getInstance();
    public static ArrayList<Likes> likes=new ArrayList<>();

    public static int number_of_likes=0;
    public static int initial=0;
    DatabaseReference databaseLikes= FirebaseDatabase.getInstance().getReference("Likes");
    DatabaseReference databasePost=FirebaseDatabase.getInstance().getReference("Post");
    public PostList(Context context, ArrayList<Post> postList){
        this.context=context;
    }
    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
if(convertView==null){
holder=new ViewHolder();
LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
convertView=inflater.inflate(R.layout.post,null,true);
holder.addCommentNow=convertView.findViewById(R.id.commentButton);

holder.like_button=convertView.findViewById(R.id.like_button);
holder.likes=convertView.findViewById(R.id.likes);

holder.textViewName=convertView.findViewById(R.id.textViewName);
holder.textViewCaption=convertView.findViewById(R.id.textViewCaption);
holder.imageViewPost=convertView.findViewById(R.id.display_post_image);
holder.userProfile=convertView.findViewById(R.id.userProfile);

convertView.setTag(holder);
}else{
holder=(ViewHolder)convertView.getTag();
}
holder.addCommentNow.setTag(R.integer.btn_comment_view,convertView);
holder.addCommentNow.setTag(R.integer.btn_comment_pos,position);

        final Post post=postList.get(position);
        holder.textViewName.setText(post.getUsername());
        holder.textViewCaption.setText(post.getCaption());
        Picasso.get().load(post.getImageUrl()).into(holder.imageViewPost);
        Picasso.get().load(post.getUserProfile()).into(holder.userProfile);
        holder.likes.setText(post.getLikes());


        holder.addCommentNow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Post post=postList.get(position);
        Intent intent=new Intent(context.getApplicationContext(),AddCommentActivity.class);
        intent.putExtra(POST_NAME,post.getUsername());
        intent.putExtra(POST_CAPTION,post.getCaption());
        intent.putExtra(POST_ID,post.getPostId());
        context.startActivity(intent);
        positionShow=position;
    }
});

        holder.like_button.setBackgroundResource(R.drawable.like);
        holder.like_button.setTag(R.integer.btn_like_pos,position);
        holder.like_button.setTag(R.integer.btn_like_view,convertView);

        for(int i=0;i<likes.size();i++){
            if(likes.get(i).getPost_id().equals(post.getPostId())){
                if(likes.get(i).getUser_id().equals(UserCredentials.user_email)){
                    holder.like_button.setBackgroundResource(R.drawable.liked);
                    notifyDataSetChanged();
                }
            }
        }


        holder.like_button.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        positionShow=position;
        number_of_likes=Integer.parseInt(post.getLikes());

        View tempView = (View) holder.like_button.getTag(R.integer.btn_like_view);
        Integer pos=(Integer)holder.like_button.getTag(R.integer.btn_like_pos);
        TextView tv=tempView.findViewById(R.id.likes);

        Button bt=tempView.findViewById(R.id.like_button);

            boolean user_exist=false;
            boolean post_exist=false;
        for(int i=0;i<likes.size();i++){
            if(likes.get(i).getPost_id().equals(post.getPostId())){
                post_exist=true;
                if(likes.get(i).getUser_id().equals(UserCredentials.user_email)){
                    user_exist=true;
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Likes").child(likes.get(i).getLike_id());
                    number_of_likes=number_of_likes-1;
                    databaseReference.removeValue();
                }else{
                }
            }
        }
        if(!post_exist || !user_exist){
            String id=databaseLikes.push().getKey();
            Likes likes=new Likes(post.getPostId(),UserCredentials.user_email,id,"1");
            databaseLikes.child(id).setValue(likes);
            number_of_likes=number_of_likes+1;
        }

        Post post1=new Post(post.getPostId(),
                                        post.getUsername(),
                                        post.getCaption(),
                                        post.getImageUrl(),
                                        post.getUserProfile(),
                                        post.getUser_email(),
                                        String.valueOf(number_of_likes));
                    databasePost.child(post.getPostId()).setValue(post1);
        tv.setText(String.valueOf(number_of_likes));
    }

});
        databaseLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Likes like_obj= snapshot.getValue(Likes.class);
                    likes.add(like_obj);
                    for(int i=0;i<likes.size();i++){
                        if(likes.get(i).getPost_id().equals(post.getPostId())){
                            number_of_likes=number_of_likes+1;
                        }
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return convertView;
    }
    public class ViewHolder{
        Button addCommentNow,like_button;
        TextView textViewName,textViewCaption,likes;
        ImageView imageViewPost;
        ImageView userProfile;
    }
}
