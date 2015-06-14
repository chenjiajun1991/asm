package com.sam.yh.common.msg;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "response")
@XmlType(propOrder = { "msgid", "result", "desc", "blacklist", "failPhones" })
public class SmsSendResp {

    private String msgid;
    private int result;
    private String desc;
    private String blacklist;
    private String failPhones;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public String getFailPhones() {
        return failPhones;
    }

    public void setFailPhones(String failPhones) {
        this.failPhones = failPhones;
    }

}
