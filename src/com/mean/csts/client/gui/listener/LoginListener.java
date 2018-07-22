package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.*;
import com.mean.csts.data.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-22 21:43
 **/
public class LoginListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        LoginWin loginWin = LoginWin.getInstance();
        if(loginWin.tfUname.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "请输入用户名");
            return;
        }
        if(loginWin.tfPwd.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "请输入密码");
            return;
        }
        try {
            Socket socket = new Socket(BasicWin.ADDRESS, BasicWin.PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("$login$"); //发送请求类型
            out.writeUTF(loginWin.tfUname.getText() + "#" + loginWin.tfPwd.getText());
            String msg1 = in.readUTF();
            if(msg1.compareTo("$login$") == 0){  //回复请求类型
                String[] msg2 = in.readUTF().split("#");
                if(msg2[0].compareTo("success") == 0){ //回复内容判断处理
                    JOptionPane.showMessageDialog(null, "登录成功");
                    loginWin.tfUname.setEnabled(false);
                    loginWin.tfPwd.setEnabled(false);
                    User currentUser = new User(); // 创建用户实例
                    currentUser.setUid(Integer.valueOf(msg2[1]));
                    currentUser.setType(msg2[2]);
                    currentUser.setUname(msg2[3]);
                    currentUser.setNickname(msg2[4]);
                    currentUser.setToken(Integer.valueOf(msg2[5]));
                    currentUser.setStatus("online");
                    currentUser.setWallet(Double.valueOf(msg2[6]));
                    if(currentUser.getType().compareTo("admin") == 0){
                        ManageWin ManageWinInstance = ManageWin.getInstance();
                        ManageWinInstance.setCurrentUser(currentUser);
                        ManageWinInstance.setVisible(true);
                        ManageWinInstance.validate();
                    }else if(currentUser.getType().compareTo("normal") == 0){
                        new MainWin(currentUser);
                    }else if(currentUser.getType().compareTo("guest") == 0){
                        new MainWin(null);
                    }
                    System.out.println(currentUser.toString());
                    loginWin.dispose();
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
        }
    }
}
