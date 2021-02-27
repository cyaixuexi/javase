package com.cy.dao;

import com.cy.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/*
* 此接口用于规范针对customers表的通用操作
*
* */
public interface CustomerDAO {
    //将customer对象添加到数据库中
    public void insert(Connection conn, Customer customer);
    //根据指定的id删除指定的一条记录
    public void deleteById(Connection conn,int id);
    //针对于内存中的customer对象，去修改数据表中的一条记录
    public void update(Connection conn,Customer customer);
    //根据指定的id查询对应的customer对象
    public Customer queryById(Connection conn,int id);
    //查询表中的所有记录构成的集合
    public List<Customer> queryAll(Connection conn);
    //返回数据表中数据的条目数
    public Long getCount(Connection conn);
    //返回数据表中最大的生日
    Date getMaxBirth(Connection conn);

}
