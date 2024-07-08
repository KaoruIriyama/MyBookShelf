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
<title>MyBookShelf 登録</title>
</head>
<body class="d-flex flex-column vh-100">
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
<main class="mb-auto">
	<div class="container-fluid">
		<div class="row justify-content-center">
			<div class="col-sm-12">
			<h1>書籍新規登録</h1>
			</div>
			<div>
			<h2><c:if test="${not empty Msg}"><c:out value="${Msg}"/></c:if></h2>
			</div>
		</div>
	<%--ここで空欄のまま送信ボタンを押すと警告が出るようにしたい --%>
		<form action="RecordServlet" method="post">
			<jsp:include page="/WEB-INF/jsp/include/editablelist.jsp"></jsp:include>	
			<div class="col-12 m-3">
				<div class="btn-group" role="group">
					<input type="submit" class="btn btn-info" value="登録"
						onclick="return confirm(以下の内容を登録しますか？);">
					<input type="reset" class="btn btn-info" value="リセット">
				</div>
			</div>
		</form>
	</div>
</main>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
</body>
</html>