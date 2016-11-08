package com.sam.yh.resp.bean.web;

import java.util.List;

import com.sam.yh.model.web.TroubleBtyInfo;

public class FetchTroBtyInfoResp {
	private List<TroubleBtyInfo> troubleBtyInfos;

	public List<TroubleBtyInfo> getTroubleBtyInfos() {
		return troubleBtyInfos;
	}

	public void setTroubleBtyInfos(List<TroubleBtyInfo> troubleBtyInfos) {
		this.troubleBtyInfos = troubleBtyInfos;
	}
	
}
