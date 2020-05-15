package com.example.testibm;

public class Likes {
    String post_id;
    String user_id;
    String like_id;
    String tag;
    public Likes(String post_id,String user_id,String like_id,String tag){
        this.user_id=user_id;
        this.post_id=post_id;
        this.like_id=like_id;
        this.tag=tag;
    }
    public Likes(){}

    public String getLike_id() {
        return like_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTag() {
        return tag;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }
}
