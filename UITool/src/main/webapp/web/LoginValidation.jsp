<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.beans.User"%>
<%@page import="com.daos.UserDAO"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%
	String userName=request.getParameter("UN");
    String password=request.getParameter("pwd");
    
    User user=new User();
    user.setuName(userName);
    user.setPwd(password);
    
    UserDAO uDao=new UserDAO();
    boolean found=uDao.findUser(user);
    
    if(found)
    		{
    	response.sendRedirect("./menu/index.html");
    			
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