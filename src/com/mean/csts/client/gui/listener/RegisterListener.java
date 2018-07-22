package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.RegisterWin;

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
 * @create: 2018-07-22 22:08
 **/
public class RegisterListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {   //注册
        JButton bt = (JButton) e.getSource();
        RegisterWin registerWin = RegisterWin.getInstance();
        //输入内容合法性检查
        if (bt == registerWin.btnSubmit) {
            if (registerWin.tfUname.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入用户名");
                return;
            }
            if (registerWin.tfNickname.getText().length() == 0) {
                registerWin.tfNickname.setText(registerWin.tfUname.getText());
            }
            if (registerWin.tfPwd.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入密码");
                return;
            }
            if (registerWin.tfRepPwd.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入确认密码");
                return;
            }
            if (registerWin.tfPwd.getText().compareTo(registerWin.tfRepPwd.getText()) != 0) {
                JOptionPane.showMessageDialog(null, "两次密码输入不一致，请重新输入");
                return;
            }
            try {       //与服务器通讯
                registerWin.socket = new Socket(registerWin.address, registerWin.port);
                DataOutputStream out = new DataOutputStream(registerWin.socket.getOutputStream());
                DataInputStream in = new DataInputStream(registerWin.socket.getInputStream());
                out.writeUTF("$register$");          //请求类型
                out.writeUTF(registerWin.tfUname.getText() + "#" + registerWin.tfNickname.getText()+"#" + registerWin.tfPwd.getText()); //请求数据
                String msg1 = in.readUTF();
                if(msg1.compareTo("$register$") == 0){  //回复类型
                    String[] msg2 = in.readUTF().split("#");
                    if(msg2[0].compareTo("success") == 0){ //回复数据
                        JOptionPane.showMessageDialog(null, "注册请求发送成功，请等待批准");
                        registerWin.tfUname.setEnabled(false);
                        registerWin.tfNickname.setEnabled(false);
                        registerWin.tfPwd.setEnabled(false);
                        registerWin.tfRepPwd.setEnabled(false);
                        registerWin.btnSubmit.setEnabled(false);
                        registerWin.btnSubmit.setText("等待批准");
                    }else if(msg2[0].compareTo("failure") == 0){
                        JOptionPane.showMessageDialog(null, "注册失败，用户名已被占用");
                    }
                }
                registerWin.socket.close();
            }catch(Exception ee){
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }else if(bt == registerWin.btnClose){
            registerWin.dispose();
        }
    }
}
