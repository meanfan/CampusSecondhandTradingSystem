package com.mean.csts.client;

import com.mean.csts.data.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class AccountPanel extends JPanel implements ActionListener {
    public InetAddress address;
    private Socket socket;
    public int port;
    private Box topBox, subBoxH1, subBoxH2, boxV1, boxV2;
    private JLabel labelUname, labelNickname, labelWallet;
    private JButton btnLogout, btnQuit;
    private User user;
    private String path;
    private Icon icon1 = null;
    private JLabel imageView;

    public AccountPanel(InetAddress address, int port) {
        this.user = user;
        boxV1 = Box.createVerticalBox();
        boxV1.add(new JLabel("用户名："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("昵称："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("钱包："));
        boxV2 = Box.createVerticalBox();
        labelUname = new JLabel();
        boxV2.add(labelUname);
        boxV2.add(Box.createVerticalStrut(16));
        labelNickname = new JLabel();
        boxV2.add(labelNickname);
        boxV2.add(Box.createVerticalStrut(16));
        labelWallet = new JLabel();
        boxV2.add(labelWallet);
        subBoxH1 = Box.createHorizontalBox();
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV1);
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV2);
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH2 = Box.createHorizontalBox();
        btnLogout = new JButton("注销退出");
        btnLogout.addActionListener(this);
        btnQuit = new JButton("    退出    ");
        btnQuit.addActionListener(this);
        subBoxH2.add(btnLogout);
        subBoxH2.add(Box.createHorizontalStrut(20));
        subBoxH2.add(btnQuit);
        topBox = Box.createVerticalBox();
        //topBox.add(Box.createVerticalStrut(100));
        topBox.add(subBoxH1);
        topBox.add(Box.createVerticalStrut(20));
        topBox.add(subBoxH2);
        add(topBox);
        validate();
        this.address = address;
        this.port = port;
    }

    private void showUserInfo() {
        if (user == null) {
            subBoxH1.setVisible(false);
            btnLogout.setVisible(false);
            btnQuit.setVisible(true);
        } else {
            labelUname.setText(user.getUname());
            labelNickname.setText(user.getNickname());
            labelWallet.setText(String.valueOf(user.getWallet()));
            subBoxH1.setVisible(true);
            btnLogout.setVisible(true);
            btnQuit.setVisible(false);
        }
    }
    public void setUser(User user) {
        this.user = user;
        showUserInfo();
    }

    public static void logout(InetAddress address,int port,String uname) {
        //TODO 登出
        Socket socket = null;
        try {
            socket = new Socket(address, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("$LogoutRequest$"); //请求类型
            out.writeUTF(uname);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnQuit) {
            if (user == null) {
                System.exit(0);
            } else {
                logout(address,port,user.getUname());
                System.exit(0);
            }
        } else if (e.getSource() == btnLogout) {
            logout(address,port,user.getUname());
            System.exit(0);
        }
    }
}