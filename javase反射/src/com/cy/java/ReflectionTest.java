package com.cy.java;

import org.junit.Test;

public class ReflectionTest {
    @Test
    public void test3() throws Exception{
        //方式一：调用运行时类的属性  .class
        Class clazz = Person.class;
        System.out.println(clazz);
        //方式二：调用运行时类的对象
        Person p = new Person();
        Class clazz1 = p.getClass();
        System.out.println(clazz1);
        //方式三：调用Class的静态方法  forName(String classpath)
        Class clazz2 = Class.forName("com.cy.java.Person");
        System.out.println(clazz2);
        //方式四：使用类的加载器： ClassLoader
        ClassLoader classLoader = ReflectionTest.class.getClassLoader();
        Class<?> clazz3 = classLoader.loadClass("com.cy.java.Person");
        System.out.println(clazz3);

    }
}
