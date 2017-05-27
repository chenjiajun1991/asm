package com.sam.yh.foreign.connection;



public interface SendGpsToC1Server {
	
	public void sendBtyInfo(String imei,String longitude,String latitude,String temperature,String voltage);
}
