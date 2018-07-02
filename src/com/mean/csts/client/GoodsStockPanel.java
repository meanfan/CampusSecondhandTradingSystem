package com.mean.csts.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import com.mean.csts.TCPComm;
import com.mean.csts.data.Data;

public class GoodsStockPanel extends JPanel implements ActionListener{
    //public static String title = "发布商品";
    public static int winWedth =400;
    public static int winHeight =350;
    public InetAddress address;
    public int port;
    private Box baseBox,subBoxH1,subBoxH2,boxV1,boxV2;
    private JTextField tfGname, tfGnum,tfGprice,tfGcontent;
    private JButton btnRelease;

    GoodsStockPanel(String address,int port){
        super();
        boxV1 = Box.createVerticalBox();
        boxV1.add(new JLabel("商品名："));
        boxV1.add(Box.createVerticalStrut(20));
        boxV1.add(new JLabel("数量："));
        boxV1.add(Box.createVerticalStrut(20));
        boxV1.add(new JLabel("价格："));
        boxV1.add(Box.createVerticalStrut(30));
        boxV1.add(new JLabel("描述："));
        boxV2 = Box.createVerticalBox();
        tfGname = new JTextField(16);
        tfGname.setText("");
        boxV2.add(tfGname);
        boxV2.add(Box.createVerticalStrut(16));
        tfGnum = new JTextField(16);
        tfGnum.setText("");
        boxV2.add(tfGnum);
        boxV2.add(Box.createVerticalStrut(16));
        tfGprice = new JTextField(16);
        tfGprice.setText("");
        boxV2.add(tfGprice);
        boxV2.add(Box.createVerticalStrut(16));
        tfGcontent = new JTextField(16);
        tfGcontent.setText("");
        boxV2.add(tfGcontent);
        subBoxH1 = Box.createHorizontalBox();
        subBoxH1.add(boxV1);
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV2);
        subBoxH2 = Box.createHorizontalBox();
        btnRelease = new JButton("发布");
        btnRelease.addActionListener(this);
        subBoxH2.add(btnRelease);
        subBoxH2.add(Box.createHorizontalStrut(20));
        baseBox = Box.createVerticalBox();
        baseBox.add(Box.createVerticalStrut(100));
        baseBox.add(subBoxH1);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH2);
        baseBox.add(Box.createVerticalStrut(150));
        add(baseBox);
        validate();
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {e.printStackTrace();}
        this.port = port;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        if(bt==btnRelease)
            if(tfGname.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入商品名");
                return;
            }
        if(tfGnum.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "请输入数量");
            return;
        }
        if(tfGprice.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "请输入价格");
            return;
        }
        if(tfGcontent.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "请输入描述");
            return;
        }
        try {
            Socket socket = new Socket(address, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("$GoodsStock$");
            out.writeUTF(tfGname.getText() + "#" + tfGnum.getText()+"#"+tfGprice.getText()+"#"+tfGcontent.getText());
            String msg1 = in.readUTF();
            if(msg1.compareTo("$GoodStock$") == 0){
                String msg2 = in.readUTF();
                if(msg2.compareTo("success") == 0){
                    JOptionPane.showMessageDialog(null, "发布成功");
                    tfGname.setEnabled(false);
                    tfGnum.setEnabled(false);
                    //TODO 发布成功成功后操作
                }else if(msg2.compareTo("failure") == 0){
                    JOptionPane.showMessageDialog(null, "发布失败");
                }
            }
        }catch(Exception ee){
            JOptionPane.showMessageDialog(null, "与服务器通信失败");
            return;
        }
    }
}
