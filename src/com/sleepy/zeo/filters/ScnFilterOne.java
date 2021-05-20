package com.sleepy.zeo.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ScnFilterOne extends OncePerRequestFilter {

    private Log log = LogFactory.getLog(ScnFilterOne.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal");
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
