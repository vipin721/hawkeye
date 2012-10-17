<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.beans.User"%>
<%@page import="com.daos.UserDAO"%>
<%@page import="com.couch.TableData"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>GOC</title>
<link href="../../content/shared/styles/examples-offline.css" rel="stylesheet"/>
<link href="../../../styles/kendo.common.min.css" rel="stylesheet"/>
<link href="../../styles/kendo.default.min.css" rel="stylesheet"/>
<script type="text/javascript" src="../../jsv/jquery-1.3.1.min.js"></script>
<script type="text/javascript" src="../../jsv/changeColour.js"></script>
<script type="text/javascript" src="../../jsv/couch.js"></script>
<style type="text/css">

table.altrowstable {
	
	
	border-width: 4px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}
table.altrowstable th {
	
	width:400px;
	height:60px;
	border-width: 4px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	width:400px;
	height:20px;
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#d4e3e5;
	
}
.evenrowcolor{
	background-color:#c3dde0;
}

</style>
<script type="text/javascript">
   
  </script>
</head>
<body>

<table class="altrowstable" id="alternatecolor">
<tr>
	<th>Organization Name</th><th>API-1</th><th>API-2</th><th>API-3</th><th>API-4</th><th>API-5</th><th>API-6</th><th>API-7</th><th>API-8</th><th>API-9</th>
</tr>
<%TableData dat=new TableData();String tabbody=dat.getTableBody(); %>
<%=tabbody%>
</table>


</body>
</html>