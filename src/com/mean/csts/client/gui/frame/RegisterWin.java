package com.mean.csts.client.gui.frame;

import com.mean.csts.client.gui.listener.RegisterListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class RegisterWin extends BasicWin{
    private static RegisterWin instance = null;
    private static String title = "注册";
    private static int winWedth = 400;
    private static int winHeight = 350;
    private Box baseBox,subBoxH1,subBoxH2,boxV1,boxV2;
    public JTextField tfUname, tfNickname, tfPwd, tfRepPwd;
    public JButton btnSubmit, btnClose;
    public InetAddress address;
    public int port;
    public Socket socket;
    public static RegisterWin getInstance() {
        if(instance == null){
            instance = new RegisterWin();
        }
        return instance;
    }
    public RegisterWin(){
        super(title,winWedth,winHeight);
        boxV1 = Box.createVerticalBox();
        boxV1.add(new JLabel("用户名："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("昵称："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("密码："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("确认密码："));
        boxV2 = Box.createVerticalBox();
        tfUname = new JTextField(16);
        tfUname.setText("");
        boxV2.add(tfUname);
        boxV2.add(Box.createVerticalStrut(16));
        tfNickname = new JTextField(16);
        tfNickname.setText("");
        boxV2.add(tfNickname);
        boxV2.add(Box.createVerticalStrut(16));
        tfPwd = new JPasswordField(32);
        tfPwd.setText("");
        boxV2.add(tfPwd);
        boxV2.add(Box.createVerticalStrut(16));
        tfRepPwd = new JPasswordField(16);
        tfRepPwd.setText("");
        boxV2.add(tfRepPwd);

        subBoxH1 = Box.createHorizontalBox();
        subBoxH1.add(boxV1);
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV2);
        subBoxH2 = Box.createHorizontalBox();
        btnSubmit = new JButton("注册");
        btnSubmit.addActionListener(new RegisterListener());
        btnClose = new JButton("关闭");
        btnClose.addActionListener(new RegisterListener());
        subBoxH2.add(btnSubmit);
        subBoxH2.add(Box.createHorizontalStrut(20));
        subBoxH2.add(btnClose);
        baseBox = Box.createVerticalBox();
        baseBox.add(Box.createVerticalStrut(100));
        baseBox.add(subBoxH1);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH2);
        baseBox.add(Box.createVerticalStrut(150));
        add(baseBox);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        address = super.ADDRESS;
        port = super.PORT;


    }
    public static void main(String[] args) {
        new RegisterWin();
    }

}
