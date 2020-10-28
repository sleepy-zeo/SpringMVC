package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/ex")
public class ExceptionController {

    @RequestMapping()
    public void exception(HttpServletResponse response) throws IOException {
        int i = 100 / 0;
        response.getWriter().write("success");
    }
}
