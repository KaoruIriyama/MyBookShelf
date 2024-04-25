<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<title>MyBookShelf TOP</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
<div class="container">
	<div class="row justify-content-center">
		<div class="col-sm-12">
		<h1>My BookShelf</h1>
		</div>
	</div>
	<div class="row-sm-12 justify-content-center">
	<a href="MainServlet" class="text-decoration-none">蔵書一覧</a>	
	<a href="ListSearchServlet" class="text-decoration-none">蔵書検索</a>	
	<a href="BookSearchServlet" class="text-decoration-none">新規登録</a>
	</div>
</div>

</body>
</html>