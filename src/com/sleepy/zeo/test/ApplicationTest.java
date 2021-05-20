package com.sleepy.zeo.test;

public class ApplicationTest {

    public static void main(String[] args){
        String content = "-12";

        String[] values = content.split("-");
        System.out.println(values.length);
        System.out.println(values[0]);
        System.out.println(values[1]);

        System.out.println("abcd/abc".substring(5));


    }
}
