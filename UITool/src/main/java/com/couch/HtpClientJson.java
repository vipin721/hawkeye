package com.couch;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HtpClientJson {
	
	
	
	public  String getResponseString(String uri){
	HttpResponse response=null;
	String respondstring=null;
	
	DefaultHttpClient httpclient = null;
	 
	try
	{
		
	httpclient = new DefaultHttpClient();
	httpclient.getParams().setParameter("http.socket.timeout",  10000);
	httpclient.getParams().setParameter("http.connection.timeout", 10000);
	httpclient.getParams().setParameter("http.connection-manager.timeout", new Long( 10000));
	httpclient.getParams().setParameter("http.protocol.head-body-timeout", 10000);

	
	HttpGet httpget = new HttpGet(uri);
	
	response = httpclient.execute(httpget);
	HttpEntity responseEntity = response.getEntity();
	respondstring = EntityUtils.toString(responseEntity);
	
	
	httpget.abort();
	if (responseEntity != null) {
		
		}
	// System.out.println( respondstring);
	} 
	catch(NullPointerException e){
		
	} catch (ClientProtocolException e) {
		
		e.printStackTrace();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	finally {
	    httpclient.getConnectionManager().shutdown();
	}
	return respondstring;
}
}
