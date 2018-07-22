package com.mean.csts.client.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;

import com.mean.csts.data.User;

public class GoodsStockPanel extends JPanel implements ActionListener{
    public InetAddress address;
    private Socket socket;
    public int port;
    private Box baseBox,subBoxH0,subBoxH1,subBoxH2,boxV0,boxV1,boxV2;
    private JTextField tfGname, tfGnum,tfGprice,tfGcontent;
    private JButton btnRelease,btnOpen;
    private User user;
    private String path;
    private Icon icon1=null;
    private JLabel imageView;
    public GoodsStockPanel(InetAddress address,int port){
        super();
        boxV0=Box.createVerticalBox();
        boxV0.add(new JLabel("图片："));
        boxV1 = Box.createVerticalBox();
        boxV1.add(new JLabel("商品名："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("数量："));
        boxV1.add(Box.createVerticalStrut(16));
        boxV1.add(new JLabel("价格："));
        boxV1.add(Box.createVerticalStrut(16));
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
        subBoxH0=Box.createHorizontalBox();
        subBoxH0.add(Box.createHorizontalStrut(20));
        subBoxH0.add(boxV0);
        btnOpen=new JButton("选择图片");
        btnOpen.addActionListener(this);
        imageView = new JLabel();
        imageView.setPreferredSize(new Dimension(128,128));
        imageView.setIcon(new ImageIcon("src/com/mean/csts/client/default.jpg"));
        subBoxH0.add(imageView);
        subBoxH0.add(btnOpen);
        subBoxH1 = Box.createHorizontalBox();
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV1);
        subBoxH1.add(Box.createHorizontalStrut(20));
        subBoxH1.add(boxV2);
        subBoxH2 = Box.createHorizontalBox();
        btnRelease = new JButton("发布");
        btnRelease.addActionListener(this);
        subBoxH2.add(btnRelease);
        subBoxH2.add(Box.createHorizontalStrut(20));
        baseBox = Box.createVerticalBox();
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH0);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH1);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(subBoxH2);
        baseBox.add(Box.createVerticalStrut(100));
        add(baseBox);
        validate();
        this.address = address;
        this.port = port;
    }
    public void setUser(User user){this.user=user;}
    public byte[] image2byte(String path){
        byte[] data;
        FileImageInputStream input;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[3145728];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return data;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        if(bt==btnOpen) {
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
            jfc.showDialog(new JLabel(), "选择");
            File file=jfc.getSelectedFile();
            path=file.getAbsolutePath();
            //System.out.println(path);
            try {
                icon1=new ImageIcon(ImageIO.read(new File(path)));
            } catch (IOException e1) { e1.printStackTrace(); }
            imageView.setIcon(icon1);
            validate();

        }
        if(bt==btnRelease) {     //发布商品
            if(user == null){   //数据合法性检查
                JOptionPane.showMessageDialog(null, "未登录不能发布商品");
                return;
            }
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
                socket = new Socket(address, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeUTF("$GoodsStock$"); //请求类型
                out.writeUTF(String.valueOf(user.getToken())); //请求数据
                byte[] img = image2byte(path);
                int imageLength;
                if(img == null || img.length == 0){
                    imageLength = 0;
                }else{
                    imageLength = img.length;
                }
                out.writeUTF(tfGname.getText() + "#" +
                        tfGnum.getText() + "#" +
                        tfGprice.getText() + "#" +
                        tfGcontent.getText() + "#" +
                        imageLength);
                if(imageLength>0){
                    out.write(img);
                }
                String msg1 = in.readUTF();
                if(msg1.compareTo("$GoodsStock$") == 0){ //回复类型
                    String msg2 = in.readUTF();          //回复数据
                    if(msg2.compareTo("success") == 0){  //回复数据处理
                        JOptionPane.showMessageDialog(null, "发布成功");
                        tfGname.setText("");
                        tfGnum.setText("");
                        tfGprice.setText("");
                        tfGcontent.setText("");
                        imageView.setIcon(new ImageIcon("src/com/mean/csts/client/default.jpg"));
                    }else if(msg2.compareTo("failure") == 0){
                        JOptionPane.showMessageDialog(null, "发布失败");
                    }
                }else{
                    System.out.println("!!");
                }
                socket.close();
            }catch(Exception ee){
                ee.printStackTrace();
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }
    }

}
