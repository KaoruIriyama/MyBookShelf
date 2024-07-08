<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="model.entity.* , java.util.List" %>
<%@page import="model.entity.Profession.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%--editlist.jspとrecord.jspに共通の編集可能リスト--%>
<%--ここから共通--%>
	<input type="checkbox" id="checkDetail" onchange="showDetail()">
	<%--showDetail()が機能していない --%>
	<label for="checkDetail"></label>全表示</input>
	<table class="table table-responsive table-striped table-hover">
		<jsp:include page="/WEB-INF/jsp/include/tablehead.jsp"></jsp:include>
		<tbody>
		<c:forEach var="bookinfo" items="${infolist}" varStatus="vs">
			<tr>
			<td scope="row">
			<input type="checkbox" class="checks" name="bookinfo" 
				value="${bookinfo.book.getId()}" onclick="checkAllorchecks()"></td>
			<%--name属性を配列にすることでcheckboxで複数選択された値をpost出来る --%>
			<td><input type="text" class="form-control" name="title" placeholder="タイトル"
				value="<c:out value="${bookinfo.book.getTitle()}" />" required/></td>
			<td>
			<c:forEach var="author" items="${bookinfo.authors}">
		氏名<input type="search" class="form-control" name="authorname" placeholder="著者名"
				value="<c:out value="${author.name.getValue()}"/>"/>
			<c:choose>
				<c:when test="${author.profession.ordinal() == 0}">
				<c:set var="author_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 1}">
				<c:set var="translater_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 2}">
				<c:set var="editer_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 3}">
				<c:set var="writer_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 4}">
				<c:set var="storyteller_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 5}">
				<c:set var="artist_checked">checked</c:set></c:when>
				<c:when test="${author.profession.ordinal() == 6}">
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
			</select><br>
			</c:forEach>
			</td>
			<td><input type="date" class="form-control" name="publishdate" placeholder=""
				value="<c:out value="${bookinfo.book.getPublishDate()}" />" required></td>
			<td><input type="search" class="form-control" name="publisher" placeholder="出版者"
				value="<c:out value="${bookinfo.book.getPublisher()}" />" required></td>
			<td><input type="number" class="form-control" name="pages" placeholder=""
				value="<c:out value="${bookinfo.book.getPages()}" />" required></td>
			<td><input type="search" class="form-control" name="isbn" placeholder="ISBN(ハイフンなし)"
				value="<c:out value="${bookinfo.book.getISBN()}" />" required></td>
			<td><input type="text" class="form-control" name="ndc" placeholder="日本十進法分類"
				value="<c:out value="${bookinfo.book.getNDC()}" />"></td>
			<td><input type="number" class="form-control" name="price" 
				value="<c:out value="${bookinfo.book.getPrice()}"/>" required></td>
			<td><input type="text" class="form-control" name="registationtime" placeholder=""
				value="<c:out value="${bookinfo.book.getRegistationTime()}" />" required></td>
			<td>
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
			</td>
			<td>
			<c:if test="${bookinfo.book.isFavorite() == true}">
				<c:set var="favorite_checked">true</c:set>
			</c:if>
			<input type="checkbox" class="form-control" name="favorite" 
				value="<c:out value="${favorite_checked}" />" required/>
			</td>
			<%--ボタンを押してもダイアログが表示できない --%>
			<td>
			<c:if test="${bookinfo.book.getComment().length() >= 3}">
				<button type="button" class="btn btn-success"  
				data-toggle="modal" data-target="#comment-modal">コメント</button>
				<div class="modal fade" id="comment-modal">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">コメント詳細</h5>
							</div>
							<div class="modal-body">
								<textarea  class="form-control" name="comment">
								<c:out value="${bookinfo.book.getComment()}"></c:out></textarea>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-warning"
								 data-dismiss="modal">閉じる</button>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<%--<textarea  class="form-control" name="comment">
				<c:out value="${bookinfo.book.getComment()}"></c:out></textarea>--%>
			</td>
			<%--<td><a href="DetailServlet?id=${bookinfo.book.getId()}">詳細</a></td>--%>
			<%--<td><a href="DetailServlet?info=${bookinfo}">詳細</a></td>--%>
			<%--新規登録画面ではまだIDがないがどうするか？-> 普通にbookinfoインスタンスをリクエストパラメータにする
			-> 画面遷移はしたがデータが受け継がれていない--%>
			<%--普通にリスト画面に全項目をのせて、デフォルトでは一部項目だけ表示されるようにする
			表示・非表示はボタンで切り替える --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<script src="${pageContext.request.contextPath}/JavaScript/checkbox.js"></script>
	<script src="${pageContext.request.contextPath}/JavaScript/handleSubmitbyChecked.js"></script>
