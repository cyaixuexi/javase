package com.cy.dao1;

import com.cy.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
* DAO:data(base) access object
* 封装了针对数据表的通用操作
* */
public abstract class BaseDAO {
    //* 通用的增删改操作  vesion---2.0(考虑上事务)
    public int update(Connection conn, String sql, Object... args) {
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


    //通用的查询操作：用于返回数据表中的多条记录构成的集合（version 2.0 考虑上事务）
    public <T> List<T> getForList(Connection conn,Class<T> clazz, String sql, Object ...args){
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
            //创建集合对象
            List<T> list = new ArrayList<T>();
            while (rs.next()){
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列：给T对象指定属性赋值
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
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;

    }

    //用于查询特殊值的通用方法
    public <E> E getValue(Connection conn,String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[1]);
            }
            rs = ps.executeQuery();

            if (rs.next()){
                return (E) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }
}
