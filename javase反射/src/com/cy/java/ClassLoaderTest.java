package com.cy.java;

import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

public class ClassLoaderTest {

    /*
    *Properties:用来读取配置文件
    * */
    @Test
    public void test() throws Exception{
        Properties properties = new Properties();
        //此时的文件默认在当前的module下
        //读取配置文件的方式一：
        /*FileInputStream is = new FileInputStream("jdbc.properties");
        properties.load(is);*/

        //读取配置文件的方式二：使用ClassLoader
        //配置文件默认识别为：当前module的src下
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        InputStream is = classLoader.getResourceAsStream("jdbc1.properties");
        properties.load(is);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        System.out.println("user="+user+",password="+password);
    }
}
