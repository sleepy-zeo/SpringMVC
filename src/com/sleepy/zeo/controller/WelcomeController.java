package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 后端处理器
@Controller
public class WelcomeController {

    @RequestMapping("/hello")
    public String hello() {
        System.out.println("hello...");
        return "welcome";
    }
}
