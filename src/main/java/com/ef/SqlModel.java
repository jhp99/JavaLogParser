package com.ef;

import java.sql.Timestamp;;

public class SqlModel {
	
	private Timestamp date;
	private String ipAddress, requestType, userAgent;
	private int statusCode;
	
	SqlModel(Timestamp date,String ipAddress,String requestType,int statusCode,String userAgent){
		this.date = date;
		this.ipAddress = ipAddress;
		this.requestType = requestType;
		this.userAgent = userAgent;
		this.statusCode = statusCode;
	}
	
	 public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	
}
