package com.sam.yh.common;

import org.apache.commons.lang3.StringUtils;

import com.sam.yh.enums.AppVersionStatus;
import com.sam.yh.req.bean.BaseReq;

public class AppVersionUtils {
    private static String ANDROID_VERSION;
    private static String ANDRIOD_DOWNLOADURL;

    static {
        ANDROID_VERSION = ConfigUtils.getConfig().getString(ConfigUtils.ANDROID_LATEST_VERSION, "0.0.1");
        ANDRIOD_DOWNLOADURL = ConfigUtils.getConfig().getString(ConfigUtils.ANDRIOD_LATEST_DOWNLOADURL,
                "http://samyh.oss-cn-shenzhen.aliyuncs.com/YahengBattery.apk");
    }

    public static String getDownloadUrl() {
        return ANDRIOD_DOWNLOADURL;
    }

    public static AppVersionStatus checkVersion(BaseReq req) {
        String version = req.getVersion();
        if (StringUtils.equalsIgnoreCase("android", req.getDeviceType())) {
            return compair(version, ANDROID_VERSION);
        } else {
            return null;
        }

    }

    private static AppVersionStatus compair(String reqVer, String lastVer) {

        AppVersionStatus status = AppVersionStatus.NO_UPDATE;
        if (StringUtils.equals(reqVer, lastVer)) {
            return status;
        }

        String[] lastVers = lastVer.split("\\.");
        String[] reqVers = reqVer.split("\\.");
        int minor = reqVers.length < 2 ? 0 : Integer.valueOf(reqVers[1]);
        int fix = reqVers.length < 3 ? 0 : Integer.valueOf(reqVers[2]);

        if (Integer.valueOf(lastVers[0]) > Integer.valueOf(reqVers[0])) {
            status = AppVersionStatus.FORCE_UPDATE;
        }
        if (Integer.valueOf(lastVers[0]) == Integer.valueOf(reqVers[0]) && Integer.valueOf(lastVers[1]) > minor) {
            status = AppVersionStatus.FORCE_UPDATE;
        }
        if (Integer.valueOf(lastVers[0]) == Integer.valueOf(reqVers[0]) && Integer.valueOf(lastVers[1]) == minor && Integer.valueOf(lastVers[2]) > fix) {
            status = AppVersionStatus.OPTIONAL_UPDATE;
        }

        return status;
    }

}
