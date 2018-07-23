package com.mean.csts.client.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;

import com.mean.csts.client.gui.frame.BasicWin;
import com.mean.csts.client.gui.listener.GoodsStockListener;
import com.mean.csts.data.User;

public class GoodsStockPanel extends JPanel{
    private static GoodsStockPanel instance;
    public InetAddress address;
    public int port;
    public Socket socket;
    private Box baseBox,subBoxH0,subBoxH1,subBoxH2,boxV0,boxV1,boxV2;
    public JTextField tfGname, tfGnum,tfGprice,tfGcontent;
    public JButton btnRelease,btnOpen;
    public User user;
    public String path;
    public Icon icon1=null;
    public JLabel imageView;

    public static GoodsStockPanel getInstance() {
        if(instance == null){
            instance = new GoodsStockPanel();
        }
        return instance;
    }
    private GoodsStockPanel(){
        super();
        boxV0=Box.createVerticalBox();
        boxV0.add(new JLabel("图片："));
        boxV1 = Box.createVerticalBox();
        boxV1.add(new JLabel("商品名："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("数量："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("价格："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("描述："));
        boxV2 = Box.createVerticalBox();
        tfGname = new JTextField(16);
        tfGname.setText("");
        boxV2.add(tfGname);
        boxV2.add(Box.createVerticalStrut(16));
        tfGnum = new JTextField(16);
        tfGnum.setText("");
        boxV2.add(tfGnum);
        boxV2.add(Box.createVerticalStrut(16));
        tfGprice = new JTextField(16);
        tfGprice.setText("");
        boxV2.add(tfGprice);
        boxV2.add(Box.createVerticalStrut(16));
        tfGcontent = new JTextField(16);
        tfGcontent.setText("");
        boxV2.add(tfGcontent);
        subBoxH0=Box.createHorizontalBox();
        subBoxH0.add(Box.createHorizontalStrut(20));
        subBoxH0.add(boxV0);
        btnOpen=new JButton("选择图片");
        GoodsStockListener goodsStockListener = new GoodsStockListener();
        btnOpen.addActionListener(goodsStockListener);
        imageView = new JLabel();
        imageView.setPreferredSize(new Dimension(128,128));
        imageView.setIcon(new ImageIcon("src/com/mean/csts/client/default.jpg"));
        subBoxH0.add(imageView);
        subBoxH0.add(btnOpen);
        subBoxH1 = Box.createHorizontalBox();
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV1);
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV2);
        subBoxH2 = Box.createHorizontalBox();
        btnRelease = new JButton("发布");
        btnRelease.addActionListener(goodsStockListener);
        subBoxH2.add(btnRelease);
        subBoxH2.add(Box.createHorizontalStrut(20));
        baseBox = Box.createVerticalBox();
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH0);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH1);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH2);
        baseBox.add(Box.createVerticalStrut(100));
        add(baseBox);
        validate();
        this.address = BasicWin.ADDRESS;
        this.port = BasicWin.PORT;
    }
    public void setUser(User user){this.user=user;}


}
