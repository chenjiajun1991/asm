package com.sam.yh.unicom.sim;

import com.sam.yh.crud.exception.M2mEditTermalException;

public class UnicomM2mUtils {

    private static final String ACTIVATE_VALUE = "ACTIVATED_NAME";
    private static final String DEACTIVATE_VALUE = "DEACTIVATED_NAME";

    private UnicomM2mUtils() {
    }

    public static String activateSimCard(String msisdn) throws M2mEditTermalException {
        try {
            if (msisdn.startsWith("1") && msisdn.length() == 13) {
                msisdn = "86" + msisdn;
            }
            if (!msisdn.startsWith("86") || msisdn.length() != 15) {
                throw new M2mEditTermalException("sim卡卡号错误");
            }
            String iccid = getIccidByMsimdn(msisdn);

            EditTerminalClient client = new EditTerminalClient();
            client.callWebService(iccid, ACTIVATE_VALUE);

            return iccid;
        } catch (Exception e) {
            throw new M2mEditTermalException(e);
        }
    }

    public static void deactivateSimCard(String msisdn) throws M2mEditTermalException {
        try {
            String iccid = getIccidByMsimdn(msisdn);

            EditTerminalClient client = new EditTerminalClient();
            client.callWebService(iccid, DEACTIVATE_VALUE);

        } catch (Exception e) {
            throw new M2mEditTermalException(e);
        }

    }

    private static String getIccidByMsimdn(String msisdn) throws M2mEditTermalException {
        try {
            GetTerminalsByMsisdnClient client = new GetTerminalsByMsisdnClient();
            return client.callWebService(msisdn);
        } catch (Exception e) {
            throw new M2mEditTermalException(e);
        }
    }

}
