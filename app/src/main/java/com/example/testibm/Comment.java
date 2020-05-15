package com.example.testibm;

public class Comment {
    String commentId;
    String comment;
    String user;
    String userImage;
    String user_email;
    public Comment(){

    }
    public Comment(String commentId,String comment,String user,String userImage,String user_email){
        this.commentId=commentId;
        this.comment=comment;
        this.user=user;
        this.userImage=userImage;
        this.user_email=user_email;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public String getUserImage() {
        return userImage;
    }
}
