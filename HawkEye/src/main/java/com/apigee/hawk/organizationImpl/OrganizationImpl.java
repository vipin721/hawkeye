package com.apigee.hawk.organizationImpl;

import java.util.HashMap;
import java.util.Map;

import com.apigee.hawk.api.API;
import com.apigee.hawk.organization.Organization;

public class OrganizationImpl implements Organization {
	private String orgName;
	private String emails;
	private Map<String, API> orgAPI;
	
	public OrganizationImpl(){
		this.orgAPI = new HashMap<String, API>();
	}
	
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the emails
	 */
	public String getEmails() {
		return emails;
	}
	/**
	 * @param emails the emails to set
	 */
	public void setEmails(String emails) {
		this.emails = emails;
	}
	/**
	 * @return the orgAPI
	 */
	public Map<String, API> getOrgAPI() {
		return orgAPI;
	}
	/**
	 * @param orgAPI the orgAPI to set
	 */
	public void setOrgAPI(Map<String, API> orgAPI) {
		this.orgAPI = orgAPI;
	}
	
	public void setOrgAPI(String apiName, API api){
		this.orgAPI.put(apiName, api);
		
	}
	
	

}
