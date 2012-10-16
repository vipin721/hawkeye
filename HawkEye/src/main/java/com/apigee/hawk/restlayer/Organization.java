package com.apigee.hawk.restlayer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path ("/org")
public class Organization {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createOrganization(String payload){
		
		return Response.ok("POST for ORG:").build();
	}
	
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOrganization (@PathParam ("id") String id, String payload){
		return Response.ok("PUT for ORG:"+id).build();
	}
	
	@GET
	@Path ("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganization (@PathParam ("id") String id){
		
		return Response.ok("PUT for ORG:"+id).build();
	}
	
	@DELETE
	@Path ("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(@PathParam ("id") String id){
		return Response.ok("DELETE for ORG:"+id).build();
		
	}
	
	


}
