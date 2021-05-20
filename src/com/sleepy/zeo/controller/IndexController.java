package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping("")
    @ResponseBody
    public String index() {
        return "index";
    }

    @RequestMapping("/favicon.ico")
    public String favicon() {
        return "forward:/static/favicon.ico";
    }
}
