package com.sam.yh.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @RequestMapping("/api/hello")
    public void hello1(HttpServletRequest httpServletRequest, String[] param1, String param2, HttpServletResponse httpServletResponse) throws IOException {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            logger.info("Header key:" + key + ", header value" + value);
        }

        // TODO

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write("Hello" + param1[0] + param1[1] + param2);
    }
}
