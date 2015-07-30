package com.sam.yh.common;

public class DistanceUtils {
    // 赤道半径
    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(String lon1, String lat1, String lon2, String lat2) {
        double longtitude1 = Double.valueOf(lon1);
        double latitude1 = Double.valueOf(lat1);
        double longtitude2 = Double.valueOf(lon2);
        double latitude2 = Double.valueOf(lat2);

        return GetDistance(longtitude1, latitude1, longtitude2, latitude2);
    }

    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     * 
     * @param lon1
     *            第一点的精度
     * @param lat1
     *            第一点的纬度
     * @param lon2
     *            第二点的精度
     * @param lat2
     *            第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

}
