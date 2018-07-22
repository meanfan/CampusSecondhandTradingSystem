package com.mean.csts.client.gui.frame;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;

import com.mean.csts.client.gui.listener.GuestListener;
import com.mean.csts.client.gui.listener.LoginListener;
import com.mean.csts.client.gui.listener.toRegisterListener;

public class LoginWin extends BasicWin {
    private static LoginWin instance = null;
    private static String title = "校园二手商品交易平台-登录";
    private static int winWedth = 400;
    private static int winHeight = 380;
    private InetAddress address;
    private int port;
    private Box topBox, subBoxH1,subBoxH2, subBoxH3,boxV1,boxV2;
    public JTextField tfUname, tfPwd;
    public JButton btnLogin,btnRegister,btnGuest;
    public static LoginWin getInstance() {
        if(instance == null){
            instance = new LoginWin();
        }
        return instance;
    }
    private LoginWin(){
        super(title,winWedth,winHeight);
        subBoxH1 = Box.createHorizontalBox();
        JLabel pic = new JLabel();
        pic.setPreferredSize(new Dimension(400,200));
        pic.setIcon(new ImageIcon("src/com/mean/csts/client/res/pic/loginPic.jpg"));
        subBoxH1.add(pic);
        boxV1 = Box.createVerticalBox();
        boxV1.add(new JLabel("用户名："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("密码："));
        boxV2 = Box.createVerticalBox();
        tfUname = new JTextField(16);
        tfUname.setText("");
        boxV2.add(tfUname);
        boxV2.add(Box.createVerticalStrut(16));
        tfPwd = new JPasswordField(16);
        tfPwd.setText("");
        boxV2.add(tfPwd);
        subBoxH2 = Box.createHorizontalBox();
        subBoxH2.add(Box.createHorizontalStrut(20));
        subBoxH2.add(boxV1);
        subBoxH2.add(Box.createHorizontalStrut(20));
        subBoxH2.add(boxV2);
        subBoxH2.add(Box.createHorizontalStrut(20));
        subBoxH3 = Box.createHorizontalBox();
        btnLogin = new JButton("        登录        ");
        getRootPane().setDefaultButton(btnLogin);
        btnLogin.addActionListener(new LoginListener());
        btnRegister = new JButton("注册");
        btnRegister.addActionListener(new toRegisterListener());
        btnGuest = new JButton("游客");
        btnGuest.addActionListener(new GuestListener());
        subBoxH3.add(btnLogin);
        subBoxH3.add(Box.createHorizontalStrut(20));
        subBoxH3.add(btnRegister);
        subBoxH3.add(Box.createHorizontalStrut(20));
        subBoxH3.add(btnGuest);
        topBox = Box.createVerticalBox();
        //topBox.add(Box.createVerticalStrut(100));
        topBox.add(subBoxH1);
        topBox.add(Box.createVerticalStrut(20));
        topBox.add(subBoxH2);
        topBox.add(Box.createVerticalStrut(20));
        topBox.add(subBoxH3);
        topBox.add(Box.createVerticalStrut(20));
        add(topBox);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        address = super.ADDRESS;
        this.port = super.PORT;
        //this.setVisible(false);
    }
    public static void main(String[] args) {
        LoginWin.getInstance().setVisible(true);
        LoginWin.getInstance().validate();
    }
    public void close() {
        System.exit(0);
    }


}
