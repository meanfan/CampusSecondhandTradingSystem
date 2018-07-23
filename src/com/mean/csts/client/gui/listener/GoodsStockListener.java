package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.BasicWin;
import com.mean.csts.client.gui.panel.GoodsStockPanel;
import com.mean.csts.client.util.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-23 18:49
 **/
public class GoodsStockListener implements ActionListener {
    GoodsStockPanel goodsStockPanel;
    @Override
    public void actionPerformed(ActionEvent e) {
        goodsStockPanel = GoodsStockPanel.getInstance();
        JButton bt = (JButton)e.getSource();
        if(bt==goodsStockPanel.btnOpen) {
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
            jfc.showDialog(new JLabel(), "选择");
            File file=jfc.getSelectedFile();
            goodsStockPanel.path=file.getAbsolutePath();
            //System.out.println(path);
            try {
                goodsStockPanel.icon1=new ImageIcon(ImageIO.read(new File(goodsStockPanel.path)));
            } catch (IOException e1) { e1.printStackTrace(); }
            goodsStockPanel.imageView.setIcon(goodsStockPanel.icon1);
            goodsStockPanel.validate();

        }
        if(bt==goodsStockPanel.btnRelease) {     //发布商品
            if(goodsStockPanel.user == null){   //数据合法性检查
                JOptionPane.showMessageDialog(null, "未登录不能发布商品");
                return;
            }
            if(goodsStockPanel.tfGname.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入商品名");
                return;
            }
            if(goodsStockPanel.tfGnum.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入数量");
                return;
            }
            if(goodsStockPanel.tfGprice.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入价格");
                return;
            }
            if(goodsStockPanel.tfGcontent.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入描述");
                return;
            }
            try {
                goodsStockPanel.socket = new Socket(BasicWin.ADDRESS, BasicWin.PORT);
                DataOutputStream out = new DataOutputStream(goodsStockPanel.socket.getOutputStream());
                DataInputStream in = new DataInputStream(goodsStockPanel.socket.getInputStream());
                out.writeUTF("$GoodsStock$"); //请求类型
                out.writeUTF(String.valueOf(goodsStockPanel.user.getToken())); //请求数据
                byte[] img = Util.image2byte(goodsStockPanel.path);
                int imageLength;
                if(img == null || img.length == 0){
                    imageLength = 0;
                }else{
                    imageLength = img.length;
                }
                out.writeUTF(goodsStockPanel.tfGname.getText() + "#" +
                        goodsStockPanel.tfGnum.getText() + "#" +
                        goodsStockPanel.tfGprice.getText() + "#" +
                        goodsStockPanel.tfGcontent.getText() + "#" +
                        imageLength);
                if(imageLength>0){
                    out.write(img);
                }
                String msg1 = in.readUTF();
                if(msg1.compareTo("$GoodsStock$") == 0){ //回复类型
                    String msg2 = in.readUTF();          //回复数据
                    if(msg2.compareTo("success") == 0){  //回复数据处理
                        JOptionPane.showMessageDialog(null, "发布成功");
                        goodsStockPanel.tfGname.setText("");
                        goodsStockPanel.tfGnum.setText("");
                        goodsStockPanel.tfGprice.setText("");
                        goodsStockPanel.tfGcontent.setText("");
                        goodsStockPanel.imageView.setIcon(new ImageIcon("src/com/mean/csts/client/default.jpg"));
                    }else if(msg2.compareTo("failure") == 0){
                        JOptionPane.showMessageDialog(null, "发布失败");
                    }
                }else{
                    System.out.println("!!");
                }
                goodsStockPanel.socket.close();
            }catch(Exception ee){
                //ee.printStackTrace();
                JOptionPane.showMessageDialog(null, "与服务器通信失败");
                return;
            }
        }
    }
}
