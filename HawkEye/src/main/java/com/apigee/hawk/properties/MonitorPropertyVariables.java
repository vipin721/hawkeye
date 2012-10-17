package com.apigee.hawk.properties;

public class MonitorPropertyVariables {
	public static String _POST = "POST";
	public static String _GET = "GET";
	public static String NORESPONSE = "NORESPONSE";
	public static String SLOW = "SLOW";
	public static String NORMAL = "NORMAL";
	public static String POTENTIALLYDOWN = "POTENTIALLYDOWN"; 
	public static String FROM = "hawkeye@apigee.com";
	public static String HOST ="smtp.gmail.com";
	static private byte[] PASSWORD = "Welcome@Apigee".getBytes();
	public static byte[] getPASSWORD() {
		return PASSWORD;
	}
	public static String couchDBURL = "http://localhost:5984/";
	public static String monitorLogDB = "monitorlog/";
	/*
	public static void setPASSWORD(byte[] pASSWORD) {
		PASSWORD = pASSWORD;
	}
	*/

}
