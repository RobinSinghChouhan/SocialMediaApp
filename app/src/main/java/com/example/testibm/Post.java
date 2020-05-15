package com.example.testibm;

import android.provider.ContactsContract;

public class Post {
    String postId;
    String username;
    String caption;
    String imageUrl;
    String userProfile;
    String user_email;
    String likes;

    public Post(){

    }

    public Post(String postId,String username,String caption,String ImageUrl,String userProfile,String user_email,String likes){
        this.postId=postId;
        this.username=username;
        this.caption=caption;
        this.imageUrl= ImageUrl;
        this.userProfile=userProfile;
        this.user_email=user_email;
        this.likes=likes;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
