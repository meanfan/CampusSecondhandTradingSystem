package com.mean.csts.server;

import com.mean.csts.User;
import com.mean.csts.UserRegistered;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.logging.SocketHandler;

public class MsgHandlerThread implements Runnable{
    private Socket socket;

    public MsgHandlerThread(Socket client){
        socket = client;
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
                    User newUser = new User();
                    newUser.setType(UserRegistered.TypeID);
                    newUser.setUname(strs[0]);
                    newUser.setNickname(strs[1]);
                    newUser.setPwd(strs[2]);
                    out.writeUTF("$register$");
                    out.writeUTF("success");
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
