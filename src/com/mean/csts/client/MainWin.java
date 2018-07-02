package com.mean.csts.client;

import javax.swing.*;
import java.awt.*;

public class MainWin extends BasicWin{
    public static String title = "注册";
    public static int winWedth = 400;
    public static int winHeight = 600;
    private JPanel goodsListPanel,goodsStockPanel;
    public MainWin() {
        super(title,winWedth,winHeight);
        JTabbedPane p=new JTabbedPane(JTabbedPane.TOP);
        goodsListPanel = new GoodsListPanel(super.ADDRESS,super.PORT);
        p.add("在售",goodsListPanel);
        p.validate();
        goodsStockPanel = new GoodsStockPanel(super.ADDRESS,super.PORT);
        p.add("上架",goodsStockPanel);
        p.validate();
        this.add(p,BorderLayout.CENTER);
        this.validate();;
        setVisible(true);
    }
    public static void main(String[] args){
        new MainWin();
    }
}
