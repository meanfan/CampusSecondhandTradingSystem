package com.mean.csts.client.gui.frame;

import com.mean.csts.client.gui.panel.AccountPanel;
import com.mean.csts.client.gui.panel.GoodsListPanel;
import com.mean.csts.client.gui.panel.GoodsStockPanel;
import com.mean.csts.data.User;

import javax.swing.*;
import java.awt.*;

public class MainWin extends BasicWin{
    private static String title = "校园二手商品交易平台";
    private static int winWedth = 520;
    private static int winHeight = 750;
    public User user;
    private GoodsListPanel goodsListPanel;
    private GoodsStockPanel goodsStockPanel;
    private AccountPanel accountPanel;
    public MainWin() {
        super(title,winWedth,winHeight);
        JTabbedPane p=new JTabbedPane(JTabbedPane.TOP);
        goodsListPanel = new GoodsListPanel(super.ADDRESS,super.PORT);
        p.add("在售",goodsListPanel);
        p.validate();
        goodsStockPanel = new GoodsStockPanel(super.ADDRESS,super.PORT);
        p.add("上架",goodsStockPanel);
        accountPanel = new AccountPanel(super.ADDRESS,super.PORT);
        p.add("账户",accountPanel);
        p.validate();
        this.add(p,BorderLayout.CENTER);
        this.validate();;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    public MainWin(User currentUser){
        this();
        user = currentUser;
        goodsStockPanel.setUser(user);
        goodsListPanel.setUser(user);
        accountPanel.setUser(user);
    }
    public static void main(String[] args){
        new MainWin();
    }
}
