package com.mean.csts.client;

import com.mean.csts.Goods;

import javax.swing.*;
import javax.swing.text.IconView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class GoodsListPanel extends JPanel implements ActionListener {

    private JPanel jPanel;
    private JScrollPane scrollPane;
    private Box topBox,subBox1,subBox2;
    private ItemView[] iv;
    private JButton btnPrev,btnNext;
    private JLabel labelPage;
    public InetAddress address;
    public int port;
    public GoodsListPanel(String address,int port) {
        super();
        subBox1 = Box.createVerticalBox();
        ItemView[] iv = new ItemView[5];
        jPanel = new JPanel();
        GridLayout gl = new GridLayout(5,1);
        gl.setHgap(1);
        gl.setVgap(1);
        jPanel.setLayout(gl);
        jPanel.setPreferredSize(new Dimension(512,640));
        jPanel.setBackground(Color .BLACK);

        for(ItemView it:iv){
            it = new ItemView(new Goods("a",1));
            jPanel.add(it);
        }
        //jPanel.add(new ItemView(new Goods()));
        scrollPane = new JScrollPane(jPanel);
        subBox1.add(scrollPane);
        //subBox1.add(new ItemView(new Goods("a",1)));
        subBox2 = Box.createHorizontalBox();
        btnPrev = new JButton("上一页");
        btnNext = new JButton("下一页");
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
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {e.printStackTrace();}
        this.port = port;
    }
    private void itemViewBoxRefresh(int page){

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

class ItemView extends JPanel{
    private ImageIcon imageIcon;
    private JLabel labelIcon, labelName, labelAmount, labelPrice;
    public ItemView(Goods goods){
        super();
        setBackground(Color.WHITE);
        setGoods(goods);
    }
    public void setGoods(Goods goods){
        GridLayout gl = new GridLayout(1,3);
        gl.setHgap(4);
        setLayout(gl);

        labelIcon = new JLabel();
        if(goods.image!=null){
            labelIcon.setIcon(goods.image);
        }else{
            labelIcon.setIcon(new ImageIcon("src/com/mean/csts/client/default.jpg"));
        }
        add(labelIcon);
        labelName= new JLabel();
        labelName.setText(goods.name);
        add(labelName);
        labelAmount = new JLabel();
        labelAmount.setText("×"+String.valueOf(goods.amount));
        add(labelAmount);
        labelPrice = new JLabel();
        labelPrice.setText(String.valueOf(goods.price));
        add(labelPrice);
        validate();
        setVisible(true);
    }

}
