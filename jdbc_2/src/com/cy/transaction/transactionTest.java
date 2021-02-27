package com.cy.transaction;

import com.cy.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/*
 *1.什么叫数据库事务？
 *事务：一组逻辑操作单元，是数据从一种状态变换到另一种状态
 *       > 一组逻辑操作单元：一个或多个DML（增删改）操作。
 *
 * 2.事务处理的原则：：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方
 *      式。当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，那么这些修改就永久地保存下来；
 *      要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 *
 * 3.书库一旦提交，就不可回滚
 *
 * 4.哪些操作会导致数据的自动提交？
 *       >DDL操作一旦执行，都会自动提交
 *           >set autocommit = false的方式对DDL操作失效
 *       >DML默认情况下，一旦执行，就会自动提交。
 *           >我们可以通过 set autocommit = false的方式取消DML操作的自动提交
 *       >默认在关闭连接时，会自动的提交数据
 *
 *
 *
 * */

public class transactionTest {

    /*
     * 通用的增删改操作  vesion---1.0
     * */
    public int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数控库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PrepareStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//小心参数声明
            }
            //4.执行sql
           return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }



    /*
    * 针对于数据表user_table来说
    * AA用户给BB用户转账100元
    *
    * update user_table set balance = balance-100 where user ='AA';
    * update user_table set balance = balance+100 where user ='BB';
    * */
    @Test
    public void testUpdate(){
        String sql1 = "update user_table set balance = balance-100 where user=?";
        update(sql1,"AA");

        //模拟网络异常
        System.out.println(10/0);

        String sql2 = "update user_table set balance = balance+100 where user=?";
        update(sql2,"BB");

        System.out.println("转账成功！");

    }

    /*******************************************考虑数据库事务后的转账操作**************************************************/
     //* 通用的增删改操作  vesion---2.0(考虑上事务)
    public int update(Connection conn,String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句，返回PrepareStatement的实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//小心参数声明
            }
            //3.执行sql
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4.资源的关闭
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }
    @Test
    public void testUpdateWithTx() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //取消数据的自动提交功能
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance-100 where user=?";
            update(conn,sql1,"AA");

            //模拟网络异常
//          System.out.println(10/0);

            String sql2 = "update user_table set balance = balance+100 where user=?";
            update(conn,sql2,"BB");
            System.out.println("转账成功！");
            //提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally {
            //修改其为自动提交数据        主要针对于使用数据库连接池的使用
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(conn,null);
        }
    }
    //***************************************************************************************************************
    //通用的查询操作，返回表的一条记录 (version 2.0 考虑事务)
    public <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //执行，获取结果集
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()){
                T t = clazz.newInstance();
                for (int i=0;i<columnCount;i++){
                    //获取每一个列的列值:通过ResultSet
                    Object columnValue = rs.getObject(i + 1);
                    //获取每一个列的列名:通过ResultSetMetaData  不推荐使用
//                    String columnName = rsmd.getColumnName(i + 1);
                    //获取列的别名：getColumnLabel()
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射，将对象的指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }

    @Test
    public void testTransactionSelect() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        //获取当前连接的隔离级别
        System.out.println(conn.getTransactionIsolation());
        //设置数据库的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //取消自动提交数据
        conn.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user=?";
        User user = getInstance(conn, User.class, sql, "CC");

        System.out.println(user);
    }
    @Test
    public void testTransactionUpdate() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        //取消自动提交数据
        conn.setAutoCommit(false);

        String sql = "update user_table set balance=? where user=?";
        update(conn,sql,2000,"CC");

        Thread.sleep(15000);
        System.out.println("修改结束！");
    }
}
