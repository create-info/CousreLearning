<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EventWelcome</title>
<link rel="stylesheet" href="public/bower_components/bootstrap/dist/css/bootstrap.css">
</head>
<body background="public/img/leftframe_content.gif">
	<div class="container">
		<div class="row">
			<s:action name="sidebar_topframe" executeResult="true"></s:action>
		</div>
		<div class="row">
			<div class="col-md-3">
				<s:action name="sidebar_stu_eventlist" executeResult="true"></s:action>
			</div>
			<div class="col-md-8 col-md-offset-1">
				<p>请点击左侧事件查看详细信息！</p>
			</div>
		</div>
	</div>
</body>
</html>