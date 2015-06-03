/*
 * 文件名：StatelessDefaultSubjectFactory.java
 * 版权：Copyright by www.sam-world.com
 * 描述：
 * 修改人：nate
 * 修改时间：2015年6月3日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.sam.yh.mgt;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }

}
