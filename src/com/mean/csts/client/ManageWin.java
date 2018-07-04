package com.mean.csts.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.mean.csts.data.User;

public class ManageWin  extends BasicWin implements ActionListener,TableModelListener {
    public static String title = "校园二手商品交易平台-管理";
    public static int winWedth = 520;
    public static int winHeight = 750;
    private JPanel pSouth,pNorth;
    private Box boxH1,boxH2;
    private JComboBox list;
    private JScrollPane jScrollPane;
    private JTextField tfsearch;
    private JButton btnSearch, btnAdd, btnDelete,btnSubmit,btnApprove,btnRefuse;
    private JTable table;
    public Object domain;
    private DefaultTableModel tableModel;
    private Vector<Integer> uidDeleted;
    //private Vector<Integer> rowAdded;
    private Vector<Integer> rowUpdated;
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
        btnAdd =new JButton("添加");
        btnDelete =new JButton("删除");
        btnSubmit=new JButton("提交");
        btnApprove= new JButton("批准");
        btnRefuse = new JButton("拒绝");
        btnSearch.addActionListener(this);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);
        btnSubmit.addActionListener(this);
        btnApprove.addActionListener(this);
        btnRefuse.addActionListener(this);
        btnApprove.setVisible(false);
        btnRefuse.setVisible(false);
        list=new JComboBox();
        list.addItem("用户批量管理");
        list.addItem("注册用户批准");
        list.addActionListener(this);
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
        boxH2.add(btnAdd);
        boxH2.add(btnApprove);
        boxH2.add(Box.createHorizontalStrut(16));
        boxH2.add(btnDelete);
        boxH2.add(btnRefuse);
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
        //JButton bt = (JButton)e.getSource();
        if(e.getSource()==btnSearch) {
            if(list.getSelectedIndex() == 0){ //用户管理
                if(tfsearch.getText().length() == 0) //为空则获取所有
                {
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
                                tableModel = new DefaultTableModel(){
                                    @Override
                                    public boolean isCellEditable(int row, int column) {
                                        if(column == 0 || column == 5){
                                            return false;
                                        }
                                        return true;
                                    }
                                };
                                tableModel.setDataVector(a,name);
                                table = new JTable(tableModel);
                                table.setRowHeight(20);
                                tableModel.addTableModelListener(this);
                                //add(table,BorderLayout.CENTER);
                                //table.setEnabled(true);
                                remove(jScrollPane);
                                jScrollPane = new JScrollPane(table);
                                add(jScrollPane,BorderLayout.CENTER);
                                uidDeleted = new Vector<>();
                                //rowAdded = new Vector<>();
                                rowUpdated = new Vector<>();
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
            }else if(list.getSelectedIndex() == 1){ //用户批准
                
            }

        }
        else if(e.getSource()== btnAdd) {
            if(table != null){
                Vector<String[]> dataVector=new Vector<String[]>();
                tableModel.addRow(dataVector);
                int count=table.getRowCount();//获得总行数
                table.requestFocus();
                table.setRowSelectionInterval(count-1, count-1);//最后一行获得焦点
            }
        }
        else if(e.getSource() == btnDelete) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String uidStr = (String) tableModel.getValueAt(selectedRow, 0);
                if (uidStr != null) { //原行删除
                    int uid = Integer.valueOf(uidStr);
                    if (!uidDeleted.contains(uid)) {
                        uidDeleted.add(uid);
                    }
                } else {
                    rowUpdated.removeElement(selectedRow); //新行删除
                }
                for (int i = 0; i < rowUpdated.size(); i++) { //rowUpdate后续行号更新
                    if (rowUpdated.get(i) > selectedRow) {
                        rowUpdated.set(i, rowUpdated.get(i) - 1);
                    }
                }
                tableModel.removeRow(selectedRow);
            }
        }
        else if(e.getSource() == btnSubmit){
            System.out.println("uidDeleted:");
            for(int i=0;i<uidDeleted.size();i++){
                System.out.println(uidDeleted.get(i));
            }
            System.out.println("rowUpdated:");
            for(int i=0;i<rowUpdated.size();i++){
                System.out.println(rowUpdated.get(i));
            }
            sendUidDeleted();
            sendUserUpdate();
        }else if(list == e.getSource()){
            //System.out.println(list.getSelectedItem());
            int selected = list.getSelectedIndex();
            if(selected == 0){
                btnAdd.setVisible(true);
                btnDelete.setVisible(true);
                btnSubmit.setVisible(true);
                btnApprove.setVisible(false);
                btnRefuse.setVisible(false);

                validate();

            }else if(selected == 1){
                btnAdd.setVisible(false);
                btnDelete.setVisible(false);
                btnSubmit.setVisible(false);
                btnApprove.setVisible(true);
                btnRefuse.setVisible(true);

                validate();

            }
        }
    }
    private boolean checkRowContentLegal(int row){
        //TODO User数据合法性检查
        return true;
    }
    private void sendUidDeleted(){
        try {
            Socket socket = new Socket(address, port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("$UserDelete$");
            int num = uidDeleted.size();
            out.writeUTF(String.valueOf(num));
            for(int i=0;i<num;i++){
                out.writeUTF(uidDeleted.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendUserUpdate(){
        for(int i=0;i<rowUpdated.size();i++){
            if(!checkRowContentLegal(rowUpdated.get(i))){
                JOptionPane.showMessageDialog(null, "数据非法,请检查，行号:"+rowUpdated.get(i));
                break;
            }
        }

        try {
            Socket socket = new Socket(address, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("$UserUpdate$");
            int num = rowUpdated.size();
            out.writeUTF(String.valueOf(num));
            for(int i=0;i<num;i++){
                int row = rowUpdated.get(i);
                String uidStr = (String) tableModel.getValueAt(row,0);
                if(uidStr == null){
                    uidStr = String.valueOf(-2); //表示新增用户
                }
                String type = (String)tableModel.getValueAt(row,1);
                String uname = (String)tableModel.getValueAt(row,2);
                String nickname = (String)tableModel.getValueAt(row,3);
                String pwd = (String)tableModel.getValueAt(row,4);
                //String status = (String)tableModel.getValueAt(row,5);
                String walletStr =  (String)tableModel.getValueAt(row,6);
                out.writeUTF(uidStr + "#" +
                                 type  + "#" +
                                 uname + "#" +
                                 nickname + "#" +
                                 pwd + "#" +
                                 walletStr
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getType() == TableModelEvent.INSERT){

        } else if(e.getType() == TableModelEvent.UPDATE){ //包括了插入(有效插入要求用户update)
            int row = table.getSelectedRow();
            System.out.println("UPDATE row:"+row);
            if(!rowUpdated.contains(row)){
                rowUpdated.add(row);
            }
        }else if(e.getType() == TableModelEvent.DELETE){

        }
    }
}

