<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<c:set var="ncid"><%= java.lang.Math.round(java.lang.Math.random() * 1000000) %></c:set>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0">
	<title>Login</title>
	
	<!-- css -->
	<link href="${pageContext.request.contextPath}/img/favicon.ico" rel="shortcut icon" type="image/ico">
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/base.css?v=${ncid}'/>" /> 
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/potal/login.css?v=${ncid}'/>" /> 
	
	<!-- jQuery -->
	<script src="<c:url value='/js/lib/external/common/jquery-1.11.1.min.js?v=${ncid}'/>"></script>
	<script src="<c:url value='/js/lib/external/common/common.js?v=${ncid}'/>"></script>
	<script type="text/javascript">
		<%
		String exceptionMsg = (String) request.getAttribute("exceptionMsg");
		
		if (exceptionMsg != null && !exceptionMsg.equals("")) {
			out.println("alert('" + exceptionMsg + "');");
		}
		%>
		var ieVersionNa = null;
		
	    function pageLoaded() {
			var saveUserId = getCookie('potal_save_id'); 
	    	var userId = document.getElementById('j_username');
			var userPassword = document.getElementById('j_password_scr');
	
			if (saveUserId) {
					userId.value = saveUserId;
					userPassword.focus();
			} else {
					userId.focus();
			}  
		}
	    
	    function login(){
	    	var pwd_src = document.getElementById('j_password_scr');
			var pwd = document.getElementById('j_password');
	      
			if (document.getElementById('j_username').value == '') {
				alert('아이디를 입력하십시오.');
				return;
			}
	
			if (pwd_src.value == '') {
				alert('비밀번호를 입력하십시오.');
				return;
			}
			
	        saveUserId();
	        
	        pwd.value = md5(pwd_src.value);
			pwd_src.value = "";
			document.f.submit();
	    }
	    
	    function saveUserId() {
			var userId = document.getElementById('j_username');
			
			setCookie('potal_save_id', userId.value, 365);
		}
	    
	    function moveFocus(e) {
	    	if (e.keyCode != '13')
				return;
	
			if (e.currentTarget.id == 'j_username') {
					document.getElementById("j_password_scr").focus();
			} else if (e.currentTarget.id == 'j_password_scr') {
					login();
			}
		}
	</script>
</head>

<body onload="pageLoaded();">
	<div class="login_wrapper">
		<form name='f' action='j_spring_security_check' method='POST'>
			<input type=hidden name="use_tablet" id="use_tablet" value="N"/>
			<input type=hidden name="tablet_serial" id="tablet_serial" value="-1"/>
			<input type=hidden name="j_password" id="j_password" value=""/>
			<div class="login">
				<p class="center">아이디와 패스워드를 입력하시면 Potal 시스템을 이용하실 수 있습니다.</p>
				
				<div class="content">
					<figure>
						<img src="<c:url value='/img/login_tit.png'/>" />
						<figurecaption>※ 로그인이 필요한 서비스 입니다</figurecaption>
					</figure>
					<fieldset>
						<legend>로그인</legend>
						<dl>
							<dt>아이디</dt>
							<dd><input type="text" id="j_username" name="j_username" placeholder="ID" onkeydown="moveFocus(event);"></dd>
							<dt>비밀번호</dt>
							<dd><input type='password' id='j_password_scr' name='j_password_scr' placeholder="Password" onkeydown="moveFocus(event);"></dd>
						</dl>
						<button type="button" onclick="login();">로그인</button>
					</fieldset>
				</div><!-- //content -->
			</div><!-- //login -->
			<!-- 푸터 -->
			<div class="footerwrap">
				<footer class="footer">
					<p>ⓒ 2017. Potal Korea, Inc. all rights reserved.</p>
				</footer>
			</div>
		</form>
	</div><!-- //etc -->
	<%-- <form name='f' action='j_spring_security_check' method='POST'>
		<input type=hidden name="j_password" id="j_password" value="" />
		<div class="login">
			<div class="login_top">
				<ul>
					<li>Potal V1.0</li>
					<li style="position: relative;">아이디와 패스워드를 입력하시면 시스템을 이용하실 수 있습니다.</li>
				</ul>

			</div>
			<div class="login_con">
				<ul>
					<li><img src="<c:url value='/img/login_tit.png'/>" /><span
						style="font-size: 0.8em;">※ 로그인이 필요한 서비스 입니다.</span></li>
					<li>
						<ul>
							<li><span>아이디</span>&nbsp; <input type="text"
								id="j_username" name="j_username" placeholder="ID"
								autocomplete="nope" onkeydown="moveFocus(event);"></li>
							<li><span>비밀번호</span>&nbsp; <input type='password'
								id='j_password_scr' name='j_password_scr' placeholder="Password"
								autocomplete="new-password" onkeydown="moveFocus(event);">
							</li>
							<a onclick="login();" id="login" style="cursor: pointer;">로그인</a>
						</ul>
					</li>
				</ul>
			</div>
			<!-- 푸터 -->
			<div class="footerwrap">
				<footer class="footer">
					<p>ⓒ 2017. Potal Korea, Inc. all rights reserved.</p>
				</footer>
			</div>
		</div>
	</form> --%>
</body>
</html>