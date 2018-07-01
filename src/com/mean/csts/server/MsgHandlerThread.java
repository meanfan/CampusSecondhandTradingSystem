package com.mean.csts.server;

import com.mean.csts.User;
import com.mean.csts.UserRegistered;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.SocketHandler;

public class MsgHandlerThread implements Runnable{
    private Socket socket;
    private Connection connection;
    private Statement statement;

    public MsgHandlerThread(Socket client, Connection connection){
        socket = client;
        this.connection = connection;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("数据库获取statement失败");
        }
        new Thread(this).start();
        System.out.println("线程调用处理");
    }

    @Override
    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            // 向客户端回复信息的流
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String clientInStr = in.readUTF();
            switch(clientInStr){
                case "$register$": {
                    String registerInfo = in.readUTF(); //获取注册信息
                    String[] strs = registerInfo.split("|");
                    //uname查重
                    //String sql = "select * from user";
                    //ResultSet rst = statement.executeQuery(sql);

                    User newUser = new User();
                    newUser.setType(UserRegistered.Type);
                    newUser.setUname(strs[0]);
                    newUser.setNickname(strs[1]);
                    newUser.setPwd(strs[2]);
                    if(SQLOperator.insertUser(connection,newUser)) {
                        out.writeUTF("$register$");
                        out.writeUTF("success");
                    }else{
                        out.writeUTF("$register$");
                        out.writeUTF("failure");
                    }
                    break;
                }
                case "$login$": {

                    break;
                }
                case "$getGoodsList$": {
                    break;
                }
                case "$purchaseRequest$": {
                    break;
                }

            }
        }catch(Exception e){

        }
    }
}
