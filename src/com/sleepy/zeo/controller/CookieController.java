package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cookie")
public class CookieController {

    @RequestMapping("")
    public String index() {
        return "cookie";
    }

    // 方式一，获取request的cookie中属性值
    @RequestMapping("/display")
    public String display(@CookieValue("s-mvc-session") String sessionId) {
        System.out.println("sessionId: " + sessionId);
        return "cookie";
    }

    // 方式二，获取request的cookie中属性值
    @RequestMapping("/display2")
    public String display2(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String sessionId = "";
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("s-mvc-session".equals(c.getName())) {
                    sessionId = c.getValue();
                }
            }
        }
        System.out.println("sessionId: " + sessionId);
        return "cookie";
    }
}
