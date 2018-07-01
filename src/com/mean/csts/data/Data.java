package com.mean.csts.data;

import java.io.*;

public class Data implements Serializable {
    public static final int TYPE_FAILURE = 0;
    public static final int TYPE_SUCCESS = 1;

    public static final int TYPE_REGISTER = 10;
    public static final int TYPE_REGISTER_FAILURE =11;
    public static final int TYPE_LOGIN = 20;
    public static final int TYPE_LOGIN_FAILURE = 21;


    private int type;
    private String name;
    private String content;
    public Data(int type)
    {
        this.type = type;
    }
    public Data(int type,String name,String content) {
        this(type);
        this.name = name;
        this.content = content;
    }
    public int getType()
    {
        return type;
    }
    public String getName()
    {
        return name;
    }
    public String getContent()
    {
        return content;
    }
    public byte[] toByte() {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo;
        try {
            oo = new ObjectOutputStream(bo);
            oo.writeObject(this);
        } catch (IOException e) {e.printStackTrace();}
        return bo.toByteArray();
    }
    public static Data toData(byte b[]) {
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream oi;
        try {
            oi = new ObjectInputStream(bi);
            return (Data)oi.readObject();
        } catch (ClassNotFoundException e) {e.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
        return null;
    }
}
