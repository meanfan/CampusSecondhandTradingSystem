package com.mean.csts.client.gui.panel;

import com.mean.csts.client.gui.frame.BasicWin;
import com.mean.csts.client.gui.listener.AccountListener;
import com.mean.csts.data.User;

import javax.swing.*;
import java.net.InetAddress;
import java.net.Socket;

public class AccountPanel extends JPanel{
    private static AccountPanel instance = null;
    public InetAddress address;
    private Socket socket;
    public int port;
    public Box topBox, subBoxH1, subBoxH2, boxV1, boxV2;
    public JLabel labelUname, labelNickname, labelWallet;
    public JButton btnLogout, btnQuit;
    public User user;
    private String path;
    private Icon icon1 = null;
    public JLabel imageView;

    public static AccountPanel getInstance() {
        if(instance == null){
            instance = new AccountPanel();
        }
        return instance;
    }
    private AccountPanel() {
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
        btnLogout.addActionListener(new AccountListener());
        btnQuit = new JButton("    退出    ");
        btnQuit.addActionListener(new AccountListener());
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
        this.address = BasicWin.ADDRESS;
        this.port = BasicWin.PORT;
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

}