<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model.entity.* , java.util.List" %>
<%@page import="model.entity.Profession.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/jsp/include/jQuery.jsp"></jsp:include>
<title>MyBookShelf 蔵書検索結果</title>
</head>
<body class="d-flex flex-column vh-100">
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
<main class="mb-auto">
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-sm-12">
				<h1>検索結果</h1>
				<h2><c:if test="${not empty Msg}"><c:out value="${Msg }"/></c:if></h2>
			</div>
			<form method="get">
				<%--ここから共通--%>
				<jsp:include page="/WEB-INF/jsp/include/readonlylist.jsp"></jsp:include>
			<%--ここまで共通--%>
				<div class="col-12 m-3">
					<div class="btn-group" role="group">
					<input type="submit" class="btn btn-info" value="編集" 
					formaction="EditServlet"/>
					<input type="submit" class="btn btn-info" value="再検索" 
					formaction="ListSearchServlet"/>
					</div>
				</div>
			</form>	
		</div>
	</div>
</main>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
</body>
</html>