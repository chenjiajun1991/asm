package com.sam.yh.resp.bean.web;

import java.util.ArrayList;
import java.util.List;

import com.sam.yh.model.web.BtySaleInfoModel;

public class FetchBtyByPhoneResp {
	private List<BtySaleInfoModel> btySaleInfos=new ArrayList<BtySaleInfoModel>();

	public List<BtySaleInfoModel> getBtySaleInfos() {
		return btySaleInfos;
	}

	public void setBtySaleInfos(List<BtySaleInfoModel> btySaleInfos) {
		this.btySaleInfos = btySaleInfos;
	}
	
}
