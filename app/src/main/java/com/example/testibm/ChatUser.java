package com.example.testibm;

public class ChatUser {
    String id;
    String chat_user_name;
    public ChatUser(){

    }
    public ChatUser(String id,String chat_user_name){
        this.id=id;
        this.chat_user_name=chat_user_name;
    }

    public String getId() {
        return id;
    }

    public String getChat_user_name() {
        return chat_user_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChat_user_name(String chat_user_name) {
        this.chat_user_name = chat_user_name;
    }
}
