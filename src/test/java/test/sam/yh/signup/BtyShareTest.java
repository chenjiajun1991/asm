package test.sam.yh.signup;

import static org.junit.Assert.*;

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
import com.sam.yh.req.bean.BtyShareReq;

/**
 * <p>
 * User: Zhang Kaitao
 * <p>
 * Date: 14-2-26
 * <p>
 * Version: 1.0
 */
public class BtyShareTest {
    private static final Logger logger = LoggerFactory.getLogger(BtyShareTest.class);

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
    public void testShareBtyService() {

        BtyShareReq reqObj = new BtyShareReq();
        reqObj.setAppName("samyh");
        reqObj.setDeviceType("android");
        reqObj.setVersion("0.0.1");
        reqObj.setUserPhone("15618672987");
        reqObj.setBtyPubSn("A10001");
        reqObj.setFriendPhone("1390000001");
        String jsonReq = JSON.toJSONString(reqObj);
        logger.info("Reuqest json String:" + jsonReq);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/user/bty/share.json").build().toUriString();

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
