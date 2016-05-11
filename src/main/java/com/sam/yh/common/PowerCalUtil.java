package com.sam.yh.common;

public class PowerCalUtil {

    public static int calPower(String voltage, int btyQuantity) {
        double temp = (Double.parseDouble(voltage) - 10.5d * btyQuantity) / btyQuantity;
        int power = 0;
        if (temp < 0) {
            power = 0;
        } else if (temp >= 0 && temp <= 1) {
            power = 1;
        } else if (temp > 1 && temp <= 2) {
            power = 2;
        } else if (temp > 2 && temp <= 3) {
            power = 3;
        } else {
            power = 4;
        }
        return power;
    }

    /*
     * public static void main(String[] args) {
     * System.out.println(calPower("45.9", 4)); }
     */

}
