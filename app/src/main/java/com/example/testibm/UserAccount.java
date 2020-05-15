package com.example.testibm;

public class UserAccount {
    String userId;
    String password;
    String userProfile;
    String profileImage;
    String posts_number;
    String followers_number;
    String following_number;

    public UserAccount(String userId,String password,String userProfile,String profileImage,
                       String posts_number,String followers_number,String following_number){
        this.userId=userId;
        this.password=password;
        this.userProfile=userProfile;
        this.profileImage=profileImage;
        this.posts_number=posts_number;
        this.followers_number=followers_number;
        this.following_number=following_number;
    }
    public UserAccount(){

    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getPosts_number() {
        return posts_number;
    }

    public String getFollowers_number() {
        return followers_number;
    }

    public String getFollowing_number() {
        return following_number;
    }

    public void setPosts_number(String posts_number) {
        this.posts_number = posts_number;
    }

    public void setFollowers_number(String followers_number) {
        this.followers_number = followers_number;
    }

    public void setFollowing_number(String following_number) {
        this.following_number = following_number;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
