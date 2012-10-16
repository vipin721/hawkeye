package com.apigee.hawk.monitorImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.apigee.hawk.api.API;
import com.apigee.hawk.logger.DBLogger;
import com.apigee.hawk.mail.PostMan;
import com.apigee.hawk.mailImpl.PostManImpl;
import com.apigee.hawk.monitor.Monitor;
import com.apigee.hawk.properties.MonitorPropertyVariables;

public class MonitorImpl implements Monitor, Runnable {
	private API api;
	private static Logger logger= Logger.getLogger(MonitorImpl.class);
	private static PostMan postMan= new PostManImpl();

	static SimpleDateFormat sdf;
	public String currentAPIStatus;
	
	boolean potentiallyDownFlag = false;
	boolean slowFlag = false;
	
	String recoveryMessage;
	
	public MonitorImpl() {
	}
	@Override
	public void run() {
		
		//logger = Logger.getLogger(this.getClass());
		
		MonitorImpl.sdf =  new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		List<String>  acceptableResponseCode;
		String responseCode = "NA";
		long startTime;
		long endTime = 0 ;
		
		
		int slowCounter=0;
		int non200Counter=0;
		
		long responseTime;
		HttpResponse response = null;
		
		HttpPost postMethod = null;
		HttpGet getMethod = null;
		
		String emailPayload= new String();
		String emailSubject;
		
		String apiResponse = new String();
		
		StringEntity stringEntity;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpEntity entity = null;
		
		if (api.getVerb().equals(MonitorPropertyVariables._POST))
		{
			postMethod = new HttpPost(api.getUri());
			try {
				byte [] decodedPayload = Base64.decodeBase64(api.getPayload());
				String apiPayload = new String(decodedPayload);
				stringEntity = new StringEntity(apiPayload);
				postMethod.setEntity(stringEntity);
				
				Set <String> keys = api.getHeaders().keySet();
				for ( String key : keys){
					postMethod.setHeader(key, api.getHeaders().get(key));
				}
				
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		else 
		{
			getMethod = new HttpGet(api.getUri());
			Set <String> keys = api.getHeaders().keySet();
			for ( String key : keys){
				getMethod.setHeader(key, api.getHeaders().get(key));
			}
		}
		
		
		while (true)
		{
			this.currentAPIStatus = MonitorPropertyVariables.NORMAL;
			acceptableResponseCode =  (List<String>) Arrays.asList( api.getAcceptableResponseCodes().split(","));
			emailSubject= new String();
			endTime=0;
			startTime =0;
			
			if (api.getVerb().equals(MonitorPropertyVariables._POST))
			{
				try {
					startTime = System.currentTimeMillis();
					response = httpClient.execute(postMethod);
					
					endTime = System.currentTimeMillis();
					entity = response.getEntity();
					
					
				} catch (ClientProtocolException e) {
					endTime = System.currentTimeMillis();
					this.currentAPIStatus= MonitorPropertyVariables.NORESPONSE;
					responseCode ="NA";
					logger.error(e.getLocalizedMessage());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					endTime = System.currentTimeMillis();
					this.currentAPIStatus= MonitorPropertyVariables.NORESPONSE;
					responseCode ="NA";
					logger.error(e.getLocalizedMessage());
					
					
				} finally {
					if (endTime==0)
					{
						endTime = System.currentTimeMillis();
						
					}
					EntityUtils.consumeQuietly(entity);
				}
				
			}
			else 
			{
				try {
					startTime = System.currentTimeMillis();
					response = httpClient.execute(getMethod);
					endTime = System.currentTimeMillis();
					entity = response.getEntity();
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					
					endTime = System.currentTimeMillis();
					this.currentAPIStatus= MonitorPropertyVariables.NORESPONSE;
					responseCode ="NA";
					logger.error(e.getLocalizedMessage());
					
				} catch (IOException e) {
					endTime = System.currentTimeMillis();
					this.currentAPIStatus= MonitorPropertyVariables.NORESPONSE;
					responseCode ="NA";
					apiResponse = e.getLocalizedMessage();
					logger.error(e.getLocalizedMessage());
					
				} finally {
					if (endTime == 0)
					{
						httpClient.getConnectionManager().closeExpiredConnections();
						endTime = System.currentTimeMillis();
						EntityUtils.consumeQuietly(entity);
					}
					EntityUtils.consumeQuietly(entity);
				}
			}
			
			responseTime = endTime - startTime;
			
			if (this.currentAPIStatus.equals(MonitorPropertyVariables.NORESPONSE))
			{
				emailSubject = "Fatal: " +api.getOrgName() +"- API: "+api.getApiName()+ " is not responding"; 
				emailPayload = this.getPayload (responseTime, responseCode, apiResponse);
				DBLogger.log(api.getOrgName(),api.getApiName(),api.getUri(),api.getVerb(),this.currentAPIStatus,responseTime ,sdf.format(new Date()),responseCode, apiResponse);
				logger.debug(api.getOrgName()+","+api.getApiName()+","+api.getUri()+","+api.getVerb()+","+this.currentAPIStatus+","+responseTime +sdf.format(new Date()));
				
				postMan.sendMail(emailSubject, api.getEmails(), emailPayload);
				
				try {
					Thread.sleep(api.getOnFailurePollInterval());
				} catch (InterruptedException e) {
					logger.error(e.getLocalizedMessage());
				}
				
			}
			else
			{
				responseCode = ""+response.getStatusLine().getStatusCode();
				if (!acceptableResponseCode.contains(responseCode))
				{
					apiResponse = this.getResponseFromEntity(response.getEntity());

					non200Counter++;
					this.currentAPIStatus = MonitorPropertyVariables.POTENTIALLYDOWN;
					
					
					
					emailPayload = this.getPayload(responseTime, responseCode, apiResponse);
					DBLogger.log(api.getOrgName(),api.getApiName(),api.getUri(),api.getVerb(),this.currentAPIStatus,responseTime ,sdf.format(new Date()), responseCode, apiResponse);
					logger.debug(api.getOrgName()+","+api.getApiName()+","+api.getUri()+","+api.getVerb()+","+this.currentAPIStatus+","+responseTime +sdf.format(new Date()));;

					if (non200Counter >= api.getThresholdTries())
					{
						
						non200Counter =0;
						emailSubject = "Error: "+api.getOrgName()+"- "+api.getApiName() + ": " +" is potentially down. Responding with Response Code:"+responseCode;
						postMan.sendMail(emailSubject, api.getEmails(), emailPayload);
						potentiallyDownFlag = true;
						
						try {
							Thread.sleep(api.getOnFailurePollInterval());
						} catch (InterruptedException e) {
							logger.error(e.getLocalizedMessage());
						}
						
						/**
						 * Send Email and Wait();
						 */
					}
					else {
						try {
							Thread.sleep(api.getPollInterval());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							logger.debug(e.getLocalizedMessage());
						}
					}
				}
				else if (responseTime > api.getThresholdTime())
				{
					slowCounter ++;
					this.currentAPIStatus = MonitorPropertyVariables.SLOW;
					
					DBLogger.log(api.getOrgName(),api.getApiName(),api.getUri(),api.getVerb(),this.currentAPIStatus,responseTime ,sdf.format(new Date()), responseCode, "");
					//logger.debug(api.getOrgName()+","+api.getApiName()+","+api.getUri()+","+api.getVerb()+","+this.currentAPIStatus+","+responseTime +sdf.format(new Date()));
					
					/**
					 * log here
					 */
					if (slowCounter >= api.getThresholdTries())
					{
						slowCounter = 0;
						slowFlag = true;
						/**
						 * Send Email and wait
						 */
					}
					else {
						try {
							Thread.sleep(api.getPollInterval());
						} catch (InterruptedException e) {
							logger.error(e.getLocalizedMessage());
						}
					}
				}
				
				else{
					non200Counter =0;slowCounter = 0;
					
					this.currentAPIStatus = MonitorPropertyVariables.NORMAL;
					
					DBLogger.log(api.getOrgName(),api.getApiName(),api.getUri(),api.getVerb(),this.currentAPIStatus,responseTime ,sdf.format(new Date()), responseCode, "");
					
					logger.debug(api.getOrgName()+","+api.getApiName()+","+api.getUri()+","+api.getVerb()+","+this.currentAPIStatus+","+responseTime +sdf.format(new Date()));
					if (potentiallyDownFlag){
						
						potentiallyDownFlag = false;
						emailSubject = "Recovered:Normal: "+api.getOrgName()+"- "+api.getApiName() + ": " +" has recovered.";
						recoveryMessage = this.getRecoveryMessage();
						logger.debug("Sending Recovery Email : "+recoveryMessage);
						postMan.sendMail(emailSubject, api.getEmails(), recoveryMessage);
					}
					try {
						Thread.sleep(api.getPollInterval());
					} catch (InterruptedException e) {
						logger.error("InterruptedException: "+e.getMessage());
					}

				}
			}
			
		}
	}
	
	/**
	 * 
	 * @return
	 */
	
	private String getRecoveryMessage() {
		String payload = 
				"Customer       : "+this.api.getOrgName() +"\n" +
				"API            : "+this.api.getApiName() + "\n" +
				"URI            : "+ this.api.getUri() + "\n"+
				"Message:       : "+" The API has Recovered from failure. "+
				"\n"+"\n"+"\n"+
				"--"+"\n"+
				"Sent from Apigee HawkEye API Monitor.";
				
		logger.debug("Sending Email: "+payload);
		return payload;
	}
	private String getResponseFromEntity(HttpEntity entity) {
		InputStream input = null;
		StringWriter writer = new StringWriter();
		String response= new String();
		try {
			input = entity.getContent();
			IOUtils.copy(input, writer);
			response = writer.toString();
			logger.debug("Response : "+response);
			return response;
		} catch (IllegalStateException e) {
			logger.debug("IllegalStateException: "+e.getMessage());
		} catch (IOException e) {
			logger.debug("IOException: "+ e.getMessage());
		}finally {
			if (input != null)
			{
				try {
					input.close();
				} catch (IOException e) {
					logger.error("IOException"+ e.getMessage());
				}
			}
			
		}
		return response;
		
	}
	/**
	 * 
	 * @param responseTime : send Response Time
	 * @param responseCode : Send received response code
	 * @param apiResponse  : Received API Response
	 * @return
	 */
	private String getPayload(long responseTime, String responseCode,
			String apiResponse) {
		String payload = 
				"Customer       : "+this.api.getOrgName() +"\n" +
				"API            : "+this.api.getApiName() + "\n" +
				"URI            : "+ this.api.getUri() + "\n"+
				"Response Time  : "+ responseTime +"\n" +
				"Response Code  : "+ responseCode + "\n" +
				"Response       : "+ apiResponse + "\n"+"\n"+"\n"+
				"--"+"\n"+
				"Sent from Apigee HawkEye API Monitor.";
				
		logger.debug("Sending Email: "+payload);
		return payload;
	}
	
	/**
	 * 
	 */
	
	public API getApi() {
		return api;
	}
	
	/**
	 * 
	 */
	public void setApi(API api) {
		this.api = api;
		PropertyConfigurator.configure("log4j.properties");
	}

}
