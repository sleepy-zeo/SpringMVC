package com.sleepy.zeo.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ScnFilterThree extends OncePerRequestFilter {

    private Log log = LogFactory.getLog(ScnFilterThree.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal");
        // 注掉改行，该请求就直接中断在此处，不会继续向下传递到dispatcherServlet中了
        // filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
