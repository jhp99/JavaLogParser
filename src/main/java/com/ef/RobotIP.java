package com.ef;

import java.sql.Timestamp;

public class RobotIP {
	
	private String ipAddress,count;
	private Timestamp startDate,endDate;
	
	public RobotIP(String ipAddress, String count,Timestamp startDate,Timestamp endDate) {
		this.ipAddress = ipAddress;
		this.count = count;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public String toString(){
		return this.getIpAddress()+ " was found " + this.getCount() + " times between " + this.getStartDate() + " and " + this.getEndDate();
	}
	
}
