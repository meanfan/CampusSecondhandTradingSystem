package com.mean.csts;

import javax.swing.*;
import java.awt.*;

public class Goods {
    public int gid;
    public String name;
    public int amount;
    public ImageIcon image;
    public String content;
    public double price;
    public Goods(){
        gid = -1;
        name = "unknown";
        amount = 0;
        image = null;
        content = "null";
    }
    public Goods(String name,int amount){
        this();
        this.name = name;
        this.amount = amount;
    }
}
