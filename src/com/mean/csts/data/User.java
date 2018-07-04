package com.mean.csts.data;

public class User {
    private int uid;
    private String type;
    private String uname;
    private String nickname;
    private String pwd;
    private int token;
    private String status;
    private Double wallet;
    public User(){
        this.uid = -1;
        this.type = UserRegistered.Type;
        this.uname = "null";
        this.nickname = "null";
        this.pwd = "";
        this.token = 0;
        this.status = "offline";
        this.wallet = 0.0;
    }
    public User(int uid,String type,String uname,String pwd){
        this.uid = uid;
        this.type = type;
        this.uname = uname;
        this.pwd = pwd;
    }
    public int getUid(){return uid;}
    public String getType(){return type;}
    public String getUname(){return uname;}
    public String getNickname(){return nickname;}
    public String getPwd(){return pwd;}
    public int getToken(){return token;}
    public String getStatus(){return status;}
    public Double getWallet(){return wallet;}

    public void setUid(int uid){
        this.uid = uid;
    }
    public void setType(String type){
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
    public void setToken(int token){this.token = token; }
    public void setStatus(String status){this.status=status; }
    public void setWallet(Double wallet){this.wallet = wallet;}

    public boolean changePwd(String old_pwd,String new_pwd)
    {
        if(old_pwd.compareTo(this.pwd) == 0){
            pwd = new_pwd;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getUid()+"|"+ getType()+"|"+getUname()+"|"+getNickname()+"|"+getPwd()+"|"+getToken();
    }
}
