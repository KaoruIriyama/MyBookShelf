<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model.entity.* , java.util.List" %>
<%@page import="model.entity.Profession.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<title>MyBookShelf 蔵書一覧</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
<div class="container">
	<div class="row justify-content-center">
		<div class="col-sm-12">
		<h1>蔵書一覧</h1>
		</div>
	</div>
	<%--<h2><c:out value="${Msg}"/></h2>
<form action= "MainServlet" method="post">--%>
	<p><c:if test="${not empty Msg}"><c:out value="${Msg }"/></c:if></p>
	<form method="get">
	<jsp:include page="/WEB-INF/jsp/include/readonlylist.jsp"></jsp:include>
	
		<div class="col-12 m-3">
			<div class="btn-group" role="group">
			<input type="submit" class="btn btn-info" value="編集" formaction="EditServlet"/>
			</div>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
<script src="./checkbox.js"></script>
</body>
</html>