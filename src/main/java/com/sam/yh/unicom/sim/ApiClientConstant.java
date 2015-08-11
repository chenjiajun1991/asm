package com.sam.yh.unicom.sim;

import com.sam.yh.common.ConfigUtils;

public interface ApiClientConstant {
    String NAMESPACE_URI = "http://api.jasperwireless.com/ws/schema";
    String PREFIX = "jws";
    String SERVICE_URL = ConfigUtils.getConfig().getString(ConfigUtils.M2M_URL);
    String APIKEY = ConfigUtils.getConfig().getString(ConfigUtils.M2M_APIKEY);
    String USERNAME = ConfigUtils.getConfig().getString(ConfigUtils.M2M_USERNAME);
    String PASSWORD = ConfigUtils.getConfig().getString(ConfigUtils.M2M_PASSWORD);
}