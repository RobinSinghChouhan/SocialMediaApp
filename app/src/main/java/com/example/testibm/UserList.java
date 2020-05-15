package com.example.testibm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.testibm.MainActivity.postList;
import static com.example.testibm.MainActivity.userPostList;
import static com.example.testibm.UserListPage.userList;


public class UserList extends BaseAdapter {
    private Context context;
    public UserList(Context context, ArrayList<UserAccount> userList){
        this.context=context;
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Viewholder holder;
        if(convertView==null){
            holder=new Viewholder();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.user_list,null,true);
            holder.userProfile=convertView.findViewById(R.id.user_profile_pic);
            holder.textViewName=convertView.findViewById(R.id.textViewName);
            holder.follow=convertView.findViewById(R.id.follow);

            convertView.setTag(holder);
        }else{
            holder=(Viewholder)convertView.getTag();
        }

        final UserAccount userAccount=userList.get(position);
        holder.textViewName.setText(userAccount.getUserProfile());
        Picasso.get().load(userAccount.getProfileImage()).into(holder.userProfile);

        holder.follow.setTag(R.integer.btn_follow_pos,position);
        holder.follow.setTag(R.integer.btn_like_view,convertView);
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tempView=(View)holder.follow.getTag(R.integer.btn_follow_view);
                Integer pos=(Integer)holder.follow.getTag(R.integer.btn_follow_pos);
             //   Toast.makeText(getApplicationContext(),""+userList.get(position).getUserId(),Toast.LENGTH_SHORT).show();
                getUserInfo(userList.get(position).getUserId());
                Intent intentUser=new Intent(context.getApplicationContext(),UserProfileActivity.class);
                intentUser.putExtra("User_Image",userList.get(position).getProfileImage());
                intentUser.putExtra("User_posts",userList.get(position).getPosts_number());
                intentUser.putExtra("User_followers",userList.get(position).getFollowers_number());
                intentUser.putExtra("User_following",userList.get(position).getFollowing_number());
                intentUser.putExtra("User_name",userList.get(position).getUserProfile());
                intentUser.putExtra("User_email",userList.get(position).getUserId());
                context.startActivity(intentUser);
                holder.follow.setText("Done");
            }
        });
        return convertView;
    }

    public class Viewholder{
        private ImageView userProfile;
        private Button follow;
        private TextView textViewName;
    }

    public void getUserInfo(String user_id){
        userPostList.clear();
        for(int i=0;i<postList.size();i++){
            if(postList.get(i).getUser_email().equals(user_id)){
                Post post=postList.get(i);
                userPostList.add(post);
            }
        }
    }
}
