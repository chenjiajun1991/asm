/** 
 * Creation Date:Jun 19, 201510:00:54 AM 
 * Copyright (c) 2015, 上海佐昊网络科技有限公司 All Rights Reserved. 
 */
package com.sam.yh.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * APP版本检测
 * 
 * @author nate
 */
public class AppVersionCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO Auto-generated method stub
        return super.preHandle(request, response, handler);
    }

}
