package com.apigee.hawk.controllerimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.apigee.hawk.api.API;
import com.apigee.hawk.apiImpl.APIImpl;
import com.apigee.hawk.monitorImpl.MonitorImpl;
import com.apigee.hawk.organization.Organization;
import com.apigee.hawk.organizationImpl.OrganizationImpl;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;

import com.sun.jersey.core.util.Base64;

public class ControllerImpl {
	private static Map <String, Organization > organization=new HashMap<String, Organization>();
	Logger logger = LoggerFactory.getLogger(ControllerImpl.class);;
	
	
	public ControllerImpl (){
		//organization = new HashMap<String, Organization>();
		//logger = LoggerFactory.getLogger(ControllerImpl.class);
	}
	
	public void getAPIMonitor (String jsonPayload){
		String orgName ; 
		String emails;
		String apiName;
		String uri;
		String verb;
		String acceptableResponseCodes="200";
		Map <String, String > headers = null;
		String payload;
		long pollInterval;
		long thresholdTries;
		long thresholdTime;
		long onFailurePollInterval;
		
		Organization org = new OrganizationImpl();
		
		try {
			JSONObject jsonPayloadObject = new JSONObject(jsonPayload);
			
			JSONObject orgJsonObject = jsonPayloadObject.getJSONObject("org");
			orgName = orgJsonObject.getString("name");
			emails = orgJsonObject.getString("emails");
			emails = emails.replace("support@apigee.com", "goc@apigee.com");
			JSONArray apiArray = orgJsonObject.getJSONArray("apis");
			int length = apiArray.length();
			Map< String, API>o = org.getOrgAPI();
			for (int i = 0 ; i < length ; i ++) 
			{
				JSONObject api = apiArray.getJSONObject(i);
				API apiObject = new APIImpl();
				
				apiName = api.getString("name");
				uri = api.getString("uri");
				verb = api.getString("verb");
				JSONObject h = api.getJSONObject("headers");
				@SuppressWarnings("unchecked")
				Iterator<String> itr = h.keys();
				headers = new HashMap<String, String>();
				while (itr.hasNext())
				{
					String key = itr.next();
					headers.put(key, h.getString(key));
				}
				try{
				acceptableResponseCodes = api.getString("acceptableResponseCodes");
				} catch (JSONException je)
				{
					acceptableResponseCodes = "200";
				}
				payload = new String (Base64.decode((api.getString("payload"))));
				
				pollInterval = api.getLong("pollInterval")*1000;
				thresholdTries = api.getLong("thresholdTries");
				thresholdTime = api.getLong("thresholdTime")*1000;
				onFailurePollInterval = api.getLong("onFailurePollInterval")*1000;
				
				apiObject.setOrgName(orgName);
				apiObject.setEmails(emails);
				apiObject.setApiName(apiName);
				apiObject.setUri(uri);
				apiObject.setVerb(verb.toUpperCase());
				apiObject.setHeaders(headers);
				apiObject.setAcceptableResponseCodes(acceptableResponseCodes);
				apiObject.setPayload(payload);
				apiObject.setPollInterval(pollInterval);
				apiObject.setThresholdTime(thresholdTime);
				apiObject.setThresholdTries(thresholdTries);
				apiObject.setOnFailurePollInterval(onFailurePollInterval);
				o.put(apiObject.getApiName(), apiObject);
				
				org.getOrgAPI().put(apiObject.getApiName(), apiObject);
				//System.out.println(org.getOrgAPI().get(apiObject).getApiName());
				
			}
			
			organization.put(org.getOrgName(), org);
			
			this.lauchMonitors(org);
/**			try{
				this.updateDB(jsonPayload);
			} catch (Exception e){
				
			}*/
			
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	public void updateDB(String jsonPayload) {
		Session session = new Session("localhost", 5984);
		Database monitorDB = session.getDatabase("monitor");
		JSONObject jsonOb;
		JSONObject orgObject;// = jsonOb.getJSONObject("org");
		String orgName;
		
		try {
			
			jsonOb = new JSONObject(jsonPayload);
			orgObject = jsonOb.getJSONObject("org");
			orgName = orgObject.getString("name");
			
			Document doc = new Document();
			doc.accumulate("_id", orgName.toLowerCase().replace(" ", "-"));
			doc.accumulate("org",jsonOb.getString("org"));
			monitorDB.saveDocument(doc);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	public void lauchMonitors (Organization org)
	{
		Map< String, API> o = org.getOrgAPI();
		Set<String > keys = o.keySet();
		for ( String key : keys){
			API api = o.get(key);
			this.launcher (api);
		}
	}
	private void launcher(API api) {
		MonitorImpl m = new MonitorImpl();
		m.setApi(api);
		(new Thread(m)).start();
		//System.out.println(m.getApi().getApiName());
	}

	public static void main(String[] args)
	{
		init();
		/*
		Session s = new Session("localhost",5984);
		Database db = s.getDatabase("monitor");
		if (db != null)
		{
			ViewResults vr = db.getAllDocuments();
			System.out.println(vr.getResults());
			for (Document d : vr.getResults())
			{
				try {
					Document doc = db.getDocument(d.getId());
					try {
						System.out.println(new JSONObject().accumulate("org", doc.getJSONObject("org").toString()));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}*/
		
		String input = "{}";
		try {
			FileInputStream stream = new FileInputStream(new File("payload.json"));
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb =  fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			input = Charset.defaultCharset().decode(bb).toString();
			//System.out.println("input: "+input);
			ControllerImpl c = new ControllerImpl();
			c.getAPIMonitor(input);
			
			//(new LauncherImpl()).launchMonitor(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void init(){
		Session couchDBSession = new Session("localhost", 5984);
		ControllerImpl controller = new ControllerImpl();
		Database db = couchDBSession.getDatabase("monitor");
		Database logDB = couchDBSession.getDatabase("monitorlog");
		if ( db != null)
		{
			ViewResults vr = db.getAllDocuments();
			for (Document d : vr.getResults())
			{
				try {
					Document doc = db.getDocument(d.getId());
					JSONObject jsonOb = new JSONObject();
					jsonOb.accumulate("org", doc.getJSONObject("org"));
					controller.getAPIMonitor(jsonOb.toString());
					controller.logger.info("Monitor Launched for with Data: " +jsonOb.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		else {
			couchDBSession.createDatabase("monitor");
		}
		if (logDB == null)
		{
			couchDBSession.createDatabase("monitorLog");
		}
		
		
		
	}

}
