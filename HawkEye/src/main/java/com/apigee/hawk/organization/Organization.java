package com.apigee.hawk.organization;

import java.util.Map;

import com.apigee.hawk.api.API;

public interface Organization {
	
	
	
	/**
	 * @return the orgName
	 */
	public String getOrgName() ;
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) ;
	/**
	 * @return the emails
	 */
	public String getEmails() ;
	/**
	 * @param emails the emails to set
	 */
	public void setEmails(String emails) ;
	/**
	 * @return the orgAPI
	 */
	public Map<String, API> getOrgAPI() ;
	/**
	 * @param orgAPI the orgAPI to set
	 */
	public void setOrgAPI(Map<String, API> orgAPI) ;
	
	public void setOrgAPI(String apiName, API api) ;
	
	

}
