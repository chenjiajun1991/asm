package com.sam.yh.common.baidugps;

public interface SendGpsToBaiduServer {
	
	//轨迹追踪服务发送经纬度给百度鹰眼	
public String sendGPStoBaiDu(String entity_name,double latitude,double longitude,long loc_time);
}
