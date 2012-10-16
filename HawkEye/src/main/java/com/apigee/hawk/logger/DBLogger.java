package com.apigee.hawk.logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.apigee.hawk.properties.MonitorPropertyVariables;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Session;

/**
 * 
 * @author Abhishek Tyagi
 *
 */

public class DBLogger {
	Session couchDBSession;
	Database db;
	HttpClient httpClient;
	private static Logger logger;

	private DBLogger(){
		couchDBSession = new Session("localhost", 5984);
		db = couchDBSession.getDatabase("monitorlog");
		
		
		if (db == null)
		{
			couchDBSession.createDatabase("monitorlog");
		}
	}
	
	private static class DBLoggerHolder {
		public static final DBLogger INSTANCE = new DBLogger();
	}
	
	public static DBLogger getInstance (){
		
		return DBLoggerHolder.INSTANCE;
	}
	/**
	 * logger.debug(api.getOrgName()+","+api.getApiName()+","+api.getUri()+","+api.getVerb()+","+this.currentAPIStatus+","+responseTime +sdf.format(new Date()));
	 * @param apiResponse 
	 * @param responseCode 
	 */
	public static void log (String apiOrgName, String apiName, String apiURI, 
			String apiVerb, String apiStatus, long responseTime, String timeStamp, String responseCode, String apiResponse)
	{
		logger = Logger.getLogger(DBLogger.class); 
		HttpEntity entity = null ;
		String _id ;//= apiOrgName.toLowerCase()+"-"+apiName.replace(" ", "-").toLowerCase();
		_id = ((apiOrgName +"-"+apiName).replace(" ", "")).toLowerCase();
		HttpResponse response;
		HttpClient httpClient;
		JSONObject log = new JSONObject();
		String _rev;
		
		
		String uri = MonitorPropertyVariables.couchDBURL+MonitorPropertyVariables.monitorLogDB+_id;
		
		
		/**
		 * First get the _rev if exists
		 */
		httpClient = new DefaultHttpClient();
		HttpHead httpHead = new HttpHead(uri);
		
		try {
			response = httpClient.execute(httpHead);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				_rev = response.getHeaders("Etag")[0].getValue().replace("\"", "");
				log.accumulate("_id", _id);
				log.accumulate("_rev", _rev);
			}
		} catch (ClientProtocolException e2) {
			logger.error("ClientProtocolException: "+e2.getMessage());
		} catch (IOException e2) {
			logger.error("IOException"+e2.getMessage());
		} catch (JSONException e) {
			logger.error("JSONException: "+e.getMessage());
		}
		
		
		
		try {
			log.accumulate("apiOrgName", apiOrgName);
			log.accumulate("apiName", apiName);
			log.accumulate("apiURI", apiURI);
			log.accumulate("apiVerb", apiVerb);
			log.accumulate("apiStatus", apiStatus);
			log.accumulate("responseTime", responseTime);
			log.accumulate("timeStamp", timeStamp);
			log.accumulate("responseCode", responseCode);
			log.accumulate("apiResponse", apiResponse);
			
		} catch (JSONException e1) {
			logger.debug("JSONException: "+e1.getMessage());
		}
		
		try {
			entity = new StringEntity(log.toString());
		} catch (UnsupportedEncodingException e1) {
			logger.error("UnsupportedEncodingException : "+ e1.getMessage());
		}
		
		HttpPut httpPut = new HttpPut(uri);
		httpPut.setEntity(entity);
		httpClient = new DefaultHttpClient();
		try {
			logger.debug("Logging into db: "+uri);
			response = httpClient.execute(httpPut);
			logger.debug("Response Code: "+response.getStatusLine().getStatusCode());
			
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocol Exception: "+e.getMessage());
		} catch (IOException e) {
			logger.error("IOException: "+e.getMessage());
		} catch (Exception e){
			logger.error("Exception Occured: "+ e.getMessage());
		} catch ( Throwable t)
		{
			logger.error("Error Occured: "+t.getMessage());
		}
		finally {
			httpClient.getConnectionManager().closeExpiredConnections();
			
		}
		
		
	}

}
