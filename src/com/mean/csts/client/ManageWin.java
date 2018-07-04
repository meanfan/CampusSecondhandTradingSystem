package com.mean.csts.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mean.csts.data.User;

public class ManageWin  extends BasicWin implements ActionListener, ItemListener{
    public static String title = "校园二手商品交易平台-管理";
    public static int winWedth = 520;
    public static int winHeight = 750;
    private JPanel pSouth,pNorth;
    private Box boxH1,boxH2;
    private JComboBox list;
    private JScrollPane jScrollPane;
    private JTextField tfsearch;
    private JButton btnSearch, btnAddRow, btnDeleteRow,btnSubmit;
    private JTable table;
    public Object domain;
    private DefaultTableModel tableModel;
    private Vector<User> userDeleted;
    private Vector<User> userAdded;
    private Vector<User> userUpdated;
    Object a[][];
    Object name[]= {"UID","用户类型","用户名","昵称","密码","状态","钱包"};
    private int Duid[]=new int[20];
    public InetAddress address;
    public int port;
    public User user;
    public ManageWin(InetAddress address,int port) {
        super(title,winWedth,winHeight);
        for(int i=0;i<20;i++)
        {
            Duid[i]=0;
        }
        btnSearch=new JButton("搜索");
        btnAddRow =new JButton("添加");
        btnDeleteRow =new JButton("删除");
        btnSubmit=new JButton("提交");
        btnSearch.addActionListener(this);
        btnAddRow.addActionListener(this);
        btnDeleteRow.addActionListener(this);
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
        boxH2.add(btnAddRow);
        boxH2.add(Box.createHorizontalStrut(16));
        boxH2.add(btnDeleteRow);
        boxH2.add(Box.createHorizontalStrut(16));
        boxH2.add(btnSubmit);
        pSouth.add(boxH2);
        table=new JTable();
        add(pSouth,BorderLayout.SOUTH);
        add(pNorth,BorderLayout.NORTH);
        jScrollPane = new JScrollPane(new JLabel("空"));
        //jScrollPane.setSize(500,600);
        //jScrollPane.add();
        add(jScrollPane,BorderLayout.CENTER);
        this.address = address;
        this.port = port;
        tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0){
                    return false;
                }
                return true;
            }
        };
        validate();
        setVisible(true);
    }
    public static void main(String[] args){
        try {
            new ManageWin(InetAddress.getByName("localhost"),2333);
        } catch (UnknownHostException e) { e.printStackTrace(); }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bt = (JButton)e.getSource();
        if(bt==btnSearch) {
            if(tfsearch.getText().length() == 0) //为空则获取所有
            {
                System.out.println("~~~~");
                try {
                    Socket socket = new Socket(address, port);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    out.writeUTF("$requestAllUser$");
                    String msg1 = in.readUTF();
                    if(msg1.compareTo("$requestAllUser$") == 0){
                        String msg2 = in.readUTF();
                        int num = Integer.valueOf(msg2);
                        System.out.println(num);
                        if(num > 0){
                            a=new Object[num][7];
                            for(int i=0;i<num;i++) {
                                String[] strs = in.readUTF().split("#");
                                for(int j=0;j<7;j++) {
                                    a[i][j]=strs[j];
                                }
                            }
                            tableModel.setDataVector(a,name);
                            table = new JTable(tableModel);
                            table.setRowHeight(20);
                            //add(table,BorderLayout.CENTER);
                            //table.setEnabled(true);
                            remove(jScrollPane);
                            jScrollPane = new JScrollPane(table);
                            add(jScrollPane,BorderLayout.CENTER);
                            validate();
                        }else{
                            JOptionPane.showMessageDialog(null, "没有用户");
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
                    out.writeUTF("$requestSearchUser$");
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
        if(bt== btnAddRow) {
            if(table != null){
                Vector<String[]> dataVector=new Vector<String[]>();
                tableModel.addRow(dataVector);
                int count=table.getRowCount();//获得总行数
                table.requestFocus();
                table.setRowSelectionInterval(count-1, count-1);//最后一行获得焦点
            }
        }
        if(bt== btnDeleteRow) {
            int selectedRow = table.getSelectedRow();
            if(selectedRow != -1){

            }
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
}

