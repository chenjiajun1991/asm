package test.sam.yh.original;

import static org.junit.Assert.assertEquals;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UploadBtyInfoTest {
    private static final Logger logger = LoggerFactory.getLogger(UploadBtyInfoTest.class);

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
    public void testUploadBtyInfoService() {

        String imei = "10013";
        String longitude = "121.401422";
        String latitude = "31.175332";
        String temperature = "40";
        String voltage = "220";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("imei", imei);
        params.add("longitude", longitude);
        params.add("latitude", latitude);
        params.add("temperature", temperature);
        params.add("voltage", voltage);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/upload/btyinfo.json").queryParams(params).build().toUriString();

        logger.info("Request URL:" + url);

        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

        logger.info("ResponseBody:" + resp.getBody());

        assertEquals("ok", resp.getBody());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop(); // 当测试结束时停止服务器
    }
}
