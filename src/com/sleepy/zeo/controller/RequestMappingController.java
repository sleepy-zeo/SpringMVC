package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 后端处理器
@Controller
@RequestMapping("/rm")
public class RequestMappingController {

    // ""既可以拦截/rm，也可以拦截/rm/
    // "/"只能拦截/rm/
    // 所以一般都选用""
    @RequestMapping("")
    public String index() {
        return "request_mapping";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "request_mapping_hello";
    }

    @RequestMapping(value = "/json", produces = "application/json")
    public void json(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write("{\"name\":\"zeo\", \"age\":26}");
    }

    // method限制只能某种或某些方法可以访问该url
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String post() {
        return "request_mapping_post";
    }

    // headers限制request里必须包含的请求头
    // 比如这里必须包含请求头Referer: http://localhost/
    @RequestMapping(value = "/restrict", headers = "Referer=http://localhost/")
    public void restrictHeaders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("restrict headers information");
    }

    // params限制request里必须包含的参数
    // 比如这里请求必须是/restrict2?name=steven&&age=26
    @RequestMapping(value = "/restrict2", params = {"name", "age"})
    public void restrictParams(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        System.out.println("name: " + name + ", age: " + age);
        response.getWriter().write("restrict params information");
    }

    // consumes限制request的Content-Type类型
    // produces限制request的Accept类型，
    //      主要用于和request的Accept进行比对，如果request的Accept中不包含该类型，则请求失败，否则请求成功
    //      至于返回的内容是否真的是produces指定类型则没有任何关系
    @RequestMapping(value = "/cp", consumes = "text/html", produces = "application/json")
    public void cp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("{\"name\":\"sleepy\", \"age\":26}");
    }
}
