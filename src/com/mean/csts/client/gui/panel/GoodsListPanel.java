package com.mean.csts.client.gui.panel;

import com.mean.csts.client.gui.frame.BasicWin;
import com.mean.csts.client.gui.listener.GoodsListListener;
import com.mean.csts.data.Goods;
import com.mean.csts.data.User;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
public class GoodsListPanel extends JPanel {
    private static GoodsListPanel instance = null;
    public User user;
    public Goods[] goodsList;
    public JPanel jPanel;
    public JScrollPane scrollPane;
    private Box topBox,subBox1,subBox2;
    public ItemView[] iv;
    public JButton btnPrev,btnNext;
    public JLabel labelPage;
    public InetAddress address;
    public int port;
    public int currenPage, numOfEachPage;
    public GoodsListListener goodsListListener;

    public static GoodsListPanel getInstance() {
        if(instance == null){
            instance = new GoodsListPanel();
        }
        return instance;
    }
    private GoodsListPanel() {
        super();
        currenPage = 1;
        numOfEachPage = 5;
        iv = new ItemView[5];
        this.address = BasicWin.ADDRESS;
        this.port = BasicWin.PORT;
        subBox1 = Box.createVerticalBox();
        jPanel = new JPanel();
        GridLayout gl = new GridLayout(5,1);
        gl.setHgap(1);
        gl.setVgap(1);
        jPanel.setLayout(gl);
        jPanel.setPreferredSize(new Dimension(512,640));
        jPanel.setBackground(Color .BLACK);
        scrollPane = new JScrollPane(jPanel);
        subBox1.add(scrollPane);
        subBox2 = Box.createHorizontalBox();
        btnPrev = new JButton("上一页");
        btnPrev.setEnabled(false);
        goodsListListener = new GoodsListListener();
        btnPrev.addActionListener(goodsListListener);
        btnNext = new JButton("下一页");
        btnNext.setEnabled(true);
        btnNext.addActionListener(goodsListListener);
        labelPage = new JLabel(" 1 ");
        subBox2.add(btnPrev);
        subBox2.add(labelPage);
        subBox2.add(btnNext);
        topBox = Box.createVerticalBox();
        topBox.add(subBox1);
        topBox.add(subBox2);
        add(topBox);
        validate();
        setVisible(true);
    }

    public void setUser(User user){this.user=user;}

}
