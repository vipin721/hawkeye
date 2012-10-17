package com.apigee.hawk.api;

import java.util.HashMap;
import java.util.Map;

public interface API {
	
	public String getOrgName();
	
	public void setOrgName(String orgName);
	
	public String getEmails() ;
	
	public void setEmails(String emails) ;
	
	public String getApiName() ;
	
	public void setApiName(String apiName) ;
	
	public String getUri() ;
	
	public void setUri(String uri) ;
	
	public String getVerb() ;
	
	public void setVerb(String verb) ;
	
	public Map<String, String> getHeaders() ;
	
	public void setHeaders(Map<String, String> headers) ;
	
	public String getPayload() ;
	
	public void setPayload(String payload) ;
	
	public long getPollInterval() ;
	
	public void setPollInterval(long pollInterval) ;
	
	public long getThresholdTries() ;
	
	public void setThresholdTries(long thresholdTries) ;
	
	public long getThresholdTime() ;
	
	public void setThresholdTime(long thresholdTime) ;
		
	public long getOnFailurePollInterval() ;
	
	public void setOnFailurePollInterval(long onFailurePollInterval) ;
	
	public String getAcceptableResponseCodes() ;
	public void setAcceptableResponseCodes(String acceptableResponseCodes);

}
