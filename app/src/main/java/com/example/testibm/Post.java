package com.example.testibm;

public class Post {
    String postId;
    String username;
    String caption;
    public Post(){

    }
    public Post(String postId,String username,String caption){
        this.postId=postId;
        this.username=username;
        this.caption=caption;
    }

    public String getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public String getCaption() {
        return caption;
    }
}
