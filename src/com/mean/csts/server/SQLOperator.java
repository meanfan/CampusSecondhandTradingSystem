package com.mean.csts.server;

import com.mean.csts.data.Goods;
import com.mean.csts.data.User;

import java.sql.*;

public class SQLOperator {
    public static boolean insertUser(Connection connection, User user){ //添加用户
        String sql = "insert into user (type,username,nickname,password)" + "values(?,?,?,?)";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setString(1,user.getType());
            presta.setString(2,user.getUname());
            presta.setString(3,user.getNickname());
            presta.setString(4,user.getPwd());
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:用户添加成功");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:用户添加失败");
            //e.printStackTrace();
            return false;
        }
    }
    public static boolean updateUser(Connection connection, User user){ //更新用户
        if(user.getUid() == -2){ //新增用户
            String sql = "insert into user (type,username,nickname,password,wallet)" + "values(?,?,?,?,?)";
            try {
                //预处理sql语句
                PreparedStatement presta = connection.prepareStatement(sql);
                //设置语句value值
                presta.setString(1,user.getType());
                presta.setString(2,user.getUname());
                presta.setString(3,user.getNickname());
                presta.setString(4,user.getPwd());
                presta.setDouble(5,user.getWallet());
                //执行sql语句
                presta.execute();
                System.out.println("SQLO:用户新增成功");
                    return true;
            } catch (SQLException e) {
                System.out.println("SQLO:用户新增失败");
                //e.printStackTrace();
                return false;
        }
        }else { //更新用户
            try{
            Statement statement= connection.createStatement();
            try{
                ResultSet resultSet = statement.executeQuery("select * from goods where uid="+user.getUid());
                resultSet.next();
            }catch (SQLException e){
                System.out.println("SQLO:用户更新失败，用户不存在");
                e.printStackTrace();
                return false;
            }
            statement.executeUpdate("update user SET type ='"+user.getType()+"' where uid="+user.getUid());
            statement.executeUpdate("update user SET username ='"+user.getUname()+"' where uid="+user.getUid());
            statement.executeUpdate("update user SET nickname ='"+user.getNickname()+"' where uid="+user.getUid());
            statement.executeUpdate("update user SET password ='"+user.getPwd()+"' where uid="+user.getUid());
            statement.executeUpdate("update user SET wallet ="+user.getWallet()+" where uid="+user.getUid());
            return true;
            }catch(SQLException e){
                System.out.println("SQLO:用户更新失败");
                e.printStackTrace();
                return  false;
            }
        }
    }
    public static boolean insertGoods(Connection connection, Goods goods,byte[] image,int uid){  //添加商品
        String sql = "insert into goods (name,amount,image,content,price,uid)" + "values(?,?,?,?,?,?)";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setString(1,goods.name);
            presta.setInt(2,goods.amount);

            presta.setBytes(3,image);
            presta.setString(4,goods.content);
            presta.setDouble(5,goods.price);
            presta.setInt(6,uid);
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:商品添加成功");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:商品添加失败");
            e.printStackTrace();
            return false;
        }
    }
    public static Goods[] getGoods(Connection connection, int page, int limit){          //获得商品
        int count=0;
        int totalPages=0;
        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select count(*) as num from goods"); //获得总数
            if (resultSet.next()) {
                count = resultSet.getInt(1);//获取总数值
            }
            totalPages = (int) Math.ceil(count / (limit * 1.0));//得到总页数
            if (page <= 0) { //保证页号合法性
                page = 1;
            } else if (page > totalPages) {
                return null;
            }
            System.out.println("count:"+count);
            System.out.println("TotalPagesGot:"+totalPages);
            resultSet = statement.executeQuery("select * from goods limit " + (page - 1) * limit + "," + limit);
            int rowCount =0 ;
        }catch (SQLException e){
            System.out.println("SQLO:商品分页查询失败");
            return null;
        }
        int rstNum=0;
        Goods[] goods = new Goods[limit];
        for(int i=0;i<limit;i++) {
            try {
                resultSet.next();
                rstNum++;
                int gid = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int amount = resultSet.getInt(3);
                byte[] image;
                image = resultSet.getBytes(4);
                String content = resultSet.getString(5);
                double price = resultSet.getDouble(6);
                int uid = resultSet.getInt(7);
                goods[i] = new Goods();
                goods[i].setGid(gid);
                goods[i].setName(name);
                goods[i].setAmount(amount);
                //goods[i].setImage(image);
                goods[i].setContent(content);
                goods[i].setPrice(price);
                goods[i].setUid(uid);
//                if(resultSet.isLast()){
//                    for (; i < limit; i++) {
//                        goods[i] = null;
//                    }
//                    break;
//                }
            } catch (SQLException e) {
                //e.printStackTrace();
                //System.out.println(i);
                for (; i < limit; i++) {
                    goods[i] = null;
                }
                break;
            }
        }
        System.out.println("returnGoods");
        try {
            resultSet.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return goods;
    }
    public static Goods getGoodsByGid(Connection connection, int gid){                  //根据gid获得商品
        Statement statement;
        ResultSet resultSet;
        Goods goods;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from goods where gid="+gid);
            resultSet.next();
            String name = resultSet.getString(2);
            int amount = resultSet.getInt(3);
            byte[] image;
            image = resultSet.getBytes(4);
            String content = resultSet.getString(5);
            double price = resultSet.getDouble(6);
            int uid = resultSet.getInt(7);
            goods = new Goods();
            goods.setGid(gid);
            goods.setName(name);
            goods.setAmount(amount);
            goods.setImage(image);
            goods.setContent(content);
            goods.setPrice(price);
            goods.setUid(uid);
            return goods;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLO:商品依据未找到:gid="+gid);
            return null;
        }

    }
    public static boolean deleteUser(Connection connection, int uid){             //根据uid删除用户
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute("delete from user where uid="+uid);
            System.out.println("SQLO:用户已删除:uid="+uid);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLO:用户删除失败:uid="+uid);
            return false;
        }

    }
    public static User loginAuth(Connection connection,User user){                //用户密码验证
        String sql = "select * from user where username='"+user.getUname()+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            int uid = resultSet.getInt("uid");
            String type = resultSet.getString("type");
            String nickname = resultSet.getString("nickname");
            String pwd = resultSet.getString("password");
            double wallet = resultSet.getDouble("wallet");
            if(type.compareTo("new") == 0){
                user.setStatus("loginF3");
                System.out.println("SQLO:用户登录失败：注册未批准");
            }else{
                if(user.getPwd().compareTo(pwd)==0){
                    user.setUid(uid);
                    user.setType(type);
                    user.setNickname(nickname);
                    user.setPwd(pwd);
                    user.setStatus("online");
                    user.setWallet(wallet);
                    System.out.println("SQLO:用户登录成功");
                }else{
                    user.setStatus("loginF2");
                    System.out.println("SQLO:用户登录失败：密码错误");
                }
            }
        } catch (SQLException e) {
            user.setStatus("loginF1");
            System.out.println("SQLO:用户登录失败：用户不存在");
            //e.printStackTrace();
        }
        return user;

    }
    public static User loginViladate(Connection connection,int token){        //用户登录状态token验证
        String sql = "select * from user where token="+token;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            int uid = resultSet.getInt("uid");
            String type = resultSet.getString("type");
            String uname = resultSet.getString("username");
            String nickname = resultSet.getString("nickname");
            String pwd = resultSet.getString("password");
            String status = resultSet.getString("status");
            double wallet = resultSet.getDouble("wallet");
            return new User(uid,type,uname,nickname,pwd,token,status,wallet);
        } catch (SQLException e) {
            System.out.println("SQLO:用户登录验证失败：用户token不存在");
            return null;
            //e.printStackTrace();
        }
    }
    public static boolean setUserToken(Connection connection,int uid,int token){   //设置用户token
        String sql = "update user SET token =? where uid=?";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setInt(1,token);
            presta.setInt(2,uid);
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:用户token已更新");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:用户token更新失败");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean setUserLogout(Connection connection,String uname){   //设置用户token
        String sql = "update user SET status =?, token =? where username=?";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setString(1,"offline");
            presta.setInt(2,0);
            presta.setString(3,uname);
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:用户已注销");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:用户注销失败");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean setUserWallet(Connection connection,int uid,double wallet){  //设置用户钱包
        String sql = "update user SET wallet =? where uid=?";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setDouble(1,wallet);
            presta.setInt(2,uid);
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:用户wallet已更新:"+wallet);
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:用户wallet更新失败");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean setGoodsAmount(Connection connection,int gid,int amount){  //更新商品数量
        String sql = "update goods SET amount =? where gid=?";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setInt(1,amount);
            presta.setInt(2,gid);
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:商品amount已更新:"+amount);
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:商品amount更新失败");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean deleteGoods(Connection connection,int gid){
        String sql = "delete from goods where gid="+gid;
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("SQLO:商品已删除:"+gid);
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:商品删除失败");
            e.printStackTrace();
            return false;
        }
    }


    public static boolean setUserStatus(Connection connection,int uid,String status){  //设置用户状态
        String sql = "update user SET status =? where uid=?";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setString(1,status);
            presta.setInt(2,uid);
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:用户状态已更新");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:用户状态更新失败");
            e.printStackTrace();
            return false;
        }
    }
    public static boolean setUserType(Connection connection,int uid,String type){   //设置用户类型
        String sql = "update user SET type =? where uid=?";
        try {
            //预处理sql语句
            PreparedStatement presta = connection.prepareStatement(sql);
            //设置语句value值
            presta.setString(1,type);
            presta.setInt(2,uid);
            //执行sql语句
            presta.execute();
            System.out.println("SQLO:用户类型已更新");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:用户类型更新失败");
            e.printStackTrace();
            return false;
        }
    }
    public static User[] getAllUser(Connection connection){          //获得所有用户
        String sql1 = "select count(*) as num from user"; //获得总数

        String sql2 = "select * from user";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql1);
            resultSet.next();
            int num = resultSet.getInt(1);
            User[] users = new User[num];
            resultSet = statement.executeQuery(sql2);
            for(int i=0;i<num;i++){
                resultSet.next();
                int uid = resultSet.getInt("uid");
                String type = resultSet.getString("type");
                String uname = resultSet.getString("username");
                String nickname = resultSet.getString("nickname");
                String pwd = resultSet.getString("password");
                String status = resultSet.getString("status");
                double wallet = resultSet.getDouble("wallet");
                users[i] = new User();
                users[i].setUid(uid);
                users[i].setType(type);
                users[i].setUname(uname);
                users[i].setNickname(nickname);
                users[i].setPwd(pwd);
                users[i].setStatus(status);
                users[i].setWallet(wallet);
            }
            return users;
        } catch (SQLException e) {
            System.out.println("SQLO:无用户");
            e.printStackTrace();
            return null;
        }
    }
    public static User[] getAllNewUser(Connection connection){   //获得所有新用户
        String sql1 = "select count(*) as num from user where type='new'";
        String sql2 = "select * from user where type='new'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql1);
            resultSet.next();
            int num = resultSet.getInt(1); //获得总数
            System.out.println("num:"+num);
            User[] users = new User[num];
            resultSet = statement.executeQuery(sql2);
            for(int i=0;i<num;i++){
                resultSet.next();
                int uid = resultSet.getInt("uid");
                String type = resultSet.getString("type");
                String uname = resultSet.getString("username");
                String nickname = resultSet.getString("nickname");
                String pwd = resultSet.getString("password");
                String status = resultSet.getString("status");
                double wallet = resultSet.getDouble("wallet");
                users[i] = new User();
                users[i].setUid(uid);
                users[i].setType(type);
                users[i].setUname(uname);
                users[i].setNickname(nickname);
                users[i].setPwd(pwd);
                users[i].setStatus(status);
                users[i].setWallet(wallet);
            }
            return users;
        } catch (SQLException e) {
            System.out.println("SQLO:无新用户");
            e.printStackTrace();
            return null;
        }
    }
}
