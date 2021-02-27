package com.cy.exer;

import com.cy.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Exer1Test {
    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入用户名：");
        String name = scanner.next();
        System.out.println("输入用邮箱：");
        String email = scanner.next();
        System.out.println("输入生日：");
        String birthday = scanner.next();

        String sql = "insert into customers(name,email,birth)value(?,?,?)";
        int insertCount = update(sql, name, email, birthday);
        if (insertCount>0){
            System.out.println("添加成功！");
        }else {
            System.out.println("添加失败！");
        }
    }

    public int update(String sql,Object ...args)  {
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
            /*
            *  ps.execute():
            * 如果执行的是查询操作，有返回结果，则此方法返回true
            * 如果执行的是增删改操作，没有返回结果，则此方法返回false
            * */
            //方式一：
//          return ps.execute();
            //方式二：
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }
}
