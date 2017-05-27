package com.sam.yh.controller;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.AttributeKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.common.PowerCalUtil;
import com.sam.yh.common.SamConstants;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchBtyInfoException;
import com.sam.yh.model.Battery;
import com.sam.yh.model.BatteryInfoNst;
import com.sam.yh.req.bean.BtyImeiWriteReq;
import com.sam.yh.req.bean.BtyInfoReq;
import com.sam.yh.req.bean.BtyResetReq;
import com.sam.yh.resp.bean.BtyInfoResp;
import com.sam.yh.resp.bean.BtyResetResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.socket.SamBtyDataHandler;

@RestController
@RequestMapping("/bty")
public class FetchBtyInfoController {

	private static final Logger logger = LoggerFactory
			.getLogger(FetchBtyInfoController.class);

	@Autowired
	BatteryService batteryService;

	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public SamResponse fetchBtyInfo(HttpServletRequest httpServletRequest,
			@RequestParam("jsonReq") String jsonReq) {
		logger.info("Request json String:" + jsonReq);
		BtyInfoReq req = JSON.parseObject(jsonReq, BtyInfoReq.class);

		try {
			validateBtyArgs(req);
      //返回值中增加一个信号强度
			BatteryInfoNst info = batteryService
					.fetchBtyInfo(req.getBtySimNo());
			BtyInfoResp respData = new BtyInfoResp();
			respData.setLatitude(info.getLatitude());
			respData.setLongitude(info.getLongitude());
			respData.setTemperature(info.getTemperature());
			respData.setVoltage(info.getVoltage());
			
			Battery battery = batteryService.fetchBtyById(info.getBatteryId());
			respData.setPower(PowerCalUtil.calPower(info.getVoltage(),
					battery.getBtyQuantity()));

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(info.getReceiveDate());
			respData.setLastestDate(dateString);
			
			int rssi = batteryService.fetchRssi(info.getBatteryId());
			respData.setRssi(rssi);

			return ResponseUtils.getNormalResp(respData);
		} catch (IllegalParamsException e) {
			return ResponseUtils.getParamsErrorResp(e.getMessage());
		} catch (CrudException e) {
			logger.error("fetch battery info exception, " + req.getBtySimNo(),
					e);
			if (e instanceof FetchBtyInfoException) {
				return ResponseUtils.getServiceErrorResp(e.getMessage());
			} else {
				return ResponseUtils.getSysErrorResp();
			}
		} catch (Exception e) {
			logger.error("fetch battery info exception, " + req.getBtySimNo(),
					e);
			return ResponseUtils.getSysErrorResp();
		}
	}

	@RequestMapping(value = "/info/real", method = RequestMethod.POST)
	public SamResponse fetchBtyInfoReal(HttpServletRequest httpServletRequest,
			@RequestParam("jsonReq") String jsonReq) {
		logger.info("Request json String:" + jsonReq);
		BtyInfoReq req = JSON.parseObject(jsonReq, BtyInfoReq.class);

		try {
			validateBtyArgs(req);

			sendReq(req.getBtySimNo());

			TimeUnit.SECONDS.sleep(SamConstants.MAX_WAIT_SECONDS);

			BatteryInfoNst info = batteryService
					.fetchBtyInfo(req.getBtySimNo());
			BtyInfoResp respData = new BtyInfoResp();
			respData.setLatitude(info.getLatitude());
			respData.setLongitude(info.getLongitude());
			respData.setTemperature(info.getTemperature());
			respData.setVoltage(info.getVoltage());
			Battery battery = batteryService.fetchBtyById(info.getBatteryId());
			respData.setPower(PowerCalUtil.calPower(info.getVoltage(),
					battery.getBtyQuantity()));

			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(info.getReceiveDate());
			respData.setLastestDate(dateString);
			//增加一个信号强度
			int rssi = batteryService.fetchRssi(info.getBatteryId());
			respData.setRssi(rssi);
			
			logger.info("Response json String:"
					+ JSON.toJSONString(ResponseUtils.getNormalResp(respData)));
			return ResponseUtils.getNormalResp(respData);
		} catch (IllegalParamsException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseUtils.getParamsErrorResp(e.getMessage());
		} catch (CrudException e) {
			logger.error("fetch battery info exception, " + req.getBtySimNo(),
					e);
			if (e instanceof FetchBtyInfoException) {
				return ResponseUtils.getServiceErrorResp(e.getMessage());
			} else {
				return ResponseUtils.getSysErrorResp();
			}
		} catch (Exception e) {
			logger.error("fetch battery info exception, " + req.getBtySimNo(),
					e);
			return ResponseUtils.getSysErrorResp();
		}
	}

	private void validateBtyArgs(BtyInfoReq btyInfoReq)
			throws IllegalParamsException {
		if (!MobilePhoneUtils.isValidM2MPhone(btyInfoReq.getBtySimNo())) {
			throw new IllegalParamsException("请输入正确的电池sim卡号");
		}
	}

	private void sendReq(String simNo) {
		Battery battery = batteryService.fetchBtyBySimNo(simNo);
		if (battery == null) {
			logger.error("电池不存在, " + simNo);
			return;
		}
		boolean hasConn = false;
		ConcurrentHashMap<String, Channel> channelMap = SamBtyDataHandler.channelMap;
		
//		logger.info("长连接IMEISet, {}", JSON.toJSON(channelMap.keySet()));
		logger.info("长连接IMEI-ADDR-Map, {}", JSON.toJSON(SamBtyDataHandler.channelAddrMap));
		
		Channel channel = channelMap.get(battery.getImei());
		if (channel != null) {
			channel.writeAndFlush("tellme" + battery.getImei() + "\n");
			hasConn = true;
		}

		if (!hasConn) {
			logger.error("未获取到长连接, " + simNo);
		}

	}

	// 远程重启功能
	@RequestMapping(value = "/info/resetbty", method = RequestMethod.POST)
	public SamResponse fetchBtyInfoReset(HttpServletRequest httpServletRequest,
			@RequestParam("jsonReq") String jsonReq) {
		logger.info("Request json String:" + jsonReq);
		BtyResetReq req = JSON.parseObject(jsonReq, BtyResetReq.class);

		try {

			boolean isSuccess = sendResetReq(req.getImei());

			TimeUnit.SECONDS.sleep(SamConstants.MAX_WAIT_SECONDS);

			BtyResetResp respData = new BtyResetResp();
			respData.setSuccess(isSuccess);
			Date date = new Date();
			respData.setResetDate(date);

			logger.info("Response json String:"
					+ JSON.toJSONString(ResponseUtils.getNormalResp(respData)));
			return ResponseUtils.getNormalResp(respData);
		} catch (Exception e) {
			logger.error("reset battery exception, " + req.getImei(), e);
			return ResponseUtils.getSysErrorResp();
		}
	}
	
	
	//远程写IMEI号功能
	@RequestMapping(value = "/writeImei", method = RequestMethod.POST)
	public SamResponse fetchBtyInfoWriteImei(HttpServletRequest httpServletRequest,
			@RequestParam("jsonReq") String jsonReq) {
		logger.info("Request json String:" + jsonReq);
		
		BtyImeiWriteReq req = JSON.parseObject(jsonReq, BtyImeiWriteReq.class);

		try {

			boolean isSuccess = sendWriteReq(req.getPreImei(),req.getAltImei());

			TimeUnit.SECONDS.sleep(SamConstants.MAX_WAIT_SECONDS);

			BtyResetResp respData = new BtyResetResp();
			respData.setSuccess(isSuccess);
			Date date = new Date();
			respData.setResetDate(date);

			logger.info("Response json String:"
					+ JSON.toJSONString(ResponseUtils.getNormalResp(respData)));
			return ResponseUtils.getNormalResp(respData);
		} catch (Exception e) {
			logger.error("reset battery exception, " + req.getPreImei(), e);
			return ResponseUtils.getSysErrorResp();
		}
	}
	private boolean sendResetReq(String btyImei) {
		Battery battery = batteryService.fetchBtyByIMEI(btyImei);
		if (battery == null) {
			logger.error("电池不存在, " + btyImei);
			return false;
		}
		boolean hasConn = false;

		ConcurrentHashMap<String, Channel> channelMap = SamBtyDataHandler.channelMap;
		Channel channel = channelMap.get(battery.getImei());
		if (channel != null) {
			channel.writeAndFlush("reset" + battery.getImei() + "\n");
			hasConn = true;
		}

		if (!hasConn) {
			logger.error("未获取到长连接, " + btyImei);
		}
		return hasConn;
	}
	
	private boolean sendWriteReq(String preImei,String altImei) {
		
		boolean hasConn = false;
		
		ConcurrentHashMap<String, Channel> channelMap = SamBtyDataHandler.channelMap;
		Channel channel = channelMap.get(preImei);
		if (channel != null) {
			channel.writeAndFlush("wimei" + altImei + "\n");
			logger.info("write message: " + "wimei" + altImei);
			
			hasConn = true;
			logger.error("imei写入成功, " + altImei);
		}

		if (!hasConn) {
			logger.error("未获取到长连接,无法写入, " + preImei);
		}
		return hasConn;
	}

}
