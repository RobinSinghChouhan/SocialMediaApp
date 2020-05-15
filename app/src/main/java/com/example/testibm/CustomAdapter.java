package com.example.testibm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.testibm.MainActivity.postList;
import static com.example.testibm.MainActivity.userPostList;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    public CustomAdapter(Context context, ArrayList<Post> postList){
        this.context=context;
    }
    @Override
    public int getCount() {
        return userPostList.size();
    }

    @Override
    public Object getItem(int position) {
        return userPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.post_images,null,true);
            holder.image_id=convertView.findViewById(R.id.image_id);
            convertView.setTag(holder);
        }else{

            holder=(ViewHolder)convertView.getTag();
        }
        final Post post=userPostList.get(position);
            Picasso.get().load(post.getImageUrl()).into(holder.image_id);

        return convertView;
    }
    public class ViewHolder{
        ImageView image_id;
    }
}
