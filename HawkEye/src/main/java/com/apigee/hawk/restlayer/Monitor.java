package com.apigee.hawk.restlayer;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import com.apigee.hawk.controllerimpl.ControllerImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/monitor")
public class Monitor {
	

	@GET 
	@Produces(MediaType.APPLICATION_JSON)
	public String getMonitors (){
		Factory<SecurityManager> factory =  new  IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject currentUser = SecurityUtils.getSubject();
		
		if (! currentUser.isAuthenticated())
		{
			
			UsernamePasswordToken token = new UsernamePasswordToken("guest", "guest");
			token.setRememberMe(true);
			
			
			try{
				currentUser.login(token);
				System.out.println("DONE");
			
			}catch (Exception e){
				System.out.println(e.getLocalizedMessage());
			} catch (Throwable t)
			{
				
			}
		}
	
		
		return "{}";
	}
	@POST
	@Produces (MediaType.APPLICATION_JSON)
	public Response createMonitors(String data){
		Response resp;
		boolean validJson = isValidJson(data);
		
		if (validJson)
		{
			ControllerImpl monitorServer = new ControllerImpl();
			monitorServer.updateDB(data);
			monitorServer.getAPIMonitor(data);
			//monitorServer.launch();
			resp = Response.ok("Monitor API Successfully Launched.").build();
			return resp;
		}
		else 
		{
			resp = Response.serverError().build();
			return resp;
		}
		

	}
	private static boolean isValidJson(String data) {
		boolean valid = false;
		try {
			JsonParser parser = new ObjectMapper().getJsonFactory().createJsonParser(data);
			while (parser.nextToken() != null)
			{
			}
			valid = true;
		} catch (JsonParseException e) {
			valid = false;
		} catch (IOException e) {
			valid = false;
		}
				
		return valid;
	}
	


	

}
