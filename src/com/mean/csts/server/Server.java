package com.mean.csts.server;


import com.mean.csts.User;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class Server {
    public static final int PORT = 23333;
    public static List<User> usrlst = new ArrayList<>();
    public void init(){
        try {
            ServerSocket serversocket = new ServerSocket(PORT);
            while(true){
                //客户端没有连接请求时阻塞
                Socket client = serversocket.accept();
                //阻塞消除，处理连接请求
                new MsgHandlerThread(client);
            }
        }catch (Exception e){
            System.out.println("Socket异常:"+ e.getMessage());
        }
    }

    public static void main(String[] args){
        System.out.println("--- Server start ---");
        Server server = new Server();
        server.init();
    }

}
