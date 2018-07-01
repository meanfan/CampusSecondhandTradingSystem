package com.mean.csts.client;

import com.mean.csts.User;
import com.mean.csts.UserRegistered;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RegisterWin extends BasicWin implements ActionListener {
    public static String title = "注册";
    public static int winWedth = 400;
    public static int winHeight = 350;
    private Box baseBox,subBoxH1,subBoxH2,boxV1,boxV2;
    private JTextField tfUname, tfPwd, tfRepPwd;
    private JButton btnSubmit, btnClose;
    private InetAddress address;
    private Socket socket;
    RegisterWin(){
        super(title,winWedth,winHeight);
        boxV1 = Box.createVerticalBox();
        boxV1.add(new JLabel("用户名："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("密码："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("确认密码："));

        boxV2 = Box.createVerticalBox();
        tfUname = new JTextField(16);
        tfUname.setText("");
        boxV2.add(tfUname);
        boxV2.add(Box.createVerticalStrut(16));
        tfPwd = new JPasswordField(16);
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
        btnSubmit.addActionListener(this);
        btnClose = new JButton("关闭");
        btnClose.addActionListener(this);
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
        validate();
        try {
            address = InetAddress.getByName(super.ADDRESS);
        } catch (UnknownHostException e) {e.printStackTrace(); }

    }
    public static void main(String[] args) {
        new RegisterWin();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton) e.getSource();
        if (bt == btnSubmit) {
            if (tfUname.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入用户名");
                return;
            }
            if (tfPwd.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入密码");
                return;
            }
            if (tfRepPwd.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入确认密码");
                return;
            }
            if (tfPwd.getText().compareTo(tfRepPwd.getText()) != 0) {
                JOptionPane.showMessageDialog(null, "两次密码输入不一致，请重新输入");
                return;
            }
            try {
                socket = new Socket(super.ADDRESS, super.PORT);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeUTF("$register$");
                out.writeUTF(tfUname.getText() + "|" + tfPwd.getText());
                String msg1 = in.readUTF();
                if(msg1.compareTo("$register$") == 0){
                    String msg2 = in.readUTF();
                    if(msg2.compareTo("success") == 0){
                        JOptionPane.showMessageDialog(null, "注册成功");
                        tfUname.setEnabled(false);
                        tfPwd.setEnabled(false);
                        tfRepPwd.setEnabled(false);
                        btnSubmit.setEnabled(false);
                        btnSubmit.setText("已注册");
                    }
                }
            }catch(Exception ee){
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }else if(bt == btnClose){
            this.close();
        }
    }
}
