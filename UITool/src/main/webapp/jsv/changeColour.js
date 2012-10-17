/*
 * @author Vipin Kumar
 * all the script used for changing colour on the basis of data
 */

$(document).ready(function() {
	changeData();
	//setInterval(doAjaxRequest(), 100);
	$("tbody td").click(function(e) {
		var currentCellText = $(this).text();
		window.location.replace("./barchart.jsp?value="+currentCellText);
	});

    
	setInterval(updown, 1000);
	setupRefresh();
	
});
function setupRefresh() {
	setTimeout("refreshPage();", 30000); // milliseconds
}
function refreshPage() {
	location.reload(true);
}

function changeData(){
	$('.status:contains("POTENTIALLYDOWN")').css('background-color', 'red');
	$('.status:contains("SLOW")').css('background-color', 'yellow');
	$('.status:contains("NORMAL")').css('background-color', 'green');
	$('.status:contains("NORMAL")').css('color','white');
	$('.status:contains("NORMAL")').css("font-size", "16px");
	$('.status:contains("SLOW")').css('color','blue');
	$('.status:contains("SLOW")').css("font-size", "16px");
	$('.status:contains("POTENTIALLYDOWN")').css('color','white');
	$('.status:contains("POTENTIALLYDOWN")').css("font-size", "16px");
}
function findYellow() {

	$("td.blinkYellow").each(function() {
		if ($(this).css("background-color") == "yellow") {
			$(this).css("background-color", "white");
		} else {
			$(this).css("background-color", "yellow");

		}
	});
}




function doAjaxRequest() {
	// here is where the request will happen
	$
			.ajax({
				url : 'http://127.0.0.1:5984/mycouchshop/694fad202af8c6e5c18c98e954000e68',
				type : "GET",

				dataType : 'JSON',
				async : false,

				success : function(data) {
					var i = 0;

					// our handler function will go here
					// this part is very important!
					// it's what happens with the JSON data
					// after it is fetched via AJAX!
				},
				error : function(errorThrown) {
					alert('error');
					console.log(errorThrown);
				}

			});
}

function donormalAjaxRequest() {

	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		alert("namev");
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			alert("pp");
			var namev = document.getElementById("db_name").innerHTML = xmlhttp.responseText;

		} else {
			alert(xmlhttp.status);
		}
	};
	xmlhttp.open("GET", "http://google.com", true);
	xmlhttp.send();

}

function ajaxRequest() {

	var mygetrequest = new donormalAjaxRequest();
	xmlhttp.onreadystatechange = function() {
		if (mygetrequest.readyState == 4) {
			if (mygetrequest.status == 200
					|| window.location.href.indexOf("http") == -1) {
				document.getElementById("result").innerHTML = mygetrequest.responseText;
			} else {
				alert("An error has occured making the request");
			}
		}
	};
	var namevalue = encodeURIComponent(document.getElementById("db_name").value);
	alert(namevalue);
	mygetrequest.open("GET", 'http://127.0.0.1:5984/addressbook/', true);
	mygetrequest.send(null);
}

//function for making screen up and down at regular time interval
function updown() {
	$('html, body').animate({
		scrollTop : $(document).height() - $(window).height()
	}, 2000, function() {
		$(this).animate({
			scrollTop : 0
		}, 2000);
	});
}

function success() {
	alert("data saved");
}

function getInput() {
	alert("data saved");
	$.ajax({
		type : "POST",
		url : "../jsp/callJava.jsp",
		data : "name=john",
		success : function(success) {

		}
	});
	alert("data saved");
}
