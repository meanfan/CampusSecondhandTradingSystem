package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.BasicWin;
import com.mean.csts.client.gui.panel.AccountPanel;
import com.mean.csts.data.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-23 18:01
 **/
public class AccountListener implements ActionListener {
    AccountPanel accountPanel;

    public static void logout(InetAddress address, int port, String uname) {
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
        accountPanel = AccountPanel.getInstance();
        if (e.getSource() == accountPanel.btnQuit) {
            if (accountPanel.user == null) {
                System.exit(0);
            } else {
                logout(BasicWin.ADDRESS,BasicWin.PORT,accountPanel.user.getUname());
                System.exit(0);
            }
        } else if (e.getSource() == accountPanel.btnLogout) {
            logout(BasicWin.ADDRESS,BasicWin.PORT,accountPanel.user.getUname());
            System.exit(0);
        }
    }

}
