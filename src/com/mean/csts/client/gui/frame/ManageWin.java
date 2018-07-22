package com.mean.csts.client.gui.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.mean.csts.client.gui.listener.ManageListener;
import com.mean.csts.client.gui.panel.AccountPanel;
import com.mean.csts.data.User;

public class ManageWin  extends BasicWin{
    private static ManageWin instance  = null;
    private static String title = "校园二手商品交易平台-管理";
    private static int winWedth = 520;
    private static int winHeight = 750;
    public JPanel pSouth,pNorth;
    private Box boxH1,boxH2;
    public JComboBox list;
    public JScrollPane jScrollPane;
    public JTextField tfsearch;
    public JButton btnSearch, btnAdd, btnDelete,btnSubmit,btnApprove,btnRefuse;
    public JTable table;
    public DefaultTableModel tableModel;
    public Vector<Integer> uidDeleted;
    //private Vector<Integer> rowAdded;
    public Vector<Integer> rowUpdated;
    public Vector<Integer>uidApproved;
    public Vector<Integer>uidRefused;
    public Object a[][];
    public Object a2[][];
    public Object name[]= {"UID","用户类型","用户名","昵称","密码","状态","钱包"};
    public Object name2[]= {"UID","用户名","昵称","设置权限（用户类型）"};
    public InetAddress address;
    public int port;
    public User currentUser;


    public static ManageWin getInstance() {
        if(instance == null){
            instance = new ManageWin();
        }
        return instance;
    }
    private ManageWin() {
        super(title,winWedth,winHeight);
        btnSearch=new JButton("查询全部");
        btnAdd =new JButton("添加");
        btnDelete =new JButton("删除");
        btnSubmit=new JButton("提交");
        btnApprove= new JButton("批准");
        btnRefuse = new JButton("拒绝");
        btnSearch.addActionListener(new ManageListener());
        btnAdd.addActionListener(new ManageListener());
        btnDelete.addActionListener(new ManageListener());
        btnSubmit.addActionListener(new ManageListener());
        btnApprove.addActionListener(new ManageListener());
        btnRefuse.addActionListener(new ManageListener());
        btnApprove.setVisible(false);
        btnRefuse.setVisible(false);
        list=new JComboBox();
        list.addItem("用户批量管理");
        list.addItem("注册用户批准");
        list.addActionListener(new ManageListener());
        tfsearch=new JTextField(20);
        tfsearch.setText("");
        tfsearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //System.out.println("!!!");
                if(tfsearch.getText().trim().equals("")){
                    btnSearch.setText("查询全部");
                }else{
                    btnSearch.setText("    查询    ");
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
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
        this.address = super.ADDRESS;
        this.port = super.PORT;
        addWindowListener(new ManageListener());
    }
    public void setCurrentUser(User user){
        this.currentUser = user;
    }
}

