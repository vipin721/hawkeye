package com.apigee.hawk.restlayer;

import java.security.Identity;
import java.util.HashSet;
import java.util.Set;

public class Application extends javax.ws.rs.core.Application{
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>>  classes  = new  HashSet<Class<?>>();
		classes.add( Identity.class);
		return classes;
	}

}
