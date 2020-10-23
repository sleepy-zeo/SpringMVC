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

    @RequestMapping("/display")
    public String display(@CookieValue("JSESSIONID") String sessionId) {
        System.out.println("sessionId: " + sessionId);
        return "cookie";
    }

    @RequestMapping("/display2")
    public String display2(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String sessionId = "";
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("JSESSIONID".equals(c.getName())) {
                    sessionId = c.getValue();
                }
            }
        }

        System.out.println("sessionId: " + sessionId);
        System.out.println(request.getSession().getAttribute("secret_serial"));

        return "cookie";
    }
}
