package com.sleepy.zeo.exception_resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ExceptionResolver {

    private static final Log logger = LogFactory.getFactory().getInstance(ExceptionResolver.class);

    // 只能处理程序运行中的异常
    @ExceptionHandler
    public void handleError(Exception e, HttpServletResponse response) throws IOException {
        logger.info("Exception: " + e);
        response.getWriter().write("Exception: " + e);
    }
}
