package com.mean.csts.client;

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
    private JTextField tfUname, tfNickname, tfPwd, tfRepPwd;
    private JButton btnSubmit, btnClose;
    private InetAddress address;
    private int port;
    private Socket socket;
    RegisterWin(){
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        address = super.ADDRESS;
        port = super.PORT;


    }
    public static void main(String[] args) {
        new RegisterWin();
    }
    @Override
    public void actionPerformed(ActionEvent e) {   //注册
        JButton bt = (JButton) e.getSource();
        //输入内容合法性检查
        if (bt == btnSubmit) {
            if (tfUname.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入用户名");
                return;
            }
            if (tfNickname.getText().length() == 0) {
                tfNickname.setText(tfUname.getText());
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
            try {       //与服务器通讯
                socket = new Socket(address, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeUTF("$register$");          //请求类型
                out.writeUTF(tfUname.getText() + "#" + tfNickname.getText()+"#" + tfPwd.getText()); //请求数据
                String msg1 = in.readUTF();
                if(msg1.compareTo("$register$") == 0){  //回复类型
                    String[] msg2 = in.readUTF().split("#");
                    if(msg2[0].compareTo("success") == 0){ //回复数据
                        JOptionPane.showMessageDialog(null, "注册请求发送成功，请等待批准");
                        tfUname.setEnabled(false);
                        tfNickname.setEnabled(false);
                        tfPwd.setEnabled(false);
                        tfRepPwd.setEnabled(false);
                        btnSubmit.setEnabled(false);
                        btnSubmit.setText("等待批准");
                    }else if(msg2[0].compareTo("failure") == 0){
                        JOptionPane.showMessageDialog(null, "注册失败，用户名已被占用");
                    }
                }
                socket.close();
            }catch(Exception ee){
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }else if(bt == btnClose){
            this.dispose();
        }
    }
}
