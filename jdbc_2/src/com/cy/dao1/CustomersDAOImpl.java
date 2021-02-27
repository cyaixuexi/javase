package com.cy.dao1;

import com.cy.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomersDAOImpl extends BaseDAO<Customer> implements CustomerDAO {
    @Override
    public void insert(Connection conn, Customer customer) {
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        update(conn,sql,customer.getName(),customer.getEmail(),customer.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id=?";
        update(conn,sql,id);
    }

    @Override
    public void update(Connection conn, Customer customer) {
        String sql = "update customers set name=?,email=?,birth=? where id=?";
        update(conn,sql,customer.getName(),customer.getEmail(),customer.getBirth(),customer.getId());
    }

    @Override
    public Customer queryById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customers where id=?";
        Customer customer = getInstance(conn, sql, id);
        return customer;
    }

    @Override
    public List<Customer> queryAll(Connection conn) {
        String sql = "select id,name,email,birth from customers";
        List<Customer> customerList = getForList(conn,  sql);
        return customerList;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        return getValue(conn,sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return getValue(conn,sql);
    }
}
