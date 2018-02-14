<%@ page import="java.io.File" %>
<%@ page import="java.util.*" %>
<%@ page import="potal.common.util.Utility" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>	
<%
	String strWorkParam = request.getParameter("w");
	//String ncid = String.valueOf(java.lang.Math.round(java.lang.Math.random() * 10000000));
	String ncid = "";
	if(strWorkParam == null || strWorkParam.equals("")) strWorkParam = "main";

%>

<!DOCTYPE html>
<html lang="ko" ng-app="comm" ng-controller="CommCtrl" ng-cloak>
<head>
	<title>Potal</title>
    <%@ include file="/WEB-INF/jsp/include/include-header.jsp" %>
    <script>
		var g_webRoot = '${pageContext.request.contextPath}/';
		
		<%
			out.print("document.usrMenu = ");
			out.print(Utility.getJSONString(request.getSession().getAttribute("userMenu")));
		%>
		
		createNewMenu();
		
		function doLogout()
		{
			if(confirm("로그아웃 하시겠습니까?"))
			{
				location.href = '${pageContext.request.contextPath}/logout.do';
			}
		}
		
		function moveMainPage()
		{
			location.href = '${pageContext.request.contextPath}/main.do';
		}
	</script>
	
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/angular/filters.js?v=${ncid}"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular/directives.js?v=${ncid}"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular/services.js?v=${ncid}"></script>	
	
	<%
		
		ServletContext ctx = getServletContext();
		
		String jsPath = String.format("%s/js/content/%s.js", ctx.getContextPath(), strWorkParam);
		String jsRealPath = ctx.getRealPath(String.format("/js/content/%s.js", strWorkParam));
		
		File f = new File(jsRealPath);
		if(f.exists())
		{
			out.println(String.format("<script type='text/javascript' src='%s?v=%s'></script>", jsPath, ncid));
		}
		else
		{
			out.println("<script>function mainController($scope, $timeout, $injector, $filter) {}</script>");
		}
	%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/app.js?v=${ncid}"></script>
</head>
<body>
	<div class="container">
		<%@ include file="/WEB-INF/jsp/include/main_head.jsp"%>
		<div class="body">
	        <div class="contents" ng-controller="MainCtrl">
	           	<% pageContext.include(String.format("/WEB-INF/jsp/content/po_%s.jsp?v=%s", strWorkParam, ncid)); %>
	        </div>
	    </div>
    </div>
    
    <!-- 공통 사용 컴포넌트 -->
	<%@ include file="/common/comm_element.jsp" %>	
</body>
</html>
