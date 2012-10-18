<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.apigee.hawkeye.beans.User"%>
<%@page import="com.apigee.hawkeye.daos.UserDAO"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%
	String userName=request.getParameter("UN");
    String password=request.getParameter("pwd");
    boolean found=false;
    if(userName.equals("admin")&&password.equals("admin")){
    	found=true;
    }
    
    if(found)
    		{
    	response.sendRedirect("./jsp/goc.jsp");
    			
    		}
    else
    {
    
    %>
    
    	<%= "Invalid User" %>
   <% 
    }
    
   %>


</body>
</html>