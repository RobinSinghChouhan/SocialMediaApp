package com.example.testibm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class chatList extends ArrayAdapter<Chat> {
    private Activity context;
    private List<Chat> chat;

    public chatList(@NonNull Activity context, List<Chat> chat) {
        super(context, R.layout.chat_layout,chat);
        this.context = context;
        this.chat = chat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem;
        if(chat.get(position).from.equals(UserCredentials.user_email)){
            listViewItem=inflater.inflate(R.layout.chat_layout_from,null,true);
        }else{
            listViewItem=inflater.inflate(R.layout.chat_layout,null,true);
        }
        TextView textView=listViewItem.findViewById(R.id.textViewName);
        ImageView user_profile_pic=listViewItem.findViewById(R.id.user_profile_pic);
        Chat chats=chat.get(position);
        textView.setText(chats.getChat());
        if(chat.get(position).from.equals(UserCredentials.user_email)){

        }else{

            Picasso.get().load(chats.getFrom_Image()).into(user_profile_pic);
        }
        return listViewItem;
    }
}
