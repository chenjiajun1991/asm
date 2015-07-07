package test.sam.yh.jwt;

import static org.junit.Assert.assertEquals;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

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
import com.sam.yh.req.bean.UserSigninReq;

public class UserSigninTest {
    private static final Logger logger = LoggerFactory.getLogger(UserSigninTest.class);

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
    public void testSigninService() {

        UserSigninReq reqObj = new UserSigninReq();
        reqObj.setAppName("samyh");
        reqObj.setDeviceType("android");
        reqObj.setVersion("0.0.1");
        reqObj.setUserPhone("13900000014");
        reqObj.setPassword("123456789");
        reqObj.setDeviceInfo("XXXXXXXXXXX");

        String jsonReq = JSON.toJSONString(reqObj);

        logger.info("Reuqest json String:" + jsonReq);

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/user/signin.json").build().toUriString();

        logger.info("Request URL:" + url);

        Key key = MacProvider.generateKey();
        String s = createJWT(key, jsonReq);
        logger.info("jwt:" + s);

        String str = decodeJWT(key, s);
        logger.info("jwt:" + str);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("jsonReq", jsonReq);
        String resp = restTemplate.postForObject(url, params, String.class);

        logger.info("ResponseBody:" + resp);

        assertEquals(jsonReq, str);
    }

    private String createJWT(Key key, String qs) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", "hello");
        claims.put("jsonReq", qs);
        String s = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, key).compact();

        return s;
    }

    private String decodeJWT(Key key, String jwt) {
        String s = (String) Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().get("jsonReq");
        return s;
    }

    @AfterClass
    public static void afterClass() throws Exception {
        server.stop(); // 当测试结束时停止服务器
    }
}
