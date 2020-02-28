package com.example.testibm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public class PostList extends ArrayAdapter<Post> {
    private Activity context;
    private List<Post> postList;

    public PostList(Activity context,List<Post> postList){
        super(context,R.layout.post,postList);
        this.context=context;
        this.postList=postList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View listViewItem=inflater.inflate(R.layout.post,null,true);
        TextView textViewName=listViewItem.findViewById(R.id.textViewName);
        TextView textViewCaption=listViewItem.findViewById(R.id.textViewCaption);
        Post post=postList.get(position);
        textViewName.setText(post.getUsername());
        textViewCaption.setText(post.getCaption());

        return listViewItem;

    }
}
