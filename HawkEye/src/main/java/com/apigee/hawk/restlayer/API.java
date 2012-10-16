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

@Path("/org/{org_id}/api")
public class API {
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAPI (@PathParam("id") String id){
		
		return Response.ok("GET for api:"+id).build();
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAPI(){
		
		
		return Response.ok("post for api:").build();
	}
	
	@PUT
	@Path ( "{id}" )
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces (MediaType.APPLICATION_JSON)
	public Response updateAPI (@PathParam("id") String id){
		
		
		
		return Response.ok("put for api:"+id).build();
	}
	@DELETE
	@Path ("{id}")
	@Produces (MediaType.APPLICATION_JSON)
	public Response deleteAPI (@PathParam ("id") String id)
	{
		return Response.ok("DELETE for API "+id).build();
	}

}
