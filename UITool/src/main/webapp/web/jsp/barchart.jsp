<%@page import="java.util.ArrayList"%>
<%@page import="com.couch.connection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BAR CHART</title>
<link href="../../content/shared/styles/examples-offline.css" rel="stylesheet"/>
<link href="../../../styles/kendo.common.min.css" rel="stylesheet"/>
<link href="../../styles/kendo.default.min.css" rel="stylesheet"/>
<script type="text/javascript" src="../../jsv/jscharts.js"></script>
</head>
<body>
<a class="offline-button" href="./goc.jsp">Back</a>
<div id="graph">Loading graph...</div>
	<%
		connection conn = new connection();
		String st=request.getParameter("value");
		String list = conn.getArrayData(st, "timeStamp",
				"responseTime");
		String listcol = conn.getcolorArray();
		String datcol=conn.getMouseOver();
	%>;
	<script type="text/javascript">
	var myData = new Array(<%=list%>);
	var colors = [<%=listcol%>];
	var myChart = new JSChart('graph', 'bar');
	myChart.setDataArray(myData);
	myChart.colorizeBars(colors);
	myChart.setTitle(' Distribution per 2 secs');
	myChart.setTitleColor('#8E8E8E');
	myChart.setAxisNameX('TIMELINE');
	myChart.setAxisNameY('RESPONSE TIME (sec)');
	myChart.setAxisColor('#c6c6c6');
	myChart.setAxisWidth(1);
	myChart.setAxisNameColor('#9a9a9a');
	myChart.setAxisValuesColor('#939393');
	myChart.setAxisPaddingTop(40);
	myChart.setAxisValuesAngle(30);
	myChart.setAxisPaddingLeft(50);
	myChart.setAxisPaddingBottom(80);
	myChart.setTextPaddingBottom(20);
	myChart.setTextPaddingLeft(15);
	myChart.setTitleFontSize(11);
	myChart.setBarBorderWidth(0);
	<%if(datcol!=null){%>
	myChart.setTooltip(<%=datcol%>);<%}%>;
	myChart.setBarSpacingRatio(50);
	myChart.setBarValuesColor('#737373');
	myChart.setGrid(false);
	myChart.setSize(1616, 621);
	myChart.setBackgroundImage('./chart_bg.jpg');
	myChart.draw();
</script>
</body>
</html>