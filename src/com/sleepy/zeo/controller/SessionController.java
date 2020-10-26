package com.sleepy.zeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@Controller
@RequestMapping("/sc")
public class SessionController {

    /**
     * session and cookies
     *
     * 存入数据库t_session的流程
     *      sessionId   -   value    -   expire   -   update_time
     *          id        json_data       data         timestamp
     *
     *      id: session.getId()
     *      json_value: json <===> session
     *      expire: session过期时间，每次刷新就会更新该值
     *
     * open_session
     *      # 具体逻辑参考session::open_session
     *      1. 从request的cookie中获取session_id(如果request中不包含session_id则重新创建一个session并返回)
     *      2. 根据session_id从数据库中获取session
     *      3. 返回session
     *
     * save_session
     *      # 具体逻辑参考session::save_session
     *      1. 获取要保存更新的session内的信息(主要是expire)
     *      2. 依据1中的信息insert或update数据库中的session的信息(主要是expire)
     *      3. 依据1中的信息设置response的cookie
     *
     *              response setCookie  cookie_name
     *                                  cookie_id
     *                                  max_age 设置完成交给浏览器处理，过期之后，浏览器会自动清除该cookie
     *                                  ...
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

        response.getWriter().write("Session params");
    }
}
