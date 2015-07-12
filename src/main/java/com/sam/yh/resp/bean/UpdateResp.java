package com.sam.yh.resp.bean;

public class UpdateResp {
    private String latestVer;
    private String downloadUrl;

    public String getLatestVer() {
        return latestVer;
    }

    public void setLatestVer(String latestVer) {
        this.latestVer = latestVer;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}
