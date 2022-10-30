package com.BDacy;

import org.junit.Test;

/**
 * @BelongsPackage: com.BDacy
 * @Author: yca
 * @CreateTime: 2022-10-30  23:22
 * @Description:
 */
public class SimpleTest {
    class Person{
        public void a(){
            System.out.println("person a");
        }
        public void b(){
            System.out.println("person b");
            a();
        }
    }
    class yca extends Person{
        @Override
        public void a() {
            System.out.println("yca a");
        }
    }
    @Test
    public void test1(){
        Person yca = new yca();
        yca.b();
    }
}