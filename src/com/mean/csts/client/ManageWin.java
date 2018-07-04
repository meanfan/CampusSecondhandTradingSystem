package com.mean.csts.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.mean.csts.data.User;

public class ManageWin  extends JPanel implements ActionListener, ItemListener,MouseListener{
    private JPanel pSouth,pNorth;
    private Box boxH1,boxH2;
    private JComboBox list;
    private JTextField tfsearch;
    private JButton btnSearch,btnAppend,btnDelete,btnSubmit;
    private JTable table;
    public Object domain;
    Object a[][];
    Object name[]= {"UID","用户类型","用户名","密码","昵称","状态"};
    private int Duid[]=new int[20];
    public InetAddress address;
    public int port;
    public User user;
    public ManageWin(String address,int port) {
        super();
        for(int i=0;i<20;i++)
        {
            Duid[i]=0;
        }
        btnSearch=new JButton("搜索");
        btnAppend=new JButton("添加");
        btnDelete=new JButton("删除");
        btnSubmit=new JButton("提交");
        btnSearch.addActionListener(this);
        btnAppend.addActionListener(this);
        btnDelete.addActionListener(this);
        btnSubmit.addActionListener(this);
        list=new JComboBox();
        list.addItem("用户");
        list.addItem("商品");
        list.addItemListener(this);
        tfsearch=new JTextField(20);
        tfsearch.setText("");
        boxH1=Box.createHorizontalBox();
        boxH2=Box.createHorizontalBox();
        pSouth=new JPanel();
        pNorth=new JPanel();
        boxH1.add(list);
        boxH1.add(Box.createHorizontalStrut(16));
        boxH1.add(tfsearch);
        boxH1.add(Box.createHorizontalStrut(16));
        boxH1.add(btnSearch);
        pNorth.add(boxH1);
        boxH2.add(btnAppend);
        boxH2.add(Box.createHorizontalStrut(16));
        boxH2.add(btnDelete);
        boxH2.add(Box.createHorizontalStrut(16));
        boxH2.add(btnSubmit);
        pSouth.add(boxH2);
        table=new JTable();
        table.addMouseListener(this);
        add(pSouth,BorderLayout.SOUTH);
        add(pNorth,BorderLayout.NORTH);
        add(new JScrollPane(table),BorderLayout.CENTER);
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {e.printStackTrace();}
        this.port = port;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        if(bt==btnSearch) {
            if(domain=="用户") {
                if(tfsearch.getText().length() == 0)
                {
                    try {
                        Socket socket = new Socket(address, port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        out.writeUTF("$requestALLUSER$");
                        String msg1 = in.readUTF();
                        if(msg1.compareTo("$reauestALLUSER$") == 0){
                            String[] msg2 = in.readUTF().split("#");
                            if(msg2[0].compareTo("success") == 0){
                                JOptionPane.showMessageDialog(null, "读取成功");
                                a=new Object[Integer.valueOf(msg2[1])][6];
                                for(int i=0;i<Integer.valueOf(msg2[1]);i++) {
                                    int k=0;
                                    for(int j=6*i+2;j<=6*i+7;j++) {
                                        a[i][k++]=msg2[j];
                                    }
                                }
                                table=new JTable(a,name);
                                table.setRowHeight(20);
                                getRootPane().removeAll();
                                add(new JScrollPane(table),BorderLayout.CENTER);
                                add(pSouth,BorderLayout.SOUTH);
                                add(pNorth,BorderLayout.NORTH);
                                validate();

                            }else if(msg2[0].compareTo("failure") == 0){
                                JOptionPane.showMessageDialog(null, "读取失败");
                            }
                        }
                    }catch(Exception ee){
                        JOptionPane.showMessageDialog(null, "与服务器通信失败");
                        return;
                    }
                }
                else {
                    try {
                        Socket socket = new Socket(address, port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        out.writeUTF("$requestFINDUSER$");
                        out.writeUTF(tfsearch.getText());
                        String msg1 = in.readUTF();
                        if(msg1.compareTo("$reauestFINDUSER$") == 0){
                            String[] msg2 = in.readUTF().split("#");
                            if(msg2[0].compareTo("success") == 0){
                                JOptionPane.showMessageDialog(null, "搜索成功");
                                a=new Object[Integer.valueOf(msg2[1])][6];
                                for(int i=0;i<Integer.valueOf(msg2[1]);i++) {
                                    int k=0;
                                    for(int j=6*i+2;j<=6*i+7;j++) {
                                        a[i][k++]=msg2[j];
                                    }
                                }
                                table=new JTable(a,name);
                                table.setRowHeight(20);
                                getRootPane().removeAll();
                                add(new JScrollPane(table),BorderLayout.CENTER);
                                add(pSouth,BorderLayout.SOUTH);
                                add(pNorth,BorderLayout.NORTH);
                                validate();

                            }else if(msg2[0].compareTo("failure") == 0){
                                JOptionPane.showMessageDialog(null, "搜索失败");
                            }
                        }
                    }catch(Exception ee){
                        JOptionPane.showMessageDialog(null, "与服务器通信失败");
                        return;
                    }
                }
            }
        }
        if(bt==btnAppend) {
            int rowsNumber=table.getRowCount();
            Object b[][]=new Object[rowsNumber+1][6];
            for(int i=0;i<rowsNumber;i++) {
                for(int j=0;j<6;j++) {
                    b[i][j]=a[i][j];
                }
            }
            for(int i=0;i<6;i++)
            {
                b[rowsNumber+1][i]="0";
            }
            a=b;
            table=new JTable(a,name);
            JOptionPane.showMessageDialog(null, "请添加用户信息");
            getRootPane().removeAll();
            add(new JScrollPane(table),BorderLayout.CENTER);
            add(pSouth,BorderLayout.SOUTH);
            add(pNorth,BorderLayout.NORTH);
            validate();





        }
        if(bt==btnDelete) {
            for(int i=0;i<20;i++)
            {
                if(Duid[i]==1) {
                    Object Suid=table.getValueAt(i, 0);
                    table.removeRowSelectionInterval(i, i);
                    try {
                        Socket socket = new Socket(address, port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        out.writeUTF("$requestDELETE$");
                        out.writeUTF(String.valueOf(Suid));
                        String msg1 = in.readUTF();
                        if(msg1.compareTo("$reauestDELETE$") == 0){
                            String[] msg2 = in.readUTF().split("#");
                            if(msg2[0].compareTo("success") == 0){
                                JOptionPane.showMessageDialog(null, "删除成功");

                            }else if(msg2[0].compareTo("failure") == 0){
                                JOptionPane.showMessageDialog(null, "删除失败");
                            }
                        }
                    }catch(Exception ee){
                        JOptionPane.showMessageDialog(null, "与服务器通信失败");
                        return;
                    }
                    Duid[i]=0;
                }
            }
        }
        if(bt==btnSubmit) {
            if(a[table.getRowCount()][0]=="0") {
                JOptionPane.showMessageDialog(null, "无用户信息请添加");
                return;
            }
            else {
                try {
                    Socket socket = new Socket(address, port);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    out.writeUTF("$requestSUBMIT$");
                    out.writeUTF(String.valueOf(a[table.getRowCount()][0])+"#"+String.valueOf(a[table.getRowCount()][1])+"#"+String.valueOf(a[table.getRowCount()][2])+
                            "#"+String.valueOf(a[table.getRowCount()][3])+"#"+String.valueOf(a[table.getRowCount()][4])+"#"+String.valueOf(a[table.getRowCount()][5]));
                    String msg1 = in.readUTF();
                    if(msg1.compareTo("$reauestSUBMIT$") == 0){
                        String[] msg2 = in.readUTF().split("#");
                        if(msg2[0].compareTo("success") == 0){
                            JOptionPane.showMessageDialog(null, "添加成功");

                        }else if(msg2[0].compareTo("failure") == 0){
                            JOptionPane.showMessageDialog(null, "添加失败");
                        }
                    }
                }catch(Exception ee){
                    JOptionPane.showMessageDialog(null, "与服务器通信失败");
                    return;
                }
            }
        }
    }
    public void itemStateChanged(ItemEvent e) {
        domain=list.getSelectedItem();
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        if(arg0.getClickCount() == 2) {
            int r= table.getSelectedRow();
            Duid[r]=1;
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }


}
