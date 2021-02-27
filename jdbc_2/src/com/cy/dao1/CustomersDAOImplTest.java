package com.cy.dao1;


import com.cy.bean.Customer;
import com.cy.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomersDAOImplTest {
    CustomerDAO customerDAO = new CustomersDAOImpl();

    @Test
    public void insert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            customerDAO.insert(conn,new Customer(1,"野原新之助","xin@qq.com",new Date(123123412L)));
            System.out.println("添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void deleteById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            customerDAO.deleteById(conn,13);
            System.out.println("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void update() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            customerDAO.update(conn,new Customer(18,"贝多芬","beiduofen@qq.com",new Date(1234124L)));
            System.out.println("修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void queryById() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = customerDAO.queryById(conn, 21);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void queryAll() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            List<Customer> customers = customerDAO.queryAll(conn);
            customers.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getCount() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Long count = customerDAO.getCount(conn);
            System.out.println("表中的记录数为" + count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    @Test
    public void getMaxBirth() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date maxBirth = customerDAO.getMaxBirth(conn);
            System.out.println("最大生日为："+maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }
}