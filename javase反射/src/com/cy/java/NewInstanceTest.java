package com.cy.java;

import org.junit.Test;

import java.util.Random;

/*
* 通过反射创建对应的运行时类的对象
* */
public class NewInstanceTest {
    @Test
    public void test() throws Exception{
        Class<Person> clazz = Person.class;
        /*
        * newInstance():调用此方法，创建对应的运行时类的对象。内部调用了运行时类的空参构造器
        *
        * */
        Person o = clazz.newInstance();
        System.out.println(o);
    }

    @Test
    public void test2(){
        int i = new Random().nextInt(3);//0 1 2
        String classpath="";
        switch (i){
            case 0:
                classpath = "java.util.Data";
                break;
            case 1:
                classpath = "java.lang.Object";
                break;
            case 2:
                classpath = "com.cy.java.Person";
                break;
        }
        try {
            Object instance = getInstance(classpath);
            System.out.println(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 创建一个指定类的对象
    * classpath：指定类的全类名
    * */
    public Object getInstance(String classpath) throws Exception {
        Class<?> clazz = Class.forName(classpath);
        return clazz.newInstance();
    }
}
