package com.mean.csts.server;

import com.mean.csts.data.Goods;
import com.mean.csts.data.User;
import com.mean.csts.data.UserNew;
import com.mean.csts.data.UserNormal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;

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
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String clientInStr = in.readUTF();
            switch(clientInStr){
                case "$register$": {
                    String registerInfo = in.readUTF(); //获取注册信息
                    String[] strs = registerInfo.split("#");
                    User newUser = new User();
                    newUser.setType(UserNew.Type);
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
                                user.getUname() + "#"+
                                user.getNickname() + "#"+
                                String.valueOf(user.getToken())+ "#"+
                                String.valueOf(user.getWallet()));
                    }else if(user.getStatus().compareTo("loginF1") == 0){
                        out.writeUTF("$login$");
                        out.writeUTF("failure#user_err");
                    }else if(user.getStatus().compareTo("loginF2") == 0){
                        out.writeUTF("$login$");
                        out.writeUTF("failure#pwd_err");
                    }else if(user.getStatus().compareTo("loginF3") == 0){
                        out.writeUTF("$login$");
                        out.writeUTF("failure#user_new");
                    }
                    break;
                }
                case "$getGoods$": {
                    System.out.println("收到请求：getGoods");
                    String str = in.readUTF();
                    String[] strs = str.split("#");
                    int page = Integer.valueOf(strs[0]);
                    int num = Integer.valueOf(strs[1]);
                    System.out.println("page:"+page+",num:"+num);
                    Goods[] goods = SQLOperator.getGoods(connection,page,num);
                    System.out.println("resultSetGot");
                    int i=0;
                    if(goods == null){
                        out.writeUTF("$GoodsInfo$");
                        out.writeUTF("failure#none");
                    }else{
                        for(;i<num;i++){
                            if(goods[i] == null)
                                break;
                        }
                        int rstNum = i;
                        try {
                            out.writeUTF("$GoodsInfo$");
                            if(rstNum == 0) {
                                out.writeUTF("failure#none");
                            }else{
                                out.writeUTF("success#"+String.valueOf(rstNum));
                                for(i=0;i<rstNum;i++){
                                    System.out.println(goods[i].toString());
                                    //out.write(goods[i].getImage());
                                    out.writeUTF(String.valueOf(goods[i].getGid()) + "#" +
                                            goods[i].getName() + "#" +
                                            String.valueOf(goods[i].getAmount())+ "#" +
                                            String.valueOf(goods[i].getPrice()) + "#" +
                                            goods[i].getContent() + "#" +
                                            goods[i].getGid()
                                    );
                                    System.out.println("rstSent:"+rstNum);
                                }
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }

                    break;
                }
                case "$purchaseRequest$": {
                    String msg = in.readUTF();
                    String[] strs = msg.split("#");
                    int token = Integer.valueOf(strs[0]);
                    User user = SQLOperator.loginViladate(connection,token);
                    int gid = Integer.valueOf(strs[1]);
                    int num = Integer.valueOf(strs[2]);
                    Goods goods = SQLOperator.getGoodsByGid(connection,gid);
                    out.writeUTF("$purchaseResponse$");
                    if(user == null) { //未登录或登录过期
                        out.writeUTF("failure#no_user");
                    }if(goods == null){ //商品未找到
                        out.writeUTF("failure#none");
                    }else if(goods.getAmount()<num){
                        out.writeUTF("failure#lack_stock");
                    }else{
                        double userWalletRest = user.getWallet() - goods.getPrice()*num;
                        if(userWalletRest<0.0){
                            out.writeUTF("failure#lack_wallet");
                        }else{
                            user.setWallet(userWalletRest);
                            SQLOperator.setUserWallet(connection,user.getUid(),user.getWallet());
                            goods.setAmount(goods.getAmount()-num);
                            if(goods.getAmount() == 0){
                                SQLOperator.deleteGoods(connection,goods.getGid());
                            }else {
                                SQLOperator.setGoodsAmount(connection, goods.getGid(), goods.getAmount());
                            }
                            out.writeUTF("success#"+user.getWallet());
                        }
                    }
                    break;
                }
                case "$GoodsStock$":{
                    int token = Integer.valueOf(in.readUTF());
                    byte[] buf=new byte[3145728];
                    //in.read(buf);
                    String str = in.readUTF();
                    String[] strs = str.split("#");
                    Goods goods = new Goods();
                    goods.setName(strs[0]);
                    goods.setAmount(Integer.valueOf(strs[1]));
                    goods.setPrice(Double.valueOf(strs[2]));
                    goods.setContent(strs[3]);
                    User user = SQLOperator.loginViladate(connection,token);
                    if(user == null){
                        System.out.println("商品上架失败,用户验证失败");
                        out.writeUTF("$GoodsStock$");
                        out.writeUTF("failure");
                    }else{
                        goods.setUid(user.getUid());
                        SQLOperator.insertGoods(connection,goods,buf,user.getUid());
                        System.out.println("商品上架成功");
                        out.writeUTF("$GoodsStock$");
                        out.writeUTF("success");
                    }
                    break;
                }
                case "$requestAllUser$":{
                    int num;
                    User[] users;
                    users = SQLOperator.getAllUser(connection);
                    if(users == null){
                        num = 0;
                    }else {
                        num = users.length;
                    }
                    out.writeUTF("$requestAllUser$");
                    out.writeUTF(String.valueOf(num));
                    for(int i=0;i<num;i++) {
                        if(users[i]==null) {
                            users[i] = new User();
                        }
                        out.writeUTF(users[i].getUid()+"#"+
                                users[i].getType()+"#"+
                                users[i].getUname()+"#"+
                                users[i].getNickname()+"#"+
                                users[i].getPwd()+"#"+
                                users[i].getStatus()+"#"+
                                users[i].getWallet()
                        );
                        System.out.println("user sent");
                    }
                    break;
                }
                case "$UserDelete$":{
                    Vector<Integer> deleteFailUid= new Vector<>();
                    String numStr = in.readUTF();
                    Integer num = Integer.valueOf(numStr);
                    System.out.println("UserDelete num:"+num);
                    for(int i=0;i<num;i++){
                        String str = in.readUTF();
                        int deleteUid = Integer.valueOf(str);
                        System.out.println("deleteUid:"+deleteUid);
                        if(!SQLOperator.deleteUser(connection, deleteUid)){
                            deleteFailUid.add(deleteUid);
                        }
                    }
                    break;
                }
                case "$UserUpdate$":{
                    String numStr = in.readUTF();
                    Integer num = Integer.valueOf(numStr);
                    System.out.println("UserUpdate num:"+num);
                    User[] users = new User[num];
                    for(int i=0;i<num;i++){
                        String[] strs = in.readUTF().split("#");
                        int uid = Integer.valueOf(strs[0]);
                        String type = strs[1];
                        String uname = strs[2];
                        String nickname = strs[3];
                        String pwd = strs[4];
                        double wallet = Double.valueOf(strs[5]);
                        users[i] = new User(uid,type,uname,nickname,pwd,0,null,wallet);
                        SQLOperator.updateUser(connection,users[i]);
                    }
                    break;
                }
                case "$requestAllNewUser$":{
                    int num;
                    User[] users;
                    users = SQLOperator.getAllUnapprovedUser(connection);
                    if(users == null){
                        num = 0;
                    }else {
                        num = users.length;
                    }
                    out.writeUTF("$requestAllNewUser$");
                    out.writeUTF(String.valueOf(num));
                    for(int i=0;i<num;i++) {
                        if(users[i]==null) {
                            users[i] = new User();
                        }
                        out.writeUTF(users[i].getUid()+"#"+
                                users[i].getUname()+"#"+
                                users[i].getNickname()
                        );
                        System.out.println("unapprovedUser sent");
                    }
                    break;
                }
                case "$NewUserApprove$":{
                    String[] strs = in.readUTF().split("#");
                    int uid = Integer.valueOf(strs[0]);
                    String type = strs[1];
                    if(SQLOperator.setUserType(connection,uid,type)){
                        out.writeUTF("$NewUserApprove$");
                        out.writeUTF("success");
                    }else{
                        out.writeUTF("$NewUserApprove$");
                        out.writeUTF("failure");
                    }
                }
                case "$NewUserRefuse$":{
                    String strs = in.readUTF();
                    int uid = Integer.valueOf(strs);
                    if(SQLOperator.deleteUser(connection,uid)){
                        out.writeUTF("$NewUserRefuse$");
                        out.writeUTF("success");
                    }else{
                        out.writeUTF("$NewUserRefuse$");
                        out.writeUTF("failure");
                    }
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
                //如果number产生的是数字0
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
