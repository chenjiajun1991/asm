package com.sam.yh.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sam.yh.common.Constants;
import com.sam.yh.realm.StatelessToken;

public class StatelessAuthcFilter extends AccessControlFilter {

    public static final Logger logger = LoggerFactory.getLogger(StatelessAuthcFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String clientDigest = request.getParameter(Constants.PARAM_DIGEST);
        String userName = request.getParameter(Constants.PARAM_USERNAME);

        Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
        params.remove(Constants.PARAM_DIGEST);

        StatelessToken token = new StatelessToken(userName, params, clientDigest);

        try {
            getSubject(request, response).login(token);
        } catch (Exception e) {
            logger.error("login error", e);
            onLoginFail(response);
            return false;
        }

        return true;
    }

    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error");
    }
}
