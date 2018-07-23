package com.mean.csts.client.gui.listener;

import com.mean.csts.client.gui.frame.ManageWin;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

/**
 * @program: CampusSecondhandTradingSystem
 * @description:
 * @author: MeanFan
 * @create: 2018-07-22 22:13
 **/
public class ManageListener implements ActionListener,TableModelListener,WindowListener {
    private ManageWin manageWin;
    private boolean checkRowContentLegal(int row){
        //TODO User数据合法性检查
        return true;
    }
    public void sendUidDeleted(){
        try {
            Socket socket = new Socket(manageWin.address, manageWin.port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("$UserDelete$"); //请求类型
            int num = manageWin.uidDeleted.size();
            out.writeUTF(String.valueOf(num)); //请求数据
            for(int i=0;i<num;i++){
                out.writeUTF(manageWin.uidDeleted.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendUserUpdate(){
        for(int i=0;i<manageWin.rowUpdated.size();i++){
            if(!checkRowContentLegal(manageWin.rowUpdated.get(i))){
                JOptionPane.showMessageDialog(null, "数据非法,请检查，行号:"+manageWin.rowUpdated.get(i));
                break;
            }
        }
        try {
            Socket socket = new Socket(manageWin.address, manageWin.port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.writeUTF("$UserUpdate$");     //请求类型
            int num = manageWin.rowUpdated.size();
            out.writeUTF(String.valueOf(num));   //请求数据
            for(int i=0;i<num;i++){
                int row = manageWin.rowUpdated.get(i);
                String uidStr = (String) manageWin.tableModel.getValueAt(row,0);
                if(uidStr == null){
                    uidStr = String.valueOf(-2); //表示新增用户
                }
                String type = (String) manageWin.tableModel.getValueAt(row,1);
                String uname = (String) manageWin.tableModel.getValueAt(row,2);
                String nickname = (String) manageWin.tableModel.getValueAt(row,3);
                String pwd = (String) manageWin.tableModel.getValueAt(row,4);
                //String status = (String)tableModel.getValueAt(row,5);
                String walletStr =  (String) manageWin.tableModel.getValueAt(row,6);
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
    public void actionPerformed(ActionEvent e) {
        manageWin = ManageWin.getInstance();
        //JButton bt = (JButton)e.getSource();
        //System.out.println(e.toString());
        //System.out.println(manageWin.btnSearch.toString());
        if(e.getSource()==manageWin.btnSearch) {
            if(manageWin.list.getSelectedIndex() == 0){ //用户管理
                if(manageWin.tfsearch.getText().length() == 0) //为空则获取所有
                {
                    try {
                        Socket socket = new Socket(manageWin.address, manageWin.port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        out.writeUTF("$requestAllUser$");
                        out.writeUTF(String.valueOf(manageWin.currentUser.getToken()));
                        String msg1 = in.readUTF();
                        if(msg1.compareTo("$responseAllUser$") == 0){
                            String msg2 = in.readUTF();
                            int num = Integer.valueOf(msg2);
                            System.out.println(num);
                            if(num > 0){
                                manageWin.a=new Object[num][7];
                                for(int i=0;i<num;i++) {
                                    String[] strs = in.readUTF().split("#");
                                    for(int j=0;j<7;j++) {
                                        manageWin.a[i][j]=strs[j];
                                    }
                                }
                                manageWin.tableModel = new DefaultTableModel(){
                                    @Override
                                    public boolean isCellEditable(int row, int column) {
                                        if(column == 0 || column == 5){
                                            return false;
                                        }
                                        return true;
                                    }
                                };
                                manageWin.tableModel.setDataVector(manageWin.a,manageWin.name);
                                manageWin.table = new JTable(manageWin.tableModel);
                                manageWin.table.setRowHeight(20);
                                manageWin.tableModel.addTableModelListener(this);
                                //add(table,BorderLayout.CENTER);
                                //table.setEnabled(true);
                                manageWin.remove(manageWin.jScrollPane);
                                manageWin.jScrollPane = new JScrollPane(manageWin.table);
                                manageWin.add(manageWin.jScrollPane,BorderLayout.CENTER);
                                manageWin.uidDeleted = new Vector<>();
                                //rowAdded = new Vector<>();
                                manageWin.rowUpdated = new Vector<>();
                                manageWin.validate();
                            }else{
                                JOptionPane.showMessageDialog(null, "没有用户或没有权限");
                            }
                        }
                    }catch(Exception ee){
                        ee.printStackTrace();
                        JOptionPane.showMessageDialog(null, "与服务器通信失败");
                        return;
                    }
                }
                else {
                    try {
                        Socket socket = new Socket(manageWin.address, manageWin.port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        out.writeUTF("$requestSearchUser$");
                        out.writeUTF(manageWin.tfsearch.getText());
                        String msg1 = in.readUTF();
                        if(msg1.compareTo("$reauestFINDUSER$") == 0){
                            String[] msg2 = in.readUTF().split("#");
                            if(msg2[0].compareTo("success") == 0){
                                JOptionPane.showMessageDialog(null, "搜索成功");
                                manageWin.a=new Object[Integer.valueOf(msg2[1])][6];
                                for(int i=0;i<Integer.valueOf(msg2[1]);i++) {
                                    int k=0;
                                    for(int j=6*i+2;j<=6*i+7;j++) {
                                        manageWin.a[i][k++]=msg2[j];
                                    }
                                }
                                manageWin.table=new JTable(manageWin.a,manageWin.name);
                                manageWin.table.setRowHeight(20);
                                manageWin.getRootPane().removeAll();
                                manageWin.add(new JScrollPane(manageWin.table),BorderLayout.CENTER);
                                manageWin.add(manageWin.pSouth,BorderLayout.SOUTH);
                                manageWin.add(manageWin.pNorth,BorderLayout.NORTH);
                                manageWin.validate();

                            }else if(msg2[0].compareTo("failure") == 0){
                                JOptionPane.showMessageDialog(null, "搜索失败");
                            }
                        }
                    }catch(Exception ee){
                        JOptionPane.showMessageDialog(null, "与服务器通信失败");
                        return;
                    }
                }
            }else if(manageWin.list.getSelectedIndex() == 1){   //用户批准
                if(manageWin.tfsearch.getText().length() == 0)  //为空则获取所有
                {
                    try {
                        Socket socket = new Socket(manageWin.address, manageWin.port);
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        out.writeUTF("$requestAllNewUser$");
                        out.writeUTF(String.valueOf(manageWin.currentUser.getToken()));
                        String msg1 = in.readUTF();
                        if(msg1.compareTo("$responseAllNewUser$") == 0){
                            String msg2 = in.readUTF();
                            int num = Integer.valueOf(msg2);
                            System.out.println(num);
                            if(num > 0){
                                manageWin.a2=new Object[num][3];
                                for(int i=0;i<num;i++) {
                                    String[] strs = in.readUTF().split("#");
                                    for(int j=0;j<3;j++) {
                                        manageWin.a2[i][j]=strs[j];
                                    }
                                }
                                manageWin.tableModel = new DefaultTableModel(){
                                    @Override
                                    public boolean isCellEditable(int row, int column) {
                                        if(column == 3){
                                            return true;
                                        }
                                        return false;
                                    }
                                };
                                manageWin.tableModel.setDataVector(manageWin.a2,manageWin.name2);
                                manageWin.table = new JTable(manageWin.tableModel);
                                manageWin.table.setRowHeight(20);
                                manageWin.tableModel.addTableModelListener(this);
                                //add(table,BorderLayout.CENTER);
                                //table.setEnabled(true);
                                manageWin.remove(manageWin.jScrollPane);
                                manageWin.jScrollPane = new JScrollPane(manageWin.table);
                                manageWin.add(manageWin.jScrollPane,BorderLayout.CENTER);
                                manageWin.uidRefused = new Vector<>();
                                manageWin.uidApproved = new Vector<>();
                                manageWin.validate();
                            }else{
                                manageWin.remove(manageWin.jScrollPane);
                                manageWin.validate();
                                JOptionPane.showMessageDialog(null, "没有用户或没有权限");
                            }
                        }
                    }catch(Exception ee){
                        JOptionPane.showMessageDialog(null, "与服务器通信失败");
                        ee.printStackTrace();
                        return;
                    }
                }
            }

        }
        else if(e.getSource()== manageWin.btnAdd) {
            if(manageWin.table != null){
                Vector<String[]> dataVector=new Vector<String[]>();
                manageWin.tableModel.addRow(dataVector);
                int count=manageWin.table.getRowCount();                    //获得总行数
                manageWin.table.requestFocus();
                manageWin.table.setRowSelectionInterval(count-1, count-1);  //最后一行获得焦点
            }
        }
        else if(e.getSource() == manageWin.btnDelete) {
            int selectedRow = manageWin.table.getSelectedRow();
            if (selectedRow != -1) {
                String uidStr = (String) manageWin.tableModel.getValueAt(selectedRow, 0);
                if (uidStr != null) {                             //原行删除
                    int uid = Integer.valueOf(uidStr);
                    if (!manageWin.uidDeleted.contains(uid)) {
                        manageWin.uidDeleted.add(uid);
                    }
                } else {
                    manageWin.rowUpdated.removeElement(selectedRow);         //新行删除
                }
                for (int i = 0; i < manageWin.rowUpdated.size(); i++) {       //rowUpdate后续行号更新
                    if (manageWin.rowUpdated.get(i) > selectedRow) {
                        manageWin.rowUpdated.set(i, manageWin.rowUpdated.get(i) - 1);
                    }
                }
                manageWin.tableModel.removeRow(selectedRow);
            }
        }
        else if(e.getSource() == manageWin.btnSubmit){
            System.out.println("uidDeleted:");
            for(int i=0;i<manageWin.uidDeleted.size();i++){
                System.out.println(manageWin.uidDeleted.get(i));
            }
            System.out.println("rowUpdated:");
            for(int i=0;i<manageWin.rowUpdated.size();i++){
                System.out.println(manageWin.rowUpdated.get(i));
            }
            sendUidDeleted();      //发送删除用户请求
            sendUserUpdate();     //发送用户更新请求
        }else if(manageWin.list == e.getSource()){
            //System.out.println(list.getSelectedItem());
            int selected = manageWin.list.getSelectedIndex();
            if(selected == 0){
                manageWin.btnAdd.setVisible(true);
                manageWin.btnDelete.setVisible(true);
                manageWin.btnSubmit.setVisible(true);
                manageWin.btnApprove.setVisible(false);
                manageWin.btnRefuse.setVisible(false);
                manageWin.validate();

            }else if(selected == 1){
                manageWin.btnAdd.setVisible(false);
                manageWin.btnDelete.setVisible(false);
                manageWin.btnSubmit.setVisible(false);
                manageWin.btnApprove.setVisible(true);
                manageWin.btnRefuse.setVisible(true);
                manageWin.validate();

            }
        }else if(e.getSource() == manageWin.btnApprove){
            int row = manageWin.table.getSelectedRow();
            if(row == -1){ //合法性检查
                JOptionPane.showMessageDialog(null, "请先在列表中选中用户");
            }else{
                String uidStr = (String) manageWin.tableModel.getValueAt(row, 0);
                String type = (String) manageWin.tableModel.getValueAt(row,3);
                if(type == null || type == ""){
                    type = "normal";
                }
                try{
                    Socket socket = new Socket(manageWin.address, manageWin.port);
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("$NewUserApprove$");      //请求类型
                    out.writeUTF(uidStr+"#"+type);         //请求数据,uid和用户类型
                    String str = in.readUTF();
                    if(str.compareTo("$NewUserApprove$") == 0) { //回复类型
                        String msg2 = in.readUTF();
                        if (msg2.compareTo("success") == 0) {   //回复数据判断处理
                            JOptionPane.showMessageDialog(null, "批准操作成功");
                            manageWin.tableModel.removeRow(row);
                        } else if (msg2.compareTo("failure") == 0) {
                            JOptionPane.showMessageDialog(null, "批准操作失败");
                        }
                    }
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        }else if(e.getSource() == manageWin.btnRefuse){
            int row = manageWin.table.getSelectedRow();
            if(row == -1){//合法性检查
                JOptionPane.showMessageDialog(null, "请先在列表中选中用户");
            }else{
                String uidStr = (String) manageWin.tableModel.getValueAt(row, 0);
                try {
                    Socket socket = new Socket(manageWin.address, manageWin.port);
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("$NewUserRefuse$");//请求类型
                    out.writeUTF(uidStr);//请求数据,uid
                    String str = in.readUTF();
                    if(str.compareTo("$NewUserRefuse$") == 0){//回复类型
                        String str2 = in.readUTF();
                        if(str2.compareTo("success") == 0){//回复数据判断处理
                            JOptionPane.showMessageDialog(null, "拒绝操作成功");
                        } else if (str2.compareTo("failure") == 0) {
                            JOptionPane.showMessageDialog(null, "拒绝操作失败");
                        }
                    }
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        if (manageWin.list.getSelectedIndex() == 0) {
            if (e.getType() == TableModelEvent.INSERT) {

            } else if (e.getType() == TableModelEvent.UPDATE) { //包括了插入(有效插入要求用户update)
                int row = manageWin.table.getSelectedRow();
                System.out.println("UPDATE row:" + row);
                if (!manageWin.rowUpdated.contains(row)) {
                    manageWin.rowUpdated.add(row);
                }
            } else if (e.getType() == TableModelEvent.DELETE) {

            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        AccountListener.logout(manageWin.address,manageWin.port,manageWin.currentUser.getUname());
        manageWin.dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
