package com.mean.csts.server;

import com.mean.csts.data.Goods;
import com.mean.csts.data.User;

import java.sql.*;

public class SQLOperator {
    public static boolean insertUser(Connection connection, User user){
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
    public static boolean insertGoods(Connection connection, Goods goods,byte[] image,int uid){
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
    public static Goods[] getGoods(Connection connection, int page, int limit){
        int count=0;
        int totalPages=0;
        Statement statement;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select count(*) from goods"); //获得总数
            if (resultSet.next()) {
                count = resultSet.getInt(1);//获取总数值
            }
            totalPages = (int) Math.ceil(count / (limit * 1.0));//得到总页数
            if (page <= 0) { //保证页号合法性
                page = 1;
            } else if (page > totalPages) {
                page = totalPages;
            }
            //System.out.println("TotalPagesGot:"+totalPages);
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
                goods[i].setImage(image);
                goods[i].setContent(content);
                goods[i].setPrice(price);
                goods[i].setUid(uid);
            } catch (SQLException e) {
                e.printStackTrace();
                for (; i < limit; i++) {
                    goods[i] = null;
                }
                break;
            }
        }
        System.out.println("returnGoods");
        return goods;
    }
    public static Goods getGoodsByGid(Connection connection, int gid){
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
    public static User loginAuth(Connection connection,User user){
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

        } catch (SQLException e) {
            user.setStatus("loginF1");
            System.out.println("SQLO:用户登录失败：用户不存在");
            //e.printStackTrace();
        }
        return user;

    }
    public static User loginViladate(Connection connection,int token){
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
    public static boolean setUserToken(Connection connection,int uid,int token){
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
    public static boolean setUserWallet(Connection connection,int uid,double wallet){
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
    public static boolean setGoodsAmount(Connection connection,int gid,int amount){
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
            statement.executeQuery(sql);
            System.out.println("SQLO:商品已删除:"+gid);
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:商品删除失败");
            e.printStackTrace();
            return false;
        }
    }


        public static boolean setUserStatus(Connection connection,int uid,String status){
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
    public static boolean selectUser(Connection connection,User user){
        String sql = "select uid, type, username, nickname, password from user";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            System.out.println(rs.getString("uid") +" | " + rs.getString("username") +
                    rs.getString("nickname") + rs.getString("password"));
            System.out.println("SQLO:用户查询成功");
            return true;
        } catch (SQLException e) {
            System.out.println("SQLO:用户查询失败");
            e.printStackTrace();
            return false;
        }

    }


}
