package com.sam.yh.common.baidugps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service
public class SendGpsToBaiduServerImpl implements SendGpsToBaiduServer{
	//baidu鹰眼轨迹URL
	@Resource
	private String URL_TRACK;
	
	//开发者创建应用的秘匙，用户的ak，授权使用
	@Resource
	private  String AK;
	
	//开发者轨迹控制台申请的serviceID，作为其唯一标识
	@Resource
	private  int SERVER_ID;
	
	//坐标类型       1：GPS经纬度坐标2：国测局加密经纬度坐标 3：百度加密经纬度坐标。
	private  int COORD_TYPE=1;
	
	
	/**
	 * @param entity_name  entity唯一标识
	 * @param latitude 
	 * @param longitude
	 * @param loc_time  UNIX时间戳
	 * @return 请求结果
	 */
	@Override
	public String sendGPStoBaiDu(String entity_name, double latitude,
			double longitude, long loc_time) {
		String param="ak="+AK+"&service_id="+SERVER_ID+"&latitude="+latitude+"&longitude="+longitude+
				"&coord_type="+COORD_TYPE+"&loc_time="+loc_time+"&entity_name="+entity_name;
		String result="发送失败";
		
		try {
			String str=new String(param.getBytes(),"utf-8");
			 result=sendPost(URL_TRACK, str);
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 result="编码异常";
		}
		
		return result;
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
