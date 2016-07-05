package com.sam.yh.resp.bean;

public class ResellerSiteResp {

    private String longitude;
    private String latitude;
    private String officeAddress;
    private String resellerName;
    private String resellerPhone;
    private String cityName;

    public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getResellerName() {
		return resellerName;
	}

	public void setResellerName(String resellerName) {
		this.resellerName = resellerName;
	}

	public String getResellerPhone() {
		return resellerPhone;
	}

	public void setResellerPhone(String resellerPhone) {
		this.resellerPhone = resellerPhone;
	}

	public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
