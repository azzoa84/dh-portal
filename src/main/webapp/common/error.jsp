<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html> 
<html> 

<style>
	.error_wrap {width:100%; height:100%;}
	.error_bg {background:url('../img/error_bg.png') no-repeat;  background-position: top center; background-size: auto; padding-bottom:50px;}
	.error_bg>h2 {text-align:center; padding-top:70px; font-size:2.0em; color:#00aacb; font-family: Times New Roman, sans-serif;}
	.error_bg>p {text-align:center;}
	.error_bg>div {width:100%; margin:0 auto; text-align:center; margin-top:25px;}
	.error_bg>div>span {margin:0 auto; text-align:center; width:60px; border:1px solid #d3433e; background-color:#d95450; height:31px; line-height:31px; display:block;}
	.error_bg>div>span a {color:#fff;}
	.btn {padding:7px 24px 7px 24px; text-align:center; color:#fff; border-radius:3px; cursor:pointer; margin-left:5px;}
	.basic_btn {background-color:#00aacb; border:1px solid #019cc1;}
	
	@media only screen and (max-width:639px) {
	.error_bg>h2 {text-align:center; padding-top:65px; font-size:1.7em; color:#00aacb; font-family: Times New Roman, sans-serif;}
	}
</style>

<head>
	<meta charset="UTF-8">
	<title>Astellas Worknet4.0</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<link href="img/favicon.ico" rel="shortcut icon" type="image/ico"/>
	
	<script type="text/javascript">
	function moveIndexPage() {
		window.location.href = "${pageContext.request.contextPath}/index.jsp";
	}
	</script>
</head>

<body>
<!-- error start -->
<div data-role="page" data-theme="d">
	<div class="error_wrap">
		<div class="error_bg">
			<p style="font-size:12px; font-weight:600;"><c:out value="${requestScope['javax.servlet.error.exception']}"/>&nbsp;:&nbsp;<c:out value="${requestScope['javax.servlet.error.message']}"/></p>
			<h2>페이지 오류가 발생하였습니다. <br/>관리자에게 문의해주세요.</h2>
			<div>
				<span class="btn basic_btn" onclick="moveIndexPage();"><a data-role="button" data-inline="true">홈으로</a></span>
			</div>
		</div>		
	</div>
</div>
<!-- error end -->
 
</body>
</html>