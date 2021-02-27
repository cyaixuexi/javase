package com.cy.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtilsTest {
    /*
    * 使用C3P0的数据库连接池技术
    * */
    private static ComboPooledDataSource cpds = new ComboPooledDataSource();
    public static Connection getConnection1() throws Exception{
        Connection conn = cpds.getConnection();

        return conn;
    }

    /*
    *使用DBCP数据库连接池技术获取连接
    * */
    //创建一个DBCP数据库连接池
    private static DataSource dataSource;
    static {
        try {
            Properties pros = new Properties();
            //方式一：
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            //方式二：
            //FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);
            dataSource = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws Exception{
        Properties pros = new Properties();

        //方式一：
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二：
        //FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));

        pros.load(is);
        //创建一个DBCP数据库连接池
        DataSource dataSource = BasicDataSourceFactory.createDataSource(pros);

        Connection connection = dataSource.getConnection();
        return connection;
    }

    /*\\
    * 使用Druid数据库连接池技术
    * */
    private static DataSource dataSource1;
    static {
        try {
            Properties pros = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            dataSource1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception{
        Connection conn = dataSource.getConnection();
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

    /* 使用dbutils.jar中提供的DbUtils工具类，实现资源的关闭*/
    public static void closeResource1(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            DbUtils.close(conn);
            DbUtils.close(ps);
            DbUtils.close(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        /*DbUtils.closeQuietly(conn);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);*/
    }
}
