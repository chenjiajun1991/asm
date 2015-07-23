package com.sam.yh.common.msg;

import javax.xml.bind.annotation.XmlElement;

public class ReplaySms {

    private String phone;

    private String content;

    private String subcode;

    private String delivertime;

    public String getPhone() {
        return phone;
    }

    @XmlElement(name = "phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    @XmlElement(name = "content")
    public void setContent(String content) {
        this.content = content;
    }

    public String getSubcode() {
        return subcode;
    }

    @XmlElement(name = "subcode")
    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getDelivertime() {
        return delivertime;
    }

    @XmlElement(name = "delivertime")
    public void setDelivertime(String delivertime) {
        this.delivertime = delivertime;
    }

}
