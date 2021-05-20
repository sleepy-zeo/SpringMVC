package com.sleepy.zeo.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * interceptor是配置在dispatcherServlet中的，请求到达dispatcherServlet后发送至interceptor，然后
 *
 * 当preHandle返回true的时候，执行顺序为
 * preHandle1 -> preHandle2 -> preHandle3 -> Controller -> postHandle3 -> postHandle2 -> postHandle1 -> afterCompletion3 -> afterCompletion2 -> afterCompletion1
 *
 * 当preHandle3返回false的时候，执行顺序为
 * preHandle1 -> preHandle2 -> preHandle3 -> afterCompletion2 -> afterCompletion1
 */
public class ICInterceptor1 implements HandlerInterceptor {

    private static final Log logger = LogFactory.getLog(ICInterceptor1.class);

    // Controller执行前
    // handler可以是HandlerMethod，也可以是ResourceHttpRequestHandler
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("preHandle");
        return true;
    }

    // Controller执行后，可对返回结果进行修改
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("postHandle");

    }

    // 请求完全结束后，可以统计请求耗时等
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("afterCompletion");

    }
}
