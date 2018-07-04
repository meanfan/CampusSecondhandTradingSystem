package com.mean.csts.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import com.mean.csts.data.Goods;
import com.mean.csts.data.User;

public class GoodsPurchaseWin extends BasicWin implements ActionListener{
    public static int winWedth =400;
    public static int winHeight =350;
    private ItemView iv;
    private Box baseBox,subBoxV1,subBoxH2,subBoxH3,subBoxH4,boxV1,boxV2,boxV3,boxV4;
    private JButton btnPurchs,btnCancel;
    private JTextField tfGnum;
    private JLabel jLabelWallet;
    public InetAddress address;
    public int port;
    public User user;
    public Goods goods;
    public GoodsPurchaseWin(InetAddress address,int port,Goods goods,User user) {
        super();
        this.address = address;
        this.port = port;
        this.user = user;
        this.goods = goods;
        subBoxV1 = Box.createVerticalBox();
        iv=new ItemView(address,port,goods,user);
        subBoxV1.add(iv);
        subBoxH2 = Box.createHorizontalBox();
        subBoxH3=Box.createHorizontalBox();
        subBoxH4=Box.createHorizontalBox();
        boxV1=Box.createVerticalBox();
        boxV2=Box.createVerticalBox();
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("购买的数量："));
        tfGnum = new JTextField(16);
        boxV2.add(Box.createVerticalStrut(16));
        boxV2.add(tfGnum);
        tfGnum.setText("");
        subBoxH2.add(boxV1);
        subBoxH2.add(Box.createHorizontalStrut(20));
        subBoxH2.add(boxV2);
        boxV3=Box.createVerticalBox();
        boxV4=Box.createVerticalBox();
        boxV3.add(new JLabel("账户余额："));
        if(user != null) {
            jLabelWallet = new JLabel(String.valueOf(user.getWallet()));
        }else {
            jLabelWallet =new JLabel("-1");
        }
        boxV4.add(jLabelWallet);
        subBoxH3.add(boxV3);
        subBoxH3.add(Box.createHorizontalStrut(20));
        subBoxH3.add(boxV4);
        btnPurchs=new JButton("购买");
        btnPurchs.addActionListener(this);
        btnCancel=new JButton("关闭");
        btnCancel.addActionListener(this);
        subBoxH4.add(btnPurchs);
        subBoxH4.add(Box.createHorizontalStrut(20));
        subBoxH4.add(btnCancel);
        baseBox = Box.createVerticalBox();
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxV1);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH2);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH3);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH4);
        baseBox.add(Box.createVerticalStrut(100));
        add(baseBox);
        validate();
        setVisible(true);
    }
    public static void main(String[] strs){
        try {
            new  GoodsPurchaseWin(InetAddress.getByName("localhost"),2333,new Goods(),new User());
        } catch (UnknownHostException e) { e.printStackTrace(); }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        if(bt==btnPurchs) {
            if(tfGnum.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入数量");
                return;
            }
            if(user.getWallet()<(goods.getPrice()*Integer.valueOf(tfGnum.getText()))) {
                JOptionPane.showMessageDialog(null, "余额不足");
                return;
            }
            try {
                Socket socket = new Socket(address, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeUTF("$purchaseRequest$");
                out.writeUTF(String.valueOf(user.getToken())+"#"+goods.getGid()+"#"+tfGnum.getText());
                String msg1 = in.readUTF();
                if(msg1.compareTo("$purchaseResponse$") == 0){
                    String msg2 = in.readUTF();
                    String strs[] = msg2.split("#");
                    if(strs[0].compareTo("success") == 0){
                        user.setWallet(Double.valueOf(strs[1]));
                        JOptionPane.showMessageDialog(null, "支付成功");
                        jLabelWallet.setText(String.valueOf(user.getWallet()));
                        tfGnum.setEnabled(false);
                        btnPurchs.setEnabled(false);
                    }else if(strs[0].compareTo("failure") == 0){
                        switch(strs[1]){
                            case "no_user":{
                                JOptionPane.showMessageDialog(null, "支付失败,未登录或登录过期");
                                break;
                            }
                            case "none":
                                JOptionPane.showMessageDialog(null, "支付失败,商品不存在或已下架");
                                break;
                            case "lack_stock":
                                JOptionPane.showMessageDialog(null, "支付失败,库存数量不足");
                                break;
                            case "lack_wallet":
                                JOptionPane.showMessageDialog(null, "支付失败,钱包余额不足");
                        }

                    }
                }
            }catch(Exception ee){
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }
        if(bt==btnCancel) {
            this.dispose();
        }
    }
}

