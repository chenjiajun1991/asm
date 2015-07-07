package test.sam.yh.original;

import com.alibaba.fastjson.JSON;
import com.sam.yh.req.bean.BaseReq;
import com.sam.yh.req.bean.UserSignupReq;

public class AppVersionTest {

    public static void main(String[] args) {
        UserSignupReq req = new UserSignupReq();
        req.setAuthCode("123456");
        req.setDeviceInfo("AAAAA");
        req.setPassword1("123456789");
        req.setPassword2("123456789");
        req.setUserPhone("1234");
        req.setAppName("jucaifu");
        req.setVersion("0.0.1");
        req.setDeviceType("ios");

        String jsonStr = JSON.toJSONString(req);
        System.out.println(jsonStr);

        BaseReq baseReq = JSON.parseObject(jsonStr, BaseReq.class);
        System.out.println(JSON.toJSON(baseReq));
    }

}
