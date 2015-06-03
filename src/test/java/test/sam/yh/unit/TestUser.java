package test.sam.yh.unit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.sam.yh.model.UserExample;
import com.sam.yh.service.UserExampleService;

@RunWith(SpringJUnit4ClassRunner.class)
// 表示继承了SpringJUnit4ClassRunner�?
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestUser {
    private static Logger logger = Logger.getLogger(TestUser.class);
    // private ApplicationContext ac = null;
    @Resource
    private UserExampleService userExampleService = null;

    // @Before
    // public void before() {
    // ac = new ClassPathXmlApplicationContext("applicationContext.xml");
    // userService = (IUserService) ac.getBean("userService");
    // }

    @Test
    public void test1() {
        UserExample user = userExampleService.getUserById(1);
        // System.out.println(user.getUserName());
        // logger.info("值："+user.getUserName());
        logger.info(JSON.toJSONString(user));
    }
}