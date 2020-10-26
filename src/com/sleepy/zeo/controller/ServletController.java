package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/sv")
public class ServletController {

    //
    //
    @RequestMapping("rm")
    public void rm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String attr = (String) request.getAttribute("key");
        response.getWriter().write("Test rm, attribute: " + attr);
    }

    /**
     * redirect
     *
     * redirect会改变地址栏的地址，并且不传递request中的数据，因为期间有多次request
     * redirect可以重定向到项目内和项目以外的页面
     *
     * response.sendRedirect("/rm");
     *      跳转到http://localhost/rm
     * response.sendRedirect("rm");
     *      跳转到http://localhost/sv/rm
     *
     * 另一种实现方式和forward形式一致: return "redirect:rm";
     */
    @RequestMapping("/redirect")
    public void redirect(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        request.setAttribute("key", "redirect");
        response.sendRedirect("rm");
    }

    /**
     * forward
     *
     * forward不会改变地址栏的地址，并且可以传递request中的数据，因为从始至终只有一次request
     * forward只能转发到项目内的页面
     *
     * forward:/rm
     *      跳转到http://localhost/rm
     * forward:rm
     *      跳转到http://localhost/sv/rm
     */
    @RequestMapping("/forward")
    public String forward(HttpServletRequest request) {
        request.setAttribute("key", "forward");
        return "forward:rm";
    }

}
