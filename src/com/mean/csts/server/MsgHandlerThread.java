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
import java.util.Random;
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
                    String[] strs = registerInfo.split("#");
                    User newUser = new User();
                    newUser.setType(UserRegistered.Type);
                    newUser.setUname(strs[0]);
                    newUser.setNickname(strs[1]);
                    newUser.setPwd(strs[2]);
                    //SQLOperator.selectUser(connection,newUser);
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
                    String loginInfo = in.readUTF();
                    String[] strs = loginInfo.split("#");
                    User user = new User();
                    user.setUname(strs[0]);
                    user.setPwd(strs[1]);
                    user = SQLOperator.loginAuth(connection,user);
                    if(user.getStatus().compareTo("online") == 0){
                        SQLOperator.setUserStatus(connection,user.getUid(),"online");
                        String str = user.getUname()+ getRandomString(16);
                        int token = str.hashCode();
                        user.setToken(token);
                        SQLOperator.setUserToken(connection,user.getUid(),token);
                        out.writeUTF("$login$");
                        out.writeUTF("success#"+
                                String.valueOf(user.getUid()) + "#" +
                                user.getType() + "#"+
                                user.getNickname() + "#"+
                                user.getToken());
                    }else if(user.getStatus().compareTo("loginF1") == 0){
                        out.writeUTF("$login$");
                        out.writeUTF("failure#user_err");
                    }else if(user.getStatus().compareTo("loginF2") == 0){
                        out.writeUTF("$login$");
                        out.writeUTF("failure#pwd_err");
                    }
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
    public static String getRandomString(int length){
        //产生随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0; i<length; i++){
            //产生0-2个随机数，既与a-z，A-Z，0-9三种可能
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                //如果number产生的是数字0；
                case 0:
                    //产生A-Z的ASCII码
                    result=Math.round(Math.random()*25+65);
                    //将ASCII码转换成字符
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    //产生a-z的ASCII码
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    //产生0-9的数字
                    sb.append(String.valueOf
                            (new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
}
