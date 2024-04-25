<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<title>MyBookShelf 書籍検索</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-sm-12">
			<h1>新規書籍検索</h1>
			</div>
		</div>
	<%--ここで空欄のまま送信ボタンを押すと警告が出るようにしたい --%>
		<form>
		<jsp:include page="/WEB-INF/jsp/include/searchform.jsp"></jsp:include> 
		<%--<%@include file="/WEB-INF/jsp/include/searchform.jsp" %>--%>
			<div class="col-12 m-3">
				<div class="btn-group" role="group">
					<input type="submit" class="btn btn-info" value="検索" 
					formaction="BookSearchServlet"  formmethod="post">
					<input type="reset" class="btn btn-info" value="リセット">
					<input type="submit" class="btn btn-info" value="手入力で登録する" 
					formaction="RecordServlet" formmethod="get"/>
					<%--はりぼて --%>
				</div>
			</div>
		</form>
		<%--<form action="RecordServlet" method="get">--%>
			<div class="col-12 m-3">
			<%--<a href="RecordServlet" class="btn btn-info">手入力で登録する</a>			
			<%--はりぼて --%>
			<%--<input type="submit" class="btn btn-info" value="手入力で登録する" />--%>
		
			</div>
		<%--</form>--%>
	</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
</body>
</html>