package com.sleepy.zeo.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/ip")
public class InterceptorController {

    private static final Log logger = LogFactory.getLog(InterceptorController.class);

    @RequestMapping("")
    public void intercept1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("intercept");
        response.getWriter().write("intercept controller");
    }

    @RequestMapping("/in2")
    public void intercept2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("intercept");
        response.getWriter().write("intercept controller");
    }

    @RequestMapping("/in3")
    public void intercept3(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("intercept");
        response.getWriter().write("intercept controller");
    }
}
