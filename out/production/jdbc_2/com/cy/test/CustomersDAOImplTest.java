package com.cy.test;

import com.cy.bean.Customer;
import com.cy.dao.CustomerDAO;
import com.cy.dao.CustomersDAOImpl;
import com.cy.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;


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
    }

    @Test
    public void update() {
    }

    @Test
    public void queryById() {
    }

    @Test
    public void queryAll() {
    }

    @Test
    public void getCount() {
    }

    @Test
    public void getMaxBirth() {
    }
}