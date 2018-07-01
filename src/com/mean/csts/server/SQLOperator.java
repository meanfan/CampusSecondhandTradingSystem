package com.mean.csts.server;

import com.mean.csts.User;
import com.mean.csts.UserRegistered;

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
