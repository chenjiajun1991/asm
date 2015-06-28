package com.sam.yh.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    private static PropertiesConfiguration config;

    public static final String DAHANT_SERVERURL = "dahant.serverurl";
    public static final String DAHANT_ACCOUNT = "dahant.account";
    public static final String DAHANT_PASSWORD = "dahant.password";
    public static final String SMS_ENABLE = "sms.enable";
    public static final String ADMIN_PHONE = "admin.phone";

    public static PropertiesConfiguration getConfig() {
        if (config == null) {
            synchronized (ConfigUtils.class) {
                if (config == null) {
                    try {
                        config = new PropertiesConfiguration("server.properties");
                    } catch (ConfigurationException e) {
                        logger.error("can not load server.properties file");
                    }
                }
            }
        }

        return config;
    }

}
