package com.mean.csts.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import com.mean.csts.data.Data;
import com.mean.csts.TCPComm;

public class LoginWin extends BasicWin implements ActionListener {
    public static String title = "注册";
    public static int winWedth = 400;
    public static int winHeight = 350;
    public InetAddress address;
    private Box baseBox,subBoxH1,subBoxH2,boxV1,boxV2;
    private JTextField tfUname, tfPwd;
    private JButton btnLogin,btnRegister;
    private Data data;
    private TCPComm msg;
    LoginWin(){
        super(title,winWedth,winHeight);
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
        subBoxH1 = Box.createHorizontalBox();
        subBoxH1.add(boxV1);
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV2);
        subBoxH2 = Box.createHorizontalBox();
        btnLogin = new JButton("登录");
        btnLogin.addActionListener(this);
        btnRegister = new JButton("注册");
        btnRegister.addActionListener(this);
        subBoxH2.add(btnLogin);
        subBoxH2.add(Box.createHorizontalStrut(20));
        subBoxH2.add(btnRegister);
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
        } catch (UnknownHostException e) { e.printStackTrace(); }

        //this.msgListener = msgListener;
    }
    public static void main(String[] args) {
        new LoginWin();
    }
    public void close() {
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        if(bt == btnLogin){
            if(tfUname.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入用户名");
                return;
            }
            if(tfPwd.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入密码");
                return;
            }
            try {
                Socket socket = new Socket(super.ADDRESS, super.PORT);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeUTF("$login$");
                out.writeUTF(tfUname.getText() + "#" + tfPwd.getText());
                String msg1 = in.readUTF();
                if(msg1.compareTo("$login$") == 0){
                    String msg2 = in.readUTF();
                    if(msg2.compareTo("success") == 0){
                        JOptionPane.showMessageDialog(null, "登录成功");
                        tfUname.setEnabled(false);
                        tfPwd.setEnabled(false);
                        //TODO 登录成功后操作
                    }else if(msg2.compareTo("failure") == 0){
                        JOptionPane.showMessageDialog(null, "登录失败，用户名/密码不正确");
                    }
                }
            }catch(Exception ee){
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }else if(bt == btnRegister){
            new RegisterWin();
        }

    }
}
