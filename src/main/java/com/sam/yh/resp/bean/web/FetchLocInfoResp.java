package com.sam.yh.resp.bean.web;

import java.util.List;

import com.sam.yh.model.BatteryInfo;
import com.sam.yh.model.web.BatteryLocInfo;

public class FetchLocInfoResp {
	private List<BatteryLocInfo> batteryInfos;

	public List<BatteryLocInfo> getBatteryInfos() {
		return batteryInfos;
	}

	public void setBatteryInfos(List<BatteryLocInfo> batteryInfos) {
		this.batteryInfos = batteryInfos;
	}

	
}
