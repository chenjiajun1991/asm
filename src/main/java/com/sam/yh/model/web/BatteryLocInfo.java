package com.sam.yh.model.web;

public class BatteryLocInfo {
	    private Long id;

	    private Integer batteryId;

	    private String longitude;

	    private String latitude;

	    private String temperature;

	    private String voltage;

	    private String status;

	    private String receiveDate;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getBatteryId() {
			return batteryId;
		}

		public void setBatteryId(Integer batteryId) {
			this.batteryId = batteryId;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getTemperature() {
			return temperature;
		}

		public void setTemperature(String temperature) {
			this.temperature = temperature;
		}

		public String getVoltage() {
			return voltage;
		}

		public void setVoltage(String voltage) {
			this.voltage = voltage;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getReceiveDate() {
			return receiveDate;
		}

		public void setReceiveDate(String receiveDate) {
			this.receiveDate = receiveDate;
		}
	    
	    
}
