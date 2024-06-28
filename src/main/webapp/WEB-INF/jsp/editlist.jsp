<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<title>MyBookShelf 編集リスト</title>
</head>
<body class="d-flex flex-column vh-100">
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
<main class="mb-auto">
 <%--ここで空欄のまま送信ボタンを押すと警告が出るようにしたい --%>
	 <div class="container">
		<div class="row justify-content-center">
			<div class="col-sm-12">
				<h1>編集画面</h1>
			</div>
		</div>
		<p><c:if test="${not empty Msg}"><c:out value="${Msg}"/></c:if></p>
		<form method="post">
		<jsp:include page="/WEB-INF/jsp/include/editablelist.jsp"></jsp:include><%--ここのEL式の問題で表示ができない --%>
			<div class="col-12 m-3">
				<div class="btn-group" role="group">
					<input type="submit" class="btn btn-info" value="更新" 
					formaction="UpdateServlet"
					onclick="return confirm('これらの書籍を更新してよろしいですか？');"/>
					<input type="submit" class="btn btn-info" value="削除"
					formaction="DeleteServlet"
					onclick="return confirm('これらの書籍を削除してよろしいですか？');"/>
					<input type="reset" class="btn btn-info" value="リセット" />
				</div>
			</div>
		</form>
	</div>
</main>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
</body>
</html>