package com.cy.preparedstatement;

import com.cy.bean.Customer;
import com.cy.bean.Order;
import com.cy.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用PrepareStatement实现针对不同表的通用的查询操作
 */
public class PreparedStatementQueryTest {

    @Test
    public void testGetForList(){
        String sql = "select id,name,email from customers where id<? ";
        List<Customer> list = getForList(Customer.class, sql, 12);
        list.forEach(System.out::println);

        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id<?";
        List<Order> orderList = getForList(Order.class, sql1, 5);
        orderList.forEach(System.out::println);
    }

    public <T> List<T> getForList(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;

    }


    /*******************************************************************************************************************/

    @Test
    public void testGetInstance(){
        String sql = "select id,name,email from customers where id=?";
        Customer customer = getInstance(Customer.class, sql, 12);
        System.out.println(customer);

        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id=?";
        Order order = getInstance(Order.class, sql1, 1);
        System.out.println(order);
    }


    /**
     * 针对于不同的表的通用的查询操作，返回表的一条记录
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz,String sql,Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
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
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }
}
