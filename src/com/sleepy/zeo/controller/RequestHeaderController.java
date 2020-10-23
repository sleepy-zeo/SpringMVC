package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/rh")
public class RequestHeaderController {

    @RequestMapping("")
    public String index() {
        return "request_header";
    }

    @RequestMapping("/params")
    public String params(@RequestHeader("host") String host,
                         @RequestHeader("accept") String accept) {
        System.out.println("host: " + host);
        System.out.println("accept: " + accept);
        return "request_header";
    }

    @RequestMapping("/params2")
    public void params2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String host = request.getHeader("host");
        String accept = request.getHeader("accept");
        System.out.println("host: " + host);
        System.out.println("accept: " + accept);
        response.getWriter().write("hello");
    }

    @RequestMapping("/params3")
    public String params3(@RequestHeader("host") String host,
                          @RequestHeader("accept") String accept,
                          HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        String host2 = request.getHeader("host");
        String accept2 = request.getHeader("accept");

        System.out.println("host1: " + host);
        System.out.println("accept1: " + accept);
        System.out.println("host2: " + host2);
        System.out.println("accept2: " + accept2);

        return "request_header";
    }
}
