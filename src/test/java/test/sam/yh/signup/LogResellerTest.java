package test.sam.yh.signup;

import static org.junit.Assert.assertEquals;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.sam.yh.req.bean.LogResellerReq;

/**
 * <p>
 * User: Zhang Kaitao
 * <p>
 * Date: 14-2-26
 * <p>
 * Version: 1.0
 */
public class LogResellerTest {
    private static final Logger logger = LoggerFactory.getLogger(LogResellerTest.class);

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
    public void testLogResellerService() {

        LogResellerReq reqObj = new LogResellerReq();
        reqObj.setAppName("samyh");
        reqObj.setDeviceType("android");
        reqObj.setVersion("0.0.1");
        reqObj.setAdminPhone("15618672987");
        reqObj.setResellerName("三毛");
        reqObj.setResellerPhone("13900000012");
        reqObj.setCityName("上海");
        reqObj.setResellerAddress("陆家嘴环路1000号恒生大厦8041");

        String jsonReq = JSON.toJSONString(reqObj);

        logger.info("Reuqest json String:" + jsonReq);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/reseller/info.json").build().toUriString();

        logger.info("Request URL:" + url);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("jsonReq", jsonReq);
        String resp = restTemplate.postForObject(url, params, String.class);

        logger.info("ResponseBody:" + resp);

        assertEquals("hello", resp);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop(); // 当测试结束时停止服务器
    }
}
