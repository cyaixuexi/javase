package com.cy.java1;
@MyAnnotation(value = "hi")
public class Person extends Creature<String> implements Comparable<String>,MyInterface{

    private String name;
    int age;
    public  int id;

    public Person(){}

    private Person(String name){
        this.name=name;
    }
    Person(String name,int age){
        this.name=name;
        this.age=age;
    }

    private String show(String nation){
        System.out.println("我的国籍是:"+nation);
        return nation;
    }

    public String display(String interests){
        return interests;
    }

    @Override
    public void info() {
        System.out.println("我是一个人");
    }

    @Override
    public int compareTo(String s) {

        return 0;
    }
}
