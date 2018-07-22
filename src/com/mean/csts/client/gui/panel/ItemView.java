package com.mean.csts.client.gui.panel;

import com.mean.csts.client.gui.frame.GoodsPurchaseWin;
import com.mean.csts.data.Goods;
import com.mean.csts.data.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-22 21:32
 **/
public class ItemView extends JPanel implements MouseListener {
    private Goods goods;
    private User user;
    private InetAddress address;
    private int port;
    private JLabel labelIcon, labelName, labelAmount, labelPrice;
    public ItemView(){
        super();
        setBackground(Color.WHITE);
    }
    public ItemView(InetAddress address,int port,Goods goods,User user){
        super();
        setAddress(address,port);
        setUser(user);
        //System.out.println("2:"+goods.toString());
        setBackground(Color.WHITE);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.goods = goods;
        GridLayout gl = new GridLayout(1,3);
        gl.setHgap(4);
        setLayout(gl);
        labelIcon = new JLabel();
        labelIcon.setIcon(byte2image(goods.getImage()));
        add(labelIcon);
        labelName= new JLabel();
        labelName.setText(goods.getName());
        add(labelName);
        labelAmount = new JLabel();
        labelAmount.setText("数量：" + String.valueOf(goods.getAmount()));
        add(labelAmount);
        labelPrice = new JLabel();
        labelPrice.setText("售价：" + String.valueOf(goods.getPrice()));
        add(labelPrice);
        validate();
        setVisible(true);
    }
    public void setUser(User user){
        this.user = user;
    }
    public void setAddress(InetAddress address,int port){
        this.address = address;
        this.port = port;
    }
    public ImageIcon byte2image(byte[] data){
        ImageIcon icon;
        if(data == null || data.length == 0){
            icon = new ImageIcon("src/com/mean/csts/client/res/pic/default.jpg");
        }else{
            icon = new ImageIcon(data);

        }
        return icon;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(user != null)
            new GoodsPurchaseWin(address,port,goods,user);  //购买
        else {
            JOptionPane.showMessageDialog(null, "未登录不能购买");

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}