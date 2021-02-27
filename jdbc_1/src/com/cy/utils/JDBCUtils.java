package com.cy.utils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/*
* 操作数据库的工具类
* */
public class JDBCUtils {
    /*
    * 获取数据库的连接
    * */
    public static Connection getConnection() throws Exception{
        //1。读取配置文件中的四个基本属性
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driver = pros.getProperty("driver");

        //2.加载驱动
        Class.forName(driver);

        //3.获取连接
        Connection conn = DriverManager.getConnection(url,user,password);

        return conn;
    }

    /*
    * 关闭资源的操作
    * */
    public static void closeResource(Connection conn, PreparedStatement ps){
        try {
            if (ps!=null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    * 关闭资源操作
    * */
    public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            if (ps!=null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (rs!=null)
                rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
