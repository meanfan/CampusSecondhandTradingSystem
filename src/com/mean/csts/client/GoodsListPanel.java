package com.mean.csts.client;

import com.mean.csts.data.Goods;
import com.mean.csts.data.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class GoodsListPanel extends JPanel implements ActionListener {

    public User user;
    public Goods[] goodsList;
    private JPanel jPanel;
    private JScrollPane scrollPane;
    private Box topBox,subBox1,subBox2;
    private ItemView[] iv;
    private JButton btnPrev,btnNext;
    private JLabel labelPage;
    public InetAddress address;
    public int port;
    private Socket socket;
    private int currenPage, numOfEachPage;
    public GoodsListPanel(InetAddress address,int port) {
        super();
        currenPage = 1;
        numOfEachPage = 5;
        iv = new ItemView[5];
        this.address = address;
        this.port = port;
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
        btnPrev.addActionListener(this);
        btnNext = new JButton("下一页");
        btnNext.setEnabled(true);
        btnNext.addActionListener(this);
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
        new Thread(new GetGoodsListThread()).start();
    }
    class GetGoodsListThread implements Runnable{
        @Override
        public void run() {
            System.out.println("开始获取商品列表线程");
            refreshGoodsList(1,numOfEachPage);
        }
    }
    public void setUser(User user){this.user=user;}
    private void refreshGoodsList(int page,int num){
        goodsList = getGoods(page,num);
        if(goodsList[0] == null) { //没有商品
            JOptionPane.showMessageDialog(null, "没有更多商品");
        }else{
            jPanel.removeAll();
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
            for(int i=0;i<5;i++){
                if(goodsList[i] != null){
                    System.out.println("1:"+goodsList[i].toString());
                    iv[i] = new ItemView(address,port,goodsList[i],user);
                    iv[i].addMouseListener(iv[i]);
                }else{
                    iv[i] = new ItemView();
                    btnNext.setEnabled(false);
                }
                jPanel.add(iv[i]);
            }
            currenPage = page;
            if(currenPage == 1)
                btnPrev.setEnabled(false);
            labelPage.setText(String.valueOf(currenPage));
        }
        jPanel.validate();
        validate();
    }
    private Goods[] getGoods(int page,int num){         //从服务器获取指定页号和数量的商品
        goodsList = new Goods[num];
        try {
            socket = new Socket(address, port);
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
                        goodsList[i] = new Goods();
                        byte[] image = new byte[3145728];
                        //in.read(image);
                        goodsList[i].setImage(image);
                        String str3 = in.readUTF();
                        //System.out.println("str("+str3+")");
                        String[] msg3 = str3.split("#");
                        goodsList[i].setGid(Integer.valueOf(msg3[0]));
                        goodsList[i].setName(msg3[1]);
                        goodsList[i].setAmount(Integer.valueOf(msg3[2]));
                        goodsList[i].setPrice(Double.valueOf(msg3[3]));
                        goodsList[i].setContent(msg3[4]);
                        goodsList[i].setUid(Integer.valueOf(msg3[5]));
                        //System.out.println("3:"+goodsList[i].toString());
                    }
                    for(;i<num;i++){
                        goodsList[i] = null;
                    }
                    System.out.println("商品获得数量:"+gotNum);
                    return goodsList;
                }else if(msg2[0].compareTo("failure") == 0){ //失败处理
                        System.out.println("无商品");
                        goodsList = new Goods[num];
                        for(int i=0;i<num;i++)
                            goodsList[i] = null;
                        return goodsList;
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
        JButton bt = (JButton)e.getSource();
        if(bt == btnPrev){
            if(currenPage>1){
                refreshGoodsList(currenPage-1,numOfEachPage); //获取上一页
            }
            return;
        }
        if(bt == btnNext){
            refreshGoodsList(currenPage+1,numOfEachPage);     //获取下一页
            return;
        }
    }
}
/*商品条目类*/
class ItemView extends JPanel implements MouseListener {
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
        System.out.println("2:"+goods.toString());
        setBackground(Color.WHITE);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.goods = goods;
        GridLayout gl = new GridLayout(1,3);
        gl.setHgap(4);
        setLayout(gl);
        labelIcon = new JLabel();
        labelIcon.setIcon(new ImageIcon("src/com/mean/csts/client/default.jpg"));
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
    public Image byte2image(byte[] data){
            Image img=Toolkit.getDefaultToolkit().createImage(data,0,data.length);
            return img;
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
