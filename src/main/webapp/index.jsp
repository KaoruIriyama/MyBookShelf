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
	<div class="jumbotron mt-4"><%--画像を入れたい --%>
		<h1 class="text-center">My BookShelf</h1>
		<p class="lead">
		国立国会図書館open-searchAPIを用いてISBNから書籍を検索し、<br>
		蔵書一覧に登録します。ISBNが存在しない書籍の場合はキーワード検索から<br>
		書籍を登録できます。</p>
		<div class="btn btn-info">
			<a href="MainServlet" class="text-light">蔵書一覧</a>	
		</div>
		<div class="btn btn-info">
			<a href="ListSearchServlet" class="text-light">蔵書検索</a>	
		</div>
		<div class="btn btn-info">
			<a href="BookSearchServlet" class="text-light">新規登録</a>
		</div>		
	</div>
</div>

</body>
</html>