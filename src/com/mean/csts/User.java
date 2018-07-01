package com.mean.csts;

public class User {
    private int uid;
    private int type;
    private String uname;
    private String nickname;
    private String pwd;
    public User(){
        this.uid = -1;
        this.type = -1;
        this.uname = "null";
        this.nickname = "null";
        this.pwd = "";
    }
    User(int uid,int type,String uname,String pwd){
        this.uid = uid;
        this.uname = uname;
        this.pwd = pwd;
    }
    public int getUid(){return uid;}
    public int getType(){return type;}
    public String getUname(){return uname;}
    public String getNickname(){return nickname;}
    public String getPwd(){return pwd;}

    public void setUid(int uid){
        this.uid = uid;
    }
    public void setType(int type){
        this.type = type;
    }
    public void setUname(String uname){
        this.uname = uname;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public boolean setPwd(String pwd){
        if(this.pwd.compareTo("")==0){
            this.pwd = pwd;
            return true;
        }
        return false;
    }

    public boolean changePwd(String old_pwd,String new_pwd)
    {
        if(old_pwd.compareTo(this.pwd) == 0){
            pwd = new_pwd;
            return true;
        }
        return false;
    }

}
