package com.example.testibm;

public class Test {
    String name;
    String pwd;
    String uname;
     public Test(String name,String uname,String pwd){
         this.name=name;
         this.uname=uname;
         this.pwd=pwd;
     }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUname() {
        return uname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
