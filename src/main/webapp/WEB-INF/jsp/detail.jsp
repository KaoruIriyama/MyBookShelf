<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="model.entity.* , java.util.List" %>
<%@page import="model.entity.Profession.*"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/include/bootstrap.jsp"></jsp:include>
<title>MyBookShelf 書籍詳細</title>
</head>
<body><%--UIの改善をすること --%>
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-sm-12">
			<h1>書籍詳細</h1>
			</div>
		</div>
	
	<p><c:if test="${not empty Msg}"><c:out value="${Msg }"/></c:if></p>
		<form method="post">
		<input type="hidden" class="form-control" name="bookinfo" 
		value="${bookinfo}">
		タイトル<input type="text" class="form-control" name="title" 
					value="<c:out value="${bookinfo.book.getTitle()}"/>" required><br>
		著者情報<c:forEach var="author" items="${bookinfo.authors}">
			氏名<input type="search" class="form-control" name="authorname" 
		value="<c:out value="${author.name}"/>"/>
			<%--プルダウンメニュー --%>
			<c:choose>
				<c:when test="${author.profession.ordinal() == 0}">
				<c:set var="author_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 1}">
				<c:set var="translater_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 2}">
				<c:set var="editer_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 2}">
				<c:set var="writer_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 2}">
				<c:set var="storyteller_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 2}">
				<c:set var="artist_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 2}">
				<c:set var="other_checked">checked</c:set></c:when>
			</c:choose>
			:<select name="authorprof" class="form-control" required>
				<option value="Author" <c:out value="${author_checked}" />>
				著者</option>
				<option value="Translater" <c:out value="${translater_checked}" />>
				訳者</option>
				<option value="Editer" <c:out value="${editer_checked}" />>
				編者</option>
				<option value="Writer" <c:out value="${writer_checked}" />>
				筆記者</option>
				<option value="StoryTeller" <c:out value="${storyteller_checked}" />>
				原作者</option>
				<option value="Artist" <c:out value="${artist_checked}" />>
				作画者</option>
				<option value="Other" <c:out value="${other_checked}" />>
				その他</option>
			</select>
		</c:forEach><br>
		
		出版日<input type="date" class="form-control" name="publishdate" 
		value="<c:out value="${bookinfo.book.getPublishDate()}" />" required><br>
		出版社<input type="search" class="form-control" name="publisher" 
		value="<c:out value="${bookinfo.book.getPublisher()}" />" required><br>
		ページ数<input type="search" class="form-control" name="pages" 
		value="<c:out value="${bookinfo.book.getISBN()}" />" required><br>
		ISBN<input type="text" class="form-control" name="isbn" 
		value="<c:out value="${bookinfo.book.getRegistationTime()}" />" required><br>
		NDC<input type="text" class="form-control" name="ndc" 
		value="<c:out value="${bookinfo.book.getStatus().getName()}" />" required><br>
		価格<input type="number" class="form-control" name="price" 
		value="<c:out value="${bookinfo.price}"/>" required><br>
		登録時刻<input type="datetime" class="form-control" name="registationtime" 
		value="<c:out value="${bookinfo.book.getRegistationTime()}"/>" required><br>
		<%--プルダウンメニュー --%>
		ステータス
		<c:choose>
			<c:when test="${bookinfo.book.getStatus().ordinal() == 0}">
			<c:set var="unread_checked">checked</c:set></c:when>
			<c:when test="${bookinfo.book.getStatus().ordinal() == 1}">
			<c:set var="finished_checked">checked</c:set></c:when>
			<c:when test="${bookinfo.book.getStatus().ordinal() == 2}">
			<c:set var="reading_checked">checked</c:set></c:when>
		</c:choose>	
		<select name="bookstatus" class="form-control" required>
			<option value="Unread" <c:out value="${unread_checked}" />>未読</option>
			<option value="Finished" <c:out value="${finished_checked}" />>既読</option>
			<option value="Reading" <c:out value="${reading_checked}" />>途中</option>
		</select>
		<c:if test="${bookinfo.book.isFavorite() == true}">
		<c:set var="favorite">checked</c:set>
		</c:if>
		お気に入り<input type="checkbox" class="form-control" name="favorite" 
		value="<c:out value="${favorite}" />" /><br>
		コメント<textarea  class="form-control" name="comment" required>
		<c:out value="${bookinfo.comment}"></c:out></textarea>
		
			<div class="col-12 m-3">
				<div class="btn-group" role="group">
					<input type="reset" class="btn btn-info" value="リセット">
					<input type="submit" class="btn btn-info" id = "return" value="更新して戻る" 
					formaction="EditServlet"/>
				</div>
			</div>
		</form>
	</div>
</body>
</html>