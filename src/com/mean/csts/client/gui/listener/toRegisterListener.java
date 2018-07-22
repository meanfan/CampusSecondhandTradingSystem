package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.RegisterWin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-22 21:45
 **/
public class toRegisterListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        RegisterWin.getInstance().validate();
        RegisterWin.getInstance().setVisible(true);
    }
}
