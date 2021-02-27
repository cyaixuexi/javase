package com.cy.preparedstatement;

import com.cy.utils.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

/*
* 使用Preparestatement来替换Statement，实现对数据表的增删改查操作
*
*
* */
public class PreparestatementUpdateTest {

    @Test
    public void testCommonUpdate(){
//        String sql="delete from customers where id = ?";
//        update(sql,3);

        String sql="update `order` set order_name=? where order_id=?";
        update(sql,"DD",2);
    }

    /*
    * 通用的增删改操作
    * */
    public void update(String sql,Object ...args)  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数控库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);//小心参数声明
            }
            //4.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }



    }

    /*
    * 修改customers表中的一条记录
    * */
    @Test
    public void testUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数控库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1,"莫扎特");
            ps.setObject(2,"18");
            //4.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }

    }

    //向customers表中添加一条记录
    @Test
    public void testInsert()  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
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
            conn = DriverManager.getConnection(url,user,password);
//            System.out.println(conn);

            //4.预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name,email,birth)value(?,?,?)";//?:占位符
            ps = conn.prepareStatement(sql);
            //5.填充占位符
            ps.setString(1,"哪吒");
            ps.setString(2,"nezha@qq.com");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse("1000-01-01");
            ps.setDate(3,new Date(date.getTime()));

            //6.执行sql操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.资源关闭
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

    }
}
