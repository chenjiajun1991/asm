package com.sam.yh.resp.bean;

import java.util.ArrayList;
import java.util.List;

public class BtySaleInfoResp {
private List<BtySaleInfo> btySaleInfos=new ArrayList<BtySaleInfo>();

public List<BtySaleInfo> getBtySaleInfos() {
	return btySaleInfos;
}

public void setBtySaleInfos(List<BtySaleInfo> btySaleInfos) {
	this.btySaleInfos = btySaleInfos;
}

}
