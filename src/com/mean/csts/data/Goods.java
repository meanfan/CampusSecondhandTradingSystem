package com.mean.csts.data;

public class Goods{
    public int gid;
    public String name;
    public int amount;
    public byte[] image;
    public String content;
    public double price;
    public int uid;
    public Goods(){
        gid = -1;
        name = "unknown";
        amount = 0;
        image = null;
        content = "null";
        uid = -1;
    }

    public int getGid() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public byte[] getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public double getPrice() {
        return price;
    }

    public int getUid() {
        return uid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setImage(byte[] image) {
        this.image = new byte[3145728];
        System.arraycopy(image,0,this.image,0,image.length);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return gid+"|"+name+"|"+amount+"|"+content+"|"+price+"|"+uid;
    }
}
