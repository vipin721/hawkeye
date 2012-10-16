package com.apigee.hawk.apiImpl;

import java.util.HashMap;
import java.util.Map;

import com.apigee.hawk.api.API;

public class APIImpl implements API {
	private String orgName;
	private String emails;
	private String apiName;
	private String uri ; 
	private String verb;
	private Map<String, String> headers;
	private String acceptableResponseCodes;
	
	private String payload;
	private long pollInterval;
	private long thresholdTries;
	private long thresholdTime; 
	private long onFailurePollInterval;
	
	/**
	 * @author Abhishek
	 */
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getEmails() {
		return emails;
	}
	public void setEmails(String emails) {
		this.emails = emails;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public long getPollInterval() {
		return pollInterval;
	}
	public void setPollInterval(long pollInterval) {
		this.pollInterval = pollInterval;
	}
	public long getThresholdTries() {
		return thresholdTries;
	}
	public void setThresholdTries(long thresholdTries) {
		this.thresholdTries = thresholdTries;
	}
	public long getThresholdTime() {
		return thresholdTime;
	}
	public void setThresholdTime(long thresholdTime) {
		this.thresholdTime = thresholdTime;
	}
	public long getOnFailurePollInterval() {
		return onFailurePollInterval;
	}
	public void setOnFailurePollInterval(long onFailurePollInterval) {
		this.onFailurePollInterval = onFailurePollInterval;
	}
	@Override
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;		
	}
	public String getAcceptableResponseCodes() {
		return acceptableResponseCodes;
	}
	public void setAcceptableResponseCodes(String acceptableResponseCodes) {
		this.acceptableResponseCodes = acceptableResponseCodes;
	} 

}
