package com.cy.blob;

import com.cy.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/*
*使用PreparedStatem实现批量数据的操作
*
* update、delete本身就具有裴亮操作的效果
* 此时的批量操作，主要指的是批量插入。使用preparedStatement如何实现更高效的批量插入？
*
*
* */
public class InsertTest {

    //批量插入的方式：使用preparedStatement
    @Test
    public void testInsert(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert  into goods(name)values(?)";
            ps = conn.prepareStatement(sql);

            for (int i=0;i<=20000;i++){
                ps.setObject(1,"name_"+i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));//花费的时间为：43150
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
    }

    /*
    * 批量插入的方式三：
    * 1.addBatch()、executeBatch()、clearBatch()
    * 2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
    *    ?rewriteBatchedStatements=true 写在配置文件的url后面
    * 3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
    * */
    @Test
    public void testInsert2(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert  into goods(name)values(?)";
            ps = conn.prepareStatement(sql);

            for (int i=0;i<=20000;i++){
                ps.setObject(1,"name_"+i);
                //1.“攒”sql
                ps.addBatch();
                if (i%500==0){
                    //2.执行Batch
                    ps.executeBatch();

                    //3.清空Batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));//花费的时间为：724
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
    }

    /*
    * 批量插入方式4：设置连接不允许自动提交数据
    * */
    @Test
    public void testInsert3(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();

            //设置不允许自动提交数据
            conn.setAutoCommit(false);
            String sql = "insert  into goods(name)values(?)";
            ps = conn.prepareStatement(sql);

            for (int i=0;i<=20000;i++){
                ps.setObject(1,"name_"+i);
                //1.“攒”sql
                ps.addBatch();
                if (i%500==0){
                    //2.执行Batch
                    ps.executeBatch();

                    //3.清空Batch
                    ps.clearBatch();
                }
            }
            //同意提交数据
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));//花费的时间为：604
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,ps);
        }
    }
}
