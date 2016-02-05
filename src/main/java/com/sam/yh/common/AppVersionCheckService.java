package com.sam.yh.common;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sam.yh.enums.AppVersionStatus;
import com.sam.yh.req.bean.BaseReq;

@Service
public class AppVersionCheckService {

    @Resource
    private String apkVersion;

    @Resource
    private String apkDownloadUrl;

    public AppVersionStatus checkVersion(BaseReq req) {
        String version = req.getVersion();
        if (StringUtils.equalsIgnoreCase("android", req.getDeviceType())) {
            return compair(version, apkVersion);
        } else {
            return null;
        }

    }

    private AppVersionStatus compair(String reqVer, String lastVer) {

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
