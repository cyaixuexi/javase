package com.cy.exer;

import com.cy.bean.Student;
import com.cy.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

public class Exer2Test {

    //问题一：向examstudent表中添加一条数据
    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("四级/六级:");
        int type = scanner.nextInt();
        System.out.print("身份证号:");
        String IDCard = scanner.next();
        System.out.print("准考证号:");
        String examCard = scanner.next();
        System.out.print("学生姓名:");
        String studentName = scanner.next();
        System.out.print("所在城市:");
        String location = scanner.next();
        System.out.print("学生成绩:");
        int grade = scanner.nextInt();

        String sql = "insert into examStudent(type,IDCard,examCard,studentName,location,grade)values(?,?,?,?,?,?)";
        int insertCount = update(sql, type, IDCard, examCard, studentName, location, grade);
        if (insertCount>0){
            System.out.println("添加成功!");
        }else {
            System.out.println("添加失败!");
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
    /******************************************************************************************************************/

    //问题2：根基身份证号或准考证号查询学生信息
    @Test
    public void queryWithIDCardOrExamCard(){
        System.out.println("请选择您要输入的类型");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        if ("a".equalsIgnoreCase(selection)){
            System.out.println("请输入准考证号:");
            String examCard = scanner.next();
            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent " +
                    "where ExamCard=?";
            Student student = getInstance(Student.class, sql, examCard);
            if (student!=null){
                System.out.println(student);
            }else {
                System.out.println("您输入的准考证号有误!");
            }
        }else if ("b".equalsIgnoreCase(selection)){
            System.out.println("请输入身份证号:");
            String IDCard = scanner.next();
            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent " +
                    "where IDCard=?";
            Student student = getInstance(Student.class, sql, IDCard);
            if (student!=null){
                System.out.println(student);
            }else {
                System.out.println("您输入的身份证号有误!");
            }
        }else {
            System.out.println("您的输入有误，请输入正确的类型!");
        }

    }

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

    /******************************************************************************************************************/

    //问题3：删除指定的学生信息
    @Test
    public void testDeleteByExamCard(){
        System.out.println("请输入学生的准考证号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        //查询指定准考证号的学生
        String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent " +
                "where examCard=?";
        Student student = getInstance(Student.class, sql);
        if (student==null){
            System.out.println("查无此人，请重新输入");
        }else {
            String sql1 = "delete from examstudent where examCard=?";
            int deleteCount = update(sql1, examCard);
            if (deleteCount>0){
                System.out.println("删除成功!");
            }else {
                System.out.println("删除失败!");
            }
        }
    }

    @Test
    public void testDeleteByExamCard1(){
        System.out.println("请输入学生的考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        String sql1 = "delete from examstudent where examCard=?";
        int deleteCount = update(sql1, examCard);
        if (deleteCount>0){
            System.out.println("删除成功!");
        }else {
            System.out.println("查无此人，请重新输入");
        }
    }
}
