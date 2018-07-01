package com.mean.csts.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
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
            address = InetAddress.getByName("localhost");
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
        if(tfUname.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "请输入用户名");
            return;
        }
        if(tfPwd.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "请输入密码");
            return;
        }
        JButton bt = (JButton)e.getSource();
        if(bt == btnLogin){
            data = new Data(Data.TYPE_LOGIN,"uname",tfUname.getText());
            msg = new TCPComm(address,data);
            msg.close();
            Data resp1=msg.getResponse();
            if(resp1!=null && resp1.getType()==Data.TYPE_FAILURE){
                JOptionPane.showMessageDialog(null, "登录失败，用户名不存在");
                return;
            }
            data = new Data(Data.TYPE_LOGIN,"uname",tfUname.getText());
            msg = new TCPComm(address,data);
            msg.close();
            Data resp2=msg.getResponse();
            if(resp2!=null && resp2.getType()==Data.TYPE_FAILURE){
                JOptionPane.showMessageDialog(null, "登录失败");
                return;
            }
            if(resp1!=null && resp1.getType()==Data.TYPE_SUCCESS) {
                if(resp2!=null && resp2.getType()==Data.TYPE_SUCCESS) {
                    JOptionPane.showMessageDialog(null, "登录成功！");
                    //TODO 登录成功后的操作

                }
            }
        }

    }
}
