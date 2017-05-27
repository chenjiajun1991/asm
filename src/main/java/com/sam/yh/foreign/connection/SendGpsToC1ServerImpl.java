package com.sam.yh.foreign.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;





import javax.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sam.yh.model.BatterySendInfo;
import com.sam.yh.resp.bean.ResponseUtils;

@Service
public class SendGpsToC1ServerImpl implements SendGpsToC1Server{
	
	 private static final Logger logger = LoggerFactory.getLogger(SendGpsToC1ServerImpl.class);
	
	private static final String ytUrl = "http://jiutong.ngrok.cc/cloudadd";

	@Override
	public void sendBtyInfo(String imei, String longitude, String latitude,
			String temperature, String voltage) {


		
		BatterySendInfo batterySendInfo = new BatterySendInfo();
		
		batterySendInfo.setImei(imei);
		batterySendInfo.setLatitude(latitude);
		batterySendInfo.setLongitude(longitude);
		batterySendInfo.setVoltage(voltage);
		batterySendInfo.setTemperature(temperature);
		
		
//		String parms = "msg={"+"\"imei\""+":"+"\""+imei+"\""+","+"\"longitude\""+":"+"\""+longitude+"\""+","
//		+"\"latitude\""+":"+"\""+latitude+"\""+","+"\"temperature\""+":"+"\""+temperature+"\""+","+"\"voltage\""+":"+"\""+voltage+"\""+"}";
		

		  
          if(imei.startsWith("51104")){
        	  
        	  JSONObject jsonObject = new JSONObject();
    		  
    		  jsonObject.put("imei", imei);
    		  jsonObject.put("longitude", longitude);
    		  jsonObject.put("latitude", latitude);
    		  jsonObject.put("temperature", temperature);
    		  jsonObject.put("voltage", voltage);
    		  
    		  String parms= "msg="+jsonObject.toJSONString();
    		  
//    		  sendPost(ytUrl, parms);
    		  
    		  logger.info("Request json String:" + parms);
        	  	
		  }
		
		
	}
	
	
	
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	
	

}
