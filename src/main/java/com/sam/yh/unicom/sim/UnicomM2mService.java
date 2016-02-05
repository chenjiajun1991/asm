package com.sam.yh.unicom.sim;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sam.yh.crud.exception.M2mEditTermalException;

@Service
public class UnicomM2mService {

    private static final String ACTIVATE_VALUE = "ACTIVATED_NAME";
    private static final String DEACTIVATE_VALUE = "DEACTIVATED_NAME";

    @Resource
    String m2mUrl;
    @Resource
    String m2mApiKey;
    @Resource
    String m2mUserName;
    @Resource
    String m2mPassword;

    public String activateSimCard(String msisdn) throws M2mEditTermalException {
        try {
            if (msisdn.startsWith("1") && msisdn.length() == 13) {
                msisdn = "86" + msisdn;
            }
            if (!msisdn.startsWith("86") || msisdn.length() != 15) {
                throw new M2mEditTermalException("sim卡卡号错误");
            }
            String iccid = getIccidByMsimdn(msisdn);

            EditTerminalClient client = new EditTerminalClient(m2mUrl, m2mUserName, m2mPassword, m2mApiKey);
            client.callWebService(iccid, ACTIVATE_VALUE);

            return iccid;
        } catch (Exception e) {
            throw new M2mEditTermalException(e);
        }
    }

    public void deactivateSimCard(String msisdn) throws M2mEditTermalException {
        try {
            String iccid = getIccidByMsimdn(msisdn);

            EditTerminalClient client = new EditTerminalClient(m2mUrl, m2mUserName, m2mPassword, m2mApiKey);
            client.callWebService(iccid, DEACTIVATE_VALUE);

        } catch (Exception e) {
            throw new M2mEditTermalException(e);
        }

    }

    private String getIccidByMsimdn(String msisdn) throws M2mEditTermalException {
        try {
            GetTerminalsByMsisdnClient client = new GetTerminalsByMsisdnClient(m2mUrl, m2mUserName, m2mPassword, m2mApiKey);
            return client.callWebService(msisdn);
        } catch (Exception e) {
            throw new M2mEditTermalException(e);
        }
    }

}
