package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jc")
public class RequestBodyAndResponseBodyController {

    /**
     * RequestBody
     *      自动将请求的json字符串转换成java对象
     * ResponseBody
     *      如果返回的是字符串，直接返回
     *      如果返回的是对象，会将对象转换成json字符串返回
     */
    @RequestMapping(value = "/user", produces = {"application/json"})
    @ResponseBody
    public User user(@RequestBody User user) {
        System.out.println("Request data: " + user);
        user.setName("sleepy zeo");
        user.setAge(25);
        return user;
    }

    public class User {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User[" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ']';
        }
    }
}
