package com.couch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public  class Match {
	/**
	 * 
	 * putting the id to the database
	 */
	public void setIdToProp(String idIndicator,String idinner){
		
		try {
				Properties prop=new Properties();
				URL myUrl=Match.class.getClassLoader().getResource("resources/config.properties");
				System.out.println(myUrl);
				File myProps = new File(myUrl.toURI());
				FileInputStream in = new FileInputStream(myProps); 
			    prop.load(in);
			   System.out.println(prop.get(idIndicator));
			   if(!prop.containsKey(idIndicator)){
				   	prop.put(idIndicator, idinner);
			   }
	         OutputStream out = new FileOutputStream( myProps);
	         	prop.store(out, "Id Storage");
	       
		} catch (IOException ex) {
			 
	    } catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
	}
	/*
	 * getting id from properties file
	 */
	public String getIdFromProp(String idIndicator){
		String[] twodat = idIndicator.split(",");
		Properties props = new Properties();
		try {
			URL myUrl=Match.class.getClassLoader().getResource("resources/config.properties");
			System.out.println(myUrl);
			File myProps = new File(myUrl.toURI());
			FileInputStream in = new FileInputStream(myProps); 
			props.load(in);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
		return (String) props.get(twodat[0]+","+twodat[1]);
	}
}
