package com.sam.yh.common.msg;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("defaultUmsSmsService")
public class UmsSmsServiceImpl implements DahantSmsService {

    private static final Logger logger = LoggerFactory.getLogger(UmsSmsServiceImpl.class);

    @Resource
    private String umsSpCode;
    @Resource
    private String umsUserName;
    @Resource
    private String umsPassword;
    @Resource
    private Boolean smsEnable;
    @Resource
    private String apkShortUrl;

    @Override
    public boolean sendSignupAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "您正在注册为亚亨蓄电池会员，注册验证码为" + authCode + "，请在30分钟内使用！";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendResetPwdAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "您正在重置亚亨蓄电池会员密码，验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendTestSms(String mobilePhone, String content) {
        // TODO
        // String content = "您正在测试亚亨蓄电池短信验证码，验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendLogResellerSuccess(String mobilePhone, String initPwd) {
        // TODO
        String content = "恭喜您已经成为亚亨蓄电池经销商，初始密码为" + initPwd + "，请点击下载" + apkShortUrl;
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendLogResellerSuccess(String mobilePhone) {
        // TODO
        String content = "恭喜您已经成为亚亨蓄电池经销商，请登录APP查看。";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendBuyInfo(String mobilePhone) {
        // TODO
    	//        String content = "您购买的亚亨蓄电池已经成功录入系统，请您下载APP并跟踪，请点击下载" + apkShortUrl + "，回复TD拒收";
//        String content = "您购买的亚亨蓄电池已经成功录入系统，初始密码为88888888，请您下载APP并跟踪，请点击下载" + apkShortUrl + "，回复TD拒收";
        
        String content = "恭喜您购买的云电池已注册成功,账号:"+mobilePhone+"初始密码为:88888888,安卓用户请点击链接下载"+apkShortUrl+"苹果用户请在app store里输入 '亚亨云电池'搜索下载";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendWarningMsg(String mobilePhone, String btyImei) {
        // TODO
//        String content = "您的电池IMEI" + btyImei + "温度或电压出现异常，请登陆APP查看。";
       
        String content = "您的电池温度过高，请及时查看电池状态。";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendVoltageWarningMsg(String mobilePhone, String btyImei, String voltage,int flag) {
    	 // TODO
    	String content = "您的电池电量过低，为保证您的正常出行，请尽快充电。";
    	if(flag==0){
    		content = "您的电池电量过低，为保证您的正常出行，请尽快充电。";
    	}else if(flag==1){
    		 content = "您的电池电压过高，请查看电池状态。";
    	}

        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendMovingMsg(String mobilePhone, String btyImei) {
        // TODO
    	//需要多次发送布防报警信息，加一个时间变量来区别不是同一个内容的短信
    	
    	Date date=new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(date);
        String content = "您的电池IMEI" + btyImei + "设置位置锁定后发生异常移动，请登录APP查看最新地点。如您忘记关闭布防，请及时关闭。"+" "+dateString;
        
//        String content = "您的电池IMEI" + btyImei + "设置位置锁定后发生异常移动，请登录APP查看最新地点。如您忘记关闭布防，请及时关闭。";
        return sendSms(mobilePhone, content);
    }

    private boolean sendSms(final String mobilePhone, String content) {
        logger.info("send sms to " + mobilePhone + ", content:" + content);
        if (!smsEnable) {
            return true;
        }

        return UmsClientUtils.sendSms(umsSpCode, umsUserName, umsPassword, mobilePhone, content);

    }

    @Override
    public String getSms() {
        return "";
    }

  //增加一个剪断信号线电压突变报警
	@Override
	public boolean sendViolentDestroyClient(String mobilePhone) {
		String content = "您的电池正在被暴力破坏,请立即查看。";
		return sendSms(mobilePhone, content);
	}

	@Override
	public boolean sendViolentDestroyService(String mobilePhone,
			String btyImei, String userName, String userPhone) {
		String content = "用户"+userName+"的电池IMEI"+btyImei+"信号线被剪断，手机号"+userPhone+"，请及时与用户联系。";
		return sendSms(mobilePhone, content);
	}

	@Override
	public boolean sendMovingRemindMsg(String mobilePhone, String btyImei) {
		String content = "请及时查看车辆是否移动,并确认车辆停放后是否重新布防,若不是,重新关闭再开启后生效,若不使用,请及时关闭布防.";
		return sendSms(mobilePhone, content);
	}

	@Override
	public boolean sendWarnFireMsg(String mobilePhone,String btyImei,String userName,String userPhone) {
		
        String content = "用户"+userName+"的电池IMEI"+btyImei+",电池有着火风险,手机号"+userPhone+",请及时与用户联系。";
		
		return sendSms(mobilePhone, content);
	}

}
