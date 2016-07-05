package com.sam.yh.common;

public class PowerCalUtil {
	//修改电量范围
    public static String calPower(String voltage, int btyQuantity) {
        double temp = (Double.parseDouble(voltage) - 10.5d * btyQuantity) / btyQuantity;
        int power = 0;
        if (temp < 0) {
            power = 0;
        } else if (temp >= 0 && temp < 0.75) {
            power = 1;
        } else if (temp >= 0.75&& temp < 1.5) {
            power = 2;
        } else if (temp >=1.5&& temp < 2.25) {
            power = 3;
        } else {
            power = 4;
        }
        return String.valueOf(power);
    }

    /*
     * public static void main(String[] args) {
     * System.out.println(calPower("45.9", 4)); }
     */

}
