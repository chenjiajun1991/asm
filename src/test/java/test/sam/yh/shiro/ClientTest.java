package test.sam.yh.shiro;

import junit.framework.Assert;

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

import com.sam.yh.codec.HmacSHA256Utils;
import com.sam.yh.common.SamConstants;

/**
 * <p>
 * User: Zhang Kaitao
 * <p>
 * Date: 14-2-26
 * <p>
 * Version: 1.0
 */
public class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);

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
    public void testServiceHelloSuccess() {
        String username = "admin";
        String param11 = "param11";
        String param12 = "param12";
        String param2 = "param2";
        String key = "dadadswdewq2ewdwqdwadsadasd";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(SamConstants.PARAM_USERNAME, username);
        params.add("param1", param11);
        params.add("param1", param12);
        params.add("param2", param2);
        params.add(SamConstants.PARAM_DIGEST, HmacSHA256Utils.digest(key, params));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/hello.json").queryParams(params).build().toUriString();

        logger.info("Request URL:" + url);

        ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class);

        logger.info("ResponseStatus:" + responseEntity.getStatusCode().value());

        logger.info("ResponseBody:" + responseEntity.getBody());

        Assert.assertEquals("hello" + param11 + param12 + param2, responseEntity.getBody());
    }

    /*
     * @Test public void testServiceHelloFail() { String username = "admin";
     * String param11 = "param11"; String param12 = "param12"; String param2 =
     * "param2"; String key = "dadadswdewq2ewdwqdwadsadasd";
     * MultiValueMap<String, String> params = new LinkedMultiValueMap<String,
     * String>(); params.add(Constants.PARAM_USERNAME, username);
     * params.add("param1", param11); params.add("param1", param12);
     * params.add("param2", param2); params.add(Constants.PARAM_DIGEST,
     * HmacSHA256Utils.digest(key, params)); params.set("param2", param2 + "1");
     * 
     * String url =
     * UriComponentsBuilder.fromHttpUrl("http://localhost:8080/hello"
     * ).queryParams(params).build().toUriString();
     * 
     * try { ResponseEntity responseEntity = restTemplate.getForEntity(url,
     * String.class); } catch (HttpClientErrorException e) {
     * Assert.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
     * Assert.assertEquals("login error", e.getResponseBodyAsString()); } }
     */

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop(); // 当测试结束时停止服务器
    }
}
