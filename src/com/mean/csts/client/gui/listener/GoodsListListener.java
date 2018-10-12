package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.BasicWin;
import com.mean.csts.client.gui.panel.GoodsListPanel;
import com.mean.csts.client.gui.panel.ItemView;
import com.mean.csts.pojo.Goods;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-23 18:28
 **/
public class GoodsListListener implements ActionListener {
    GoodsListPanel goodsListPanel;
    public void refreshGoodsList(int page,int num){
        goodsListPanel = GoodsListPanel.getInstance();
        goodsListPanel.goodsList = getGoods(page,num);
        if(goodsListPanel.goodsList[0] == null) { //没有商品
            JOptionPane.showMessageDialog(null, "没有更多商品");
        }else{
            goodsListPanel.jPanel.removeAll();
            goodsListPanel.btnNext.setEnabled(true);
            goodsListPanel.btnPrev.setEnabled(true);
            for(int i=0;i<5;i++){
                if(goodsListPanel.goodsList[i] != null){
                    System.out.println(i+":"+goodsListPanel.goodsList[i].toString());
                    goodsListPanel.iv[i] = new ItemView(BasicWin.ADDRESS,BasicWin.PORT,goodsListPanel.goodsList[i],goodsListPanel.user);
                    goodsListPanel.iv[i].addMouseListener(goodsListPanel.iv[i]);
                }else{
                    goodsListPanel.iv[i] = new ItemView();
                    goodsListPanel.btnNext.setEnabled(false);
                }
                goodsListPanel.jPanel.add(goodsListPanel.iv[i]);
            }
            goodsListPanel.currenPage = page;
            if(goodsListPanel.currenPage == 1)
                goodsListPanel.btnPrev.setEnabled(false);
            goodsListPanel.labelPage.setText(String.valueOf(goodsListPanel.currenPage));
        }
        goodsListPanel.jPanel.validate();
        goodsListPanel.validate();
    }

    private Goods[] getGoods(int page, int num){         //从服务器获取指定页号和数量的商品
        goodsListPanel = GoodsListPanel.getInstance();
        goodsListPanel.goodsList = new Goods[num];
        try {
            Socket socket = new Socket(BasicWin.ADDRESS, BasicWin.PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("$getGoods$");             //请求类型
            out.writeUTF(String.valueOf(page)+"#"+String.valueOf(num)); //请求数据，页号和数量
            String msg1 = in.readUTF();
            if(msg1.compareTo("$GoodsInfo$") == 0){              //回复类型
                String[] msg2 = in.readUTF().split("#");  //回复数据
                if(msg2[0].compareTo("success") == 0){           //回复数据处理判断
                    int gotNum = Integer.valueOf(msg2[1]);
                    System.out.println("GotNum:"+gotNum);
                    int i;
                    for(i=0;i<gotNum;i++){
                        goodsListPanel.goodsList[i] = new Goods();
                        byte[] image;
                        String str3 = in.readUTF();
                        //System.out.println("str("+str3+")");
                        String[] msg3 = str3.split("#");
                        goodsListPanel.goodsList[i].setGid(Integer.valueOf(msg3[0]));
                        goodsListPanel.goodsList[i].setName(msg3[1]);
                        goodsListPanel.goodsList[i].setAmount(Integer.valueOf(msg3[2]));
                        goodsListPanel.goodsList[i].setPrice(Double.valueOf(msg3[3]));
                        goodsListPanel.goodsList[i].setContent(msg3[4]);
                        goodsListPanel.goodsList[i].setUid(Integer.valueOf(msg3[5]));
                        int imageLength = Integer.valueOf(msg3[6]);
                        System.out.println("imageLength:"+imageLength);
                        if(imageLength > 0){
                            image = new byte[imageLength];
                            in.read(image);
                            goodsListPanel.goodsList[i].setImage(image);
                        }else {
                            goodsListPanel.goodsList[i].setImage(null);
                        }
                        //System.out.println("3:"+goodsList[i].toString());
                    }
                    for(;i<num;i++){
                        goodsListPanel.goodsList[i] = null;
                    }
                    System.out.println("商品获得数量:"+gotNum);
                    return goodsListPanel.goodsList;
                }else if(msg2[0].compareTo("failure") == 0){ //失败处理
                    System.out.println("无商品");
                    goodsListPanel.goodsList = new Goods[num];
                    for(int i=0;i<num;i++)
                        goodsListPanel.goodsList[i] = null;
                    return goodsListPanel.goodsList;
                }else{
                    System.out.println("?");
                }
            }
            System.out.println("??");
        }catch(Exception ee){
            JOptionPane.showMessageDialog(null, "商品获取失败, 与服务器通信失败");
            ee.printStackTrace();
            return null;
        }
        return null;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        goodsListPanel = GoodsListPanel.getInstance();
        JButton bt = (JButton)e.getSource();
        if(bt == goodsListPanel.btnPrev){
            if(goodsListPanel.currenPage>1){
                refreshGoodsList(goodsListPanel.currenPage-1,goodsListPanel.numOfEachPage); //获取上一页
            }
            return;
        }
        if(bt == goodsListPanel.btnNext){
            refreshGoodsList(goodsListPanel.currenPage+1,goodsListPanel.numOfEachPage);     //获取下一页
            return;
        }
    }
}
