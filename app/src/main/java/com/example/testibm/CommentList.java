package com.example.testibm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentList extends ArrayAdapter<Comment> {
    private Activity context;
    private List<Comment> comments;

    public CommentList(Activity context, List<Comment> comments) {
        super(context, R.layout.comment_list,comments);
        this.context=context;
        this.comments=comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
            View listViewItem ;//= inflater.inflate(R.layout.comment_list, null, true);
             listViewItem = inflater.inflate(R.layout.comment_list, null, true);
        TextView textViewName=listViewItem.findViewById(R.id.textViewName);
        //TextView textViewComment=listViewItem.findViewById(R.id.textViewComment);
        CircleImageView user_profile_pic=listViewItem.findViewById(R.id.user_profile_pic);
        Comment comment=comments.get(position);
        textViewName.setText(comment.getUser()+" "+comment.getComment());
     //   Collections.reverse(comments);
        Picasso.get().load(comment.getUserImage()).into(user_profile_pic);
        //textViewComment.setText(comment.getComment());
        return listViewItem;
    }
}
