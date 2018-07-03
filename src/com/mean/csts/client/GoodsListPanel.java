package com.mean.csts.client;

import com.mean.csts.Goods;
import com.mean.csts.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class GoodsListPanel extends JPanel implements ActionListener {

    private JPanel jPanel;
    private JScrollPane scrollPane;
    private Box topBox,subBox1,subBox2;
    private ItemView[] iv;
    private JButton btnPrev,btnNext;
    private JLabel labelPage;
    public InetAddress address;
    public int port;
    private int currenPage;
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
        btnPrev.setEnabled(false);
        btnNext = new JButton("下一页");
        btnPrev.setEnabled(true);
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
        currenPage =1;
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {e.printStackTrace();}
        this.port = port;
    }
    private void refreshGoodsList(int page){
        int i =0;
        //TODO
    }
    private Goods getGoods(int gid){
        try {
            Socket socket = new Socket(address, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("$getGoods$");
            out.writeUTF(String.valueOf(gid));
            String msg1 = in.readUTF();
            if(msg1.compareTo("$getGoods$") == 0){
                String[] msg2 = in.readUTF().split("#");
                if(msg2[0].compareTo("success") == 0){
                    Goods goods = new Goods();
                    goods.gid = gid;
                    goods.name = msg2[1];
                    goods.amount = Integer.valueOf(msg2[2]);
                    goods.price = Double.valueOf(msg2[3]);
                    goods.content = msg2[4];
                    in.read(goods.image);
                    return goods;
                }else if(msg2[0].compareTo("failure") == 0){
                    System.out.println("商品获取失败");
                    return null;
                }
            }
        }catch(Exception ee){
            //JOptionPane.showMessageDialog(null, "商品获取失败, 与服务器通信失败");
            return null;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        if(bt == btnPrev){
            if(currenPage>1) {
                currenPage--;
                getGoods(currenPage);
            }
            if(currenPage == 1){
                bt.setEnabled(false);
            }else{
                bt.setEnabled(true);
            }
            return;
        }
        if(bt == btnNext){
            //TODO
            currenPage++;
            getGoods(currenPage);
        }
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
            labelIcon.setIcon(new ImageIcon(byte2image(goods.image)));
        }else{
            labelIcon.setIcon(new ImageIcon("src/com/mean/csts/client/default.jpg"));
        }
        add(labelIcon);
        labelName= new JLabel();
        labelName.setText(goods.name);
        add(labelName);
        labelAmount = new JLabel();
        labelAmount.setText("数量：" + String.valueOf(goods.amount));
        add(labelAmount);
        labelPrice = new JLabel();
        labelPrice.setText("售价：" + String.valueOf(goods.price));
        add(labelPrice);
        validate();
        setVisible(true);
    }
    public Image byte2image(byte[] data){
            Image img=Toolkit.getDefaultToolkit().createImage(data,0,data.length);
            return img;
    }

}
