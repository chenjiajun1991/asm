/*
 * 文件名：StatelessToken.java
 * 版权：Copyright by www.sam-world.com
 * 描述：
 * 修改人：nate
 * 修改时间：2015年6月3日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.sam.yh.realm;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;

public class StatelessToken implements AuthenticationToken {

    private static final long serialVersionUID = 6358012166370943307L;

    private String userName;
    private Map<String, ?> params;
    private String clientDigest;

    public StatelessToken(String userName, Map<String, ?> params, String clientDigest) {
        this.userName = userName;
        this.params = params;
        this.clientDigest = clientDigest;
    }

    @Override
    public Object getPrincipal() {
        return userName;
    }

    @Override
    public Object getCredentials() {
        return clientDigest;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, ?> getParams() {
        return params;
    }

    public void setParams(Map<String, ?> params) {
        this.params = params;
    }

    public String getClientDigest() {
        return clientDigest;
    }

    public void setClientDigest(String clientDigest) {
        this.clientDigest = clientDigest;
    }

}
