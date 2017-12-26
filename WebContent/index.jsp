<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>test</h1>

	<a href="http://172.16.65.83/smslib-lytx/smslib?sjh=15957303069&dx=接口测试1">测试2</a>
	
	<p>测试的接口</p>
	<p>http://10.141.47.210:9005/smslib-lytx/smslib?sjh=15957303069&dx=最后再测一条qq tall me whta you see</p>
	<form action="smslib" method="get">	
		手机号:<input type="text" id="sjh" name="sjh" /><br>
		短信内容:<input type="text" id="dx" name="dx"/>
		<button type="submit">提交</button>
	</form>
</body>
</html>