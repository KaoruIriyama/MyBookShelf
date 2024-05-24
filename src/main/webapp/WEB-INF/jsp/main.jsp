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
	<%--<h2><c:out value="${Msg}"/></h2>--%>
	<p><c:if test="${not empty Msg}"><c:out value="${Msg}"/></c:if></p>
	<form method="get">
	<jsp:include page="/WEB-INF/jsp/include/readonlylist.jsp"></jsp:include>
	
		<div class="col-12 m-3">
			<div class="btn-group" role="group">
			<input type="submit" class="btn btn-info" value="編集" formaction="EditServlet"/>
			<%-- ハリボテになっている！->　テーブルが空の場合反応、空でない(蔵書が表示されている)時は無反応
			ついでにbookseach-> searchresult->	mainと移動したときにはリストが空の状態になっていた
			(http://localhost:8080/MyBookShelf/RecordServlet)
			->redirectによりsearchresultからの移動でもリストは出るようになった
			フッターから直接蔵書一覧に飛ぶとリストが出てくる(そして編集ボタンは押せない)
			(http://localhost:8080/MyBookShelf/MainServlet)
			フォワード・リダイレクトを見直せ--%>
			
			</div>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/JavaScript/checkbox.js"></script>
<script src="${pageContext.request.contextPath}/JavaScript/handleSubmitbyChecked.js"></script>
</body>
</html>