package com.cy.dbutils;

import com.cy.bean.Customer;
import com.cy.util.JDBCUtils;
import com.cy.util.JDBCUtilsTest;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
* commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
* */
public class QueryRunnerTest {
    //测试插入
    @Test
    public void testInsert(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();

            conn = JDBCUtilsTest.getConnection3();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";

            runner.update(conn,sql,"董小姐","dong@qq.com","2000-10-01");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtilsTest.closeResource(conn, null);
        }

    }
    //测试查询
    /*
    * BeanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录
    * */
    @Test
    public void testQuery1() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtilsTest.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            BeanHandler<Customer> handler = new BeanHandler<Customer>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 21);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtilsTest.closeResource(conn,null);
        }
    }

    /*
    *BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录的集合
    * */
    @Test
    public void testQuery2() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtilsTest.getConnection3();
            String sql = "select id,name,email,birth from customers where id<?";
            BeanListHandler<Customer> handler = new BeanListHandler<Customer>(Customer.class);
            List<Customer> customerList = runner.query(conn, sql, handler, 21);
            customerList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtilsTest.closeResource(conn,null);
        }
    }

    /*
     * MapHandler:是ResultSetHandler接口的实现类，对应表中的一条记录
     *      将字段及相对应的值作为map中的键和值
     * */
    @Test
    public void testQuery3() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtilsTest.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 21);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtilsTest.closeResource(conn,null);
        }
    }

    /*
     * MapHandler:是ResultSetHandler接口的实现类，对应表中的多条记录
     *      将字段及相对应的值作为map中的键和值
     * */
    @Test
    public void testQuery4() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtilsTest.getConnection3();
            String sql = "select id,name,email,birth from customers where id<?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 21);
            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtilsTest.closeResource(conn,null);
        }
    }

    /*ScalarHandler 用于查询特殊值*/
    @Test
    public void testQuery5() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtilsTest.getConnection3();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            Object query = runner.query(conn, sql, handler);
            System.out.println(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtilsTest.closeResource(conn,null);
        }
    }
}
