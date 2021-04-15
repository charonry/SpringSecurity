package com.charon.basicserver.model;

/**
 * @program: SpringSecurity
 * @description
 * @author: charon
 * @create: 2021-04-15 21:52
 **/
public class PersonDemo {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonDemo(String name) {
        this.name = name;
    }
}
