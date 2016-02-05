package com.sam.yh.service.socket;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sam.yh.req.bean.BatteryInfoReq;

public class BtyDataConverter {

    private static final Logger logger = LoggerFactory.getLogger(BtyDataConverter.class);

    public static BatteryInfoReq convert(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        BatteryInfoReq req = new BatteryInfoReq();
        String[] arr = StringUtils.split(str, "&");
        for (int i = 0; i < arr.length; i++) {
            String[] pair = StringUtils.split(arr[i], "=");
            try {
                BeanUtils.setProperty(req, pair[0], pair[1]);
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }

        return req;

    }

    // public static void main(String[] args) {
    // convert("imei=10002&imsi=9460067001010000&phonenumber=13661645501&longitude=0.000000&latitude=0.000000&temperature=561&voltage=560.0");
    //
    // convert("imei=10002&imsi=9460067001010000&phonenumber=13661645501&longitude=0.000000&latitude=0.000000&temperature=561&voltage=560.0");
    // }

}
