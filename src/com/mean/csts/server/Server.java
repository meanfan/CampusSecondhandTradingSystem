package com.mean.csts.server;


import com.mean.csts.User;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    public static final int PORT = 23333;
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/csts?useSSL=false&serverTimeZone=Asia/Shanghai";
    private static final String CONNECTION_ACCOUNT = "csts_server";
    private static final String CONNECTION_PWD = "sR8yicaW4XDKV2HC";
    public static List<User> usrlst = new ArrayList<>();
    public static HashMap<String,String> usrLoggedin = new HashMap<>();
    protected Connection connection;
    protected Statement statement;
    public void init(){
        System.out.println("---  初始化 ---");
        try {
            System.out.println("连接到数据库...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(CONNECTION_URL, CONNECTION_ACCOUNT, CONNECTION_PWD);
            if(!connection.isClosed()) {
                System.out.println("数据库连接成功");
            }else{
                System.out.println("数据库连接已关闭");
                System.exit(1);
            }
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("开始监听请求...");
        try {
            ServerSocket serversocket = new ServerSocket(PORT);
            while(true){
                //客户端没有连接请求时阻塞
                Socket client = serversocket.accept();
                //阻塞消除，处理连接请求
                System.out.println("处理请求...");
                new MsgHandlerThread(client, connection);
            }
        }catch (Exception e){
            System.out.println("Socket异常:"+ e.getMessage());
        }
    }
    public static void closeConn(Connection conn) {
        if (null != conn) {
            try {
                    conn.close();
            } catch (SQLException e) {
                System.out.println("关闭数据库连接失败！");
                e.printStackTrace();
                }
        }
    }
    public static void main(String[] args){
        System.out.println("--- 服务器启动 ---");
        Server server = new Server();
        server.init();
    }

}
