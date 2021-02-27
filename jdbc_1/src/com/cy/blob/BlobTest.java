package com.cy.blob;

import com.cy.bean.Customer;
import com.cy.utils.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/*
* 测试使用PreparedStatement操作Blob类型的数据
* */
public class BlobTest {

    //向数据表customers中踏插入blob类型的字段
    @Test
    public void testInsert() throws Exception{
        Connection conn = JDBCUtils.getConnection();

        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setObject(1,"张子枫");
        ps.setObject(2,"zhang@qq.com");
        ps.setObject(3,"1999-10-10");
        FileInputStream is = new FileInputStream(new File("zhangzifeng.jpg"));
        ps.setBlob(4,is);

        ps.execute();

        JDBCUtils.closeResource(conn,ps);
    }

    //查询数据表customers中的Blo字段
    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is= null;
        FileOutputStream fos= null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,21);

            rs = ps.executeQuery();
            if (rs.next()){
                /*int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date birth = rs.getDate(4);*/

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                //将Blob类型的字段下载下来，以文件的方式保存在本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("xiaoxin.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while((len=is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is!=null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (fos!=null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JDBCUtils.closeResource(conn,ps,rs);
        }

    }
}
