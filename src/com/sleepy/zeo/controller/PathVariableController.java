package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/rest")
public class PathVariableController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String insert() {
        System.out.println("insert...");
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET,produces = {"application/json"})
    @ResponseBody
    public User select(HttpServletResponse response) {
        System.out.println("select...");
        return new User();
    }

    /**
     * PathVariable
     * 将url中的占位符绑定到控制器处理方法的参数中
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String update(@PathVariable("id") int id) {
        System.out.println("update...id=" + id);
        return "success";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@PathVariable("id") int id) {
        System.out.println("delete...id=" + id);
        return "success";
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
