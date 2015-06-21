package test.sam.yh.signup;

import static org.junit.Assert.assertEquals;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.sam.yh.req.bean.SmsAuthCodeReq;
import com.sam.yh.req.bean.SysSaltReq;
import com.sam.yh.req.bean.UserSignupReq;

/**
 * <p>
 * User: Zhang Kaitao
 * <p>
 * Date: 14-2-26
 * <p>
 * Version: 1.0
 */
public class SignupTest {
    private static final Logger logger = LoggerFactory.getLogger(SignupTest.class);

    private static Server server;
    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void beforeClass() throws Exception {
        // 创建一个server
        server = new Server(8080);
        WebAppContext context = new WebAppContext();
        String webapp = "F:/github/asm/WebContent";
        context.setDescriptor(webapp + "/WEB-INF/web.xml"); // 指定web.xml配置文件
        context.setResourceBase(webapp); // 指定webapp目录
        context.setContextPath("/");
        context.setParentLoaderPriority(true);

        server.setHandler(context);
        server.start();
    }

    @Test
    public void testSaltService() {

        SysSaltReq reqObj = new SysSaltReq();
        reqObj.setAppName("samyh");
        reqObj.setDeviceType("android");
        reqObj.setVersion("0.0.1");
        reqObj.setUserName("15618672987");
        reqObj.setSaltType("2");
        String jsonReq = JSON.toJSONString(reqObj);
        logger.info("Reuqest json String:" + jsonReq);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/user/getsalt.json").build().toUriString();

        logger.info("Request URL:" + url);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, jsonReq, String.class);

        logger.info("ResponseStatus:" + responseEntity.getStatusCode().value());

        logger.info("ResponseBody:" + responseEntity.getBody());

        assertEquals("hello", responseEntity.getBody());
    }

    @Test
    public void testAuthCodeService() {

        SmsAuthCodeReq reqObj = new SmsAuthCodeReq();
        reqObj.setAppName("samyh");
        reqObj.setDeviceType("android");
        reqObj.setVersion("0.0.1");
        reqObj.setUserPhone("15618672987");
        reqObj.setAuthType("3");
        String jsonReq = JSON.toJSONString(reqObj);
        logger.info("Reuqest json String:" + jsonReq);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/user/sendmsg.json").build().toUriString();

        logger.info("Request URL:" + url);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, jsonReq, String.class);

        logger.info("ResponseStatus:" + responseEntity.getStatusCode().value());

        logger.info("ResponseBody:" + responseEntity.getBody());

        assertEquals("hello", responseEntity.getBody());
    }

    @Test
    public void testSignupService() {

        UserSignupReq reqObj = new UserSignupReq();
        reqObj.setAppName("samyh");
        reqObj.setDeviceType("android");
        reqObj.setVersion("0.0.1");
        reqObj.setUserPhone("15618672987");
        reqObj.setAuthCode("453138");
        reqObj.setDeviceInfo("AAAAAAAAAAAA");
        reqObj.setPassword1("123456789");
        reqObj.setPassword2("123456789");
        String jsonReq = JSON.toJSONString(reqObj);
        logger.info("Reuqest json String:" + jsonReq);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/user/signup.json").build().toUriString();

        logger.info("Request URL:" + url);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, jsonReq, String.class);

        logger.info("ResponseStatus:" + responseEntity.getStatusCode().value());

        logger.info("ResponseBody:" + responseEntity.getBody());

        assertEquals("hello", responseEntity.getBody());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop(); // 当测试结束时停止服务器
    }
}
