package com.mean.csts.client;

import com.mean.csts.Goods;

import javax.swing.*;
import javax.swing.text.IconView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class GoodsListPanel extends JPanel implements ActionListener {
    public static int winWedth =400;
    public static int winHeight =350;
    private JScrollPane scrolPane;
    private Box topBox,subBox1,subBox2;
    private JList<ItemView> list;
    private JButton btnPrev,btnNext;
    private JLabel labelPage;
    private List<ItemView> itemView;
    public InetAddress address;
    public int port;
    public GoodsListPanel(String address,int port) {
        super();
        subBox1 = Box.createVerticalBox();
        itemViewBoxRefresh(1);
        subBox2 = Box.createHorizontalBox();
        btnPrev = new JButton("上一页");
        btnNext = new JButton("下一页");
        labelPage = new JLabel("1");
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
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        fl.setHgap(2);
        setLayout(fl);
        if(goods.image!=null){
            imageIcon = new ImageIcon();
            imageIcon.setImage(goods.image);
            labelIcon.setIcon(imageIcon);
        }
        labelName= new JLabel();
        labelName.setText(goods.name);
        labelAmount = new JLabel();
        labelAmount.setText("×"+String.valueOf(goods.amount));
        labelPrice = new JLabel();
        labelPrice.setText(String.valueOf(goods.price));
        validate();
        setVisible(true);
    }

}
