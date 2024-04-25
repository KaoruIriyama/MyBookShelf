<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model.entity.* , java.util.List" %>
<%@page import="model.entity.Profession.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<title>MyBookShelf 蔵書検索結果</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-sm-12">
				<h1>検索結果</h1>
				<h2><c:if test="${not empty Msg}"><c:out value="${Msg }"/></c:if></h2>
			</div>
			<form method="get">
				<%--ここから共通--%>
				<jsp:include page="/WEB-INF/jsp/include/readonlylist.jsp"></jsp:include>
				<%--<table class="table table-striped">
					<thead class="thead-dark">
						<tr>
						<th scope="col">選択<br>
						<input type="checkbox" id="checksAll" name="book"></th>
						<th scope="col">タイトル</th>
						<th scope="col">著者</th>
						<th scope="col">出版日付</th>
						<th scope="col">出版社</th>
						<th scope="col">ISBN</th>
						<th scope="col">登録時刻</th>
						<th scope="col">ステータス</th>
						<th scope="col">お気に入り</th>
						<th scope="col">詳細</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="bookinfo" items="${infolist}" varStatus="vs">
						<tr>
						<td scope="row"><input type="checkbox" class="checks" name="bookid" 
						value="${bookinfo}" onclick="checkAllorchecks()"></td>
						<%--name属性を配列にすることでcheckboxで複数選択された値をpost出来る --%>
						<%--<td><c:out value="${bookinfo.book.getTitle()}" /></td>
						<td><c:forEach var="author" items="${bookinfo.authors}">
						<c:out value="${author.name}"/>:
						<c:out value="${author.profession.getPFName()}" /><br>
						</c:forEach></td>
						<td><c:out value="${bookinfo.book.getPublishDate()}" /></td>
						<td><c:out value="${bookinfo.book.getPublisher()}" /></td>
						<td><c:out value="${bookinfo.book.getISBN()}" /></td>
						<td><c:out value="${bookinfo.book.getRegistationTime()}" /></td>
						<td><c:out value="${bookinfo.book.getStatus().getName()}" /></td>
						<td><c:out value="${bookinfo.book.isFavorite()}" /></td>
						<td><a href="DetailServlet?id=${bookinfo}"></a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>--%>
			<%--ここまで共通--%>
				<div class="col-12 m-3">
					<div class="btn-group" role="group">
					<input type="submit" class="btn btn-info" value="編集" 
					formaction="EditServlet"/>
					<input type="button" class="btn btn-info" value="再検索" 
					formaction="ListSearchServlet"/>
					</div>
				</div>
			</form>	
		</div>
	</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
</body>
</html>