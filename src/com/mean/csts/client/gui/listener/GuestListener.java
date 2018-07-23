package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.LoginWin;
import com.mean.csts.client.gui.frame.MainWin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-22 21:50
 **/
public class GuestListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        MainWin.getInstance().setUser(null);
        LoginWin.getInstance().dispose();

    }
}
