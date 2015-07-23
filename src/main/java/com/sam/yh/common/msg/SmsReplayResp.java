package com.sam.yh.common.msg;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class SmsReplayResp {

    private int result;

    private String desc;

    private List<ReplaySms> sms;

    public int getResult() {
        return result;
    }

    @XmlElement(name = "result")
    public void setResult(int result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    @XmlElement(name = "desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ReplaySms> getSms() {
        return sms;
    }

    @XmlElement(name = "sms")
    public void setSms(List<ReplaySms> sms) {
        this.sms = sms;
    }

}
