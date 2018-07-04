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

import com.mean.csts.data.User;

public class LoginWin extends BasicWin implements ActionListener {
    public static String title = "登录";
    public static int winWedth = 400;
    public static int winHeight = 380;
    public InetAddress address;
    public int port;
    private Box topBox, subBoxH1,subBoxH2, subBoxH3,boxV1,boxV2;
    private JTextField tfUname, tfPwd;
    private JButton btnLogin,btnRegister,btnGuest;
    LoginWin(){
        super(title,winWedth,winHeight);
        subBoxH1 = Box.createHorizontalBox();
        JLabel pic = new JLabel();
        pic.setPreferredSize(new Dimension(400,200));
        pic.setIcon(new ImageIcon("src/com/mean/csts/client/loginPic.jpg"));
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
        btnLogin.addActionListener(this);
        btnRegister = new JButton("注册");
        btnRegister.addActionListener(this);
        btnGuest = new JButton("游客");
        btnGuest.addActionListener(this);
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
        validate();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            address = InetAddress.getByName(super.ADDRESS);
        } catch (UnknownHostException e) { e.printStackTrace(); }
        this.port = super.PORT;
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
                    String[] msg2 = in.readUTF().split("#");
                    if(msg2[0].compareTo("success") == 0){
                        JOptionPane.showMessageDialog(null, "登录成功");
                        tfUname.setEnabled(false);
                        tfPwd.setEnabled(false);
                        User currentUser = new User();
                        currentUser.setUid(Integer.valueOf(msg2[1]));
                        currentUser.setType(msg2[2]);
                        currentUser.setUname(msg2[3]);
                        currentUser.setNickname(msg2[4]);
                        currentUser.setToken(Integer.valueOf(msg2[5]));
                        currentUser.setStatus("online");
                        currentUser.setWallet(Double.valueOf(msg2[6]));
                        if(currentUser.getType().compareTo("admin") == 0){
                            new ManageWin(address,port);
                        }else if(currentUser.getType().compareTo("normal") == 0){
                            new MainWin(currentUser);
                        }else if(currentUser.getType().compareTo("guest") == 0){
                            new MainWin(null);
                        }
                        System.out.println(currentUser.toString());
                        this.dispose();
                    }else if(msg2[0].compareTo("failure") == 0){
                        if(msg2[1].compareTo("user_err")==0){
                            JOptionPane.showMessageDialog(null, "登录失败，用户不存在或注册被拒绝");
                        }else if(msg2[1].compareTo("pwd_err")==0) {
                            JOptionPane.showMessageDialog(null, "登录失败，密码不正确");
                        }else if(msg2[1].compareTo("user_new")==0) {
                            JOptionPane.showMessageDialog(null, "登录失败，注册还未被批准");

                        }
                    }
                }
                socket.close();
            }catch(Exception ee){
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }else if(bt == btnRegister){
            new RegisterWin();
        }else if(bt == btnGuest){
            new MainWin(null);
        }
    }
}
