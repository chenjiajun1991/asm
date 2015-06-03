/*
 * �ļ���StatelessRealm.java
 * ��Ȩ��Copyright by www.sam-world.com
 * ������
 * �޸��ˣ�nate
 * �޸�ʱ�䣺2015��6��3��
 * ���ٵ��ţ�
 * �޸ĵ��ţ�
 * �޸����ݣ�
 */

package com.sam.yh.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sam.yh.codec.HmacSHA256Utils;

public class StatelessRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(StatelessRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("admin");
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        StatelessToken statelessToken = (StatelessToken) token;
        String userName = statelessToken.getUserName();
        String key = getKey(userName);

        String serverDigest = HmacSHA256Utils.digest(key, statelessToken.getParams());
        logger.info(statelessToken.getClientDigest());
        logger.info(serverDigest);

        return new SimpleAuthenticationInfo(userName, serverDigest, getName());
    }

    private String getKey(String userName) {
        if ("admin".equals(userName)) {
            return "dadadswdewq2ewdwqdwadsadasd";
        }

        return null;
    }

}
