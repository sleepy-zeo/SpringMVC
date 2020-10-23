package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@Controller
@RequestMapping("/sv")
public class ServletController {

    /**
     * session and cookies
     *
     * 存入数据库t_session的流程
     *      sessionId   -   value    -   expire   -   update_time
     *          id        json_data       data         timestamp
     *
     *      id: session.getId()
     *      json_value: json字符串，包括session的所有信息
     *      expire: session过期时间，每次刷新就会更新该值
     *
     * open_session
     *      返回 id + obj(由json_data组成)
     *
     * save_session
     *      insert或update t_session
     *      response setCookie  session_cookie_name(JSESSIONID)
     *                          session_cookie_id(3E790097AB2DD2024C0C9625420A0FCF)
     *                          max_age 过期之后，浏览器会自动清除该cookie
     *                          domain
     *                          path
     *                          secure
     *                          httponly
     *
     */
    @RequestMapping("/params")
    public void params(HttpServletRequest request,
                       HttpServletResponse response,
                       HttpSession session) throws IOException {

        session.setAttribute("secret_serial", "20201023155852");
        Enumeration<String> e = session.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            System.out.println("key: " + key + ", value: " + session.getAttribute(key));
        }

        System.out.println("id: " + session.getId());

        System.out.println(request.getCookies()[0].getName() + "," + request.getCookies()[0].getValue() + ", " + request.getCookies().length);

        response.getWriter().write("Servlet params");
    }

    // response.sendRedirect("/rm");
    //      跳转到http://localhost/rm
    // response.sendRedirect("rm");
    //      跳转到http://localhost/sv/rm
    @RequestMapping("/redirect")
    public void redirect(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        response.sendRedirect("/rm");
    }

}
