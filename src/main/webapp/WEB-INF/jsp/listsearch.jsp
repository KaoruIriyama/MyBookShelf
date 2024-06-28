<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<title>MyBookShelf 蔵書検索</title>
</head>
<body class="d-flex flex-column vh-100">
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
<main class="mb-auto">
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-sm-12">
			<h1>蔵書検索</h1>
			</div>
		</div>
	<%--ここで空欄のまま送信ボタンを押すと警告が出るようにしたい --%>
		<form>
			<div class="form-group">
				<p>ISBNで検索</p>
				<input type="text" class="form-control" name="isbn"
				placeholder="ISBN(例 978-4-295-01596-3)" />
			</div>
			<div class="form-group">
				<p>キーワード検索</p>
				キーワード<input type="text" class="form-control" name="any" /><br>
				タイトル<input type="text" class="form-control" name="title" /><br>
				著者<input type="search" class="form-control" name="creator" /><br>
				出版社<input type="search" class="form-control" name="publisher" /><br>
				出版日<input type="date" class="form-control" name="from"  />
				～<input type="date" class="form-control" name="until"  /><br>
			</div>
			<div class="col-12 m-3">
				<div class="btn-group" role="group"><%--このボタン二つが要素として認識されていない？ --%>
					<input type="submit" class="btn btn-info" value="検索" 
					formaction="ListSearchServlet" formmethod="post"/><%-- はりぼて --%>
					<input type="reset" class="btn btn-info" value="リセット"/><%--はりぼて --%>
				</div>
			</div>
		</form>
	</div>
</main>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
</body>
</html>