package com.example.testibm;

public class Chat {
    String id;
    String chat;
    String from;
    String from_Image;
    public Chat(){

    }
    public Chat(String id,String chat,String from,String from_Image){
        this.id=id;
        this.chat=chat;
        this.from=from;
        this.from_Image=from_Image;
    }

    public String getId() {
        return id;
    }

    public String getChat() {
        return chat;
    }

    public String getFrom() {
        return from;
    }

    public String getFrom_Image() {
        return from_Image;
    }

    public void setFrom_Image(String from_Image) {
        this.from_Image = from_Image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
