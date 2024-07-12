<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="model.entity.* , java.util.List" %>
<%@page import="model.entity.Profession.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--main.jspとsearchresult.jspに共通の閲覧専用リスト--%>
<%--ここから共通--%>
<c:if test="${not empty infolist and infolist.size() > 0}">
<%--<% if(list !=null && list.size() > 0){ %>--%>

	<%-- 件数表示部分作成 --%>
	<%--<c:choose >
		<c:when test="${total <= limit}">
		<p>全<c:out value="${total}"/>件</p>
		</c:when>
		<c:otherwise>
		<%--ページ番号を利用して何件から何件を表示しているのかを表示する --%>
		<%--<p>全<c:out value="${total}"/>件中 
			<c:out value="${(pageNo-1)*limit+1}"/>～
			<c:when test="${pageNo*limit > total}"><c:out value="${total}"/></c:when>
			<c:otherwise><c:out value="${pageNo*limit}"/></c:otherwise>
			件を表示</p>
			
			 <ul class="pager">
			 <%--ページ番号が１より大きかったら前へのリンクを表示 --%>
			 <%--<c:if test="${pageNo > 1}">
			 <li><a href="/ejword/main?searchWord=<%=searchWord %>
			 &mode=<c:out value="${mode}"/>
			 &page=<c:out value="${pageNo - 1}"/>">
			 <span aria-hidden="true">&larr;</span>
			 前へ</a></li>
			 </c:if>
			 <%--件数が全件数に届かないときは次へのリンクを表示 --%>
			 <%--<c:if test="${pageNo*limit < total}">
			 <li><a href="/ejword/main?searchWord=<%=searchWord %>
			 &mode=<c:out value="${mode}"/>
			 &page=<c:out value="${pageNo + 1}"/>">
			 <span aria-hidden="true">&rarr;</span>
			 次へ</a></li>
			 </c:if>
			 </ul>
		</c:otherwise>
	</c:choose>--%>
	
	<%--<% if(total <= limit){ %>
	<p>全<%=total %>件</p>
	
	<%--<%}else{ %>--%>
	
	<%--ページ番号を利用して何件から何件を表示しているのかを表示する --%>
	
	    <%--<p>全<%=total %>件中 <%=(pageNo-1)*limit+1 %>~<%=pageNo*limit > total? total:pageNo*limit %>件を表示</p>--%>
	<%--ページ番号が１より大きかったら前へのリンクを表示 --%>
	    <%--<ul class="pager">
	    <%if(pageNo > 1) {%>
	      <li><a href="/ejword/main?searchWord=<%=searchWord %>&mode=<%=mode %>&page=<%=pageNo-1%>"><span aria-hidden="true">&larr;</span>前へ</a></li>
	 
	    <%} %>
	    <%--件数が全件数に届かないときは次へのリンクを表示 --%>
	    <%--<%if(pageNo*limit < total) {%>
	    <li><a href="/ejword/main?searchWord=<%=searchWord %>&mode=<%=mode %>&page=<%=pageNo+1%>">次へ<span aria-hidden="true">&rarr;</a></li>
	 <%} %>
	    </ul>
	<%--<%} %>--%>
	<%--<table class="table table-bordered table-striped">
	<% for(Word w:list){ %>
	
	<tr><th><%=w.getTitle() %></th><td><%=w.getBody() %></td></tr>
	<%} %>
	</table>--%>
	<%--<%} %>--%>
	<%--<%if(pagination != null){ %>
	 <%=pagination %>
	 <%} %>--%>
	<input type="checkbox" id="checkDetail" onchange="showDetail()">
	<label for="checkDetail"></label>全表示</input>
	<table class="table table-responsive table-striped table-hover">
		<jsp:include page="/WEB-INF/jsp/include/tablehead.jsp"></jsp:include>
		<tbody>
		<c:forEach var="bookinfo" items="${infolist}" varStatus="vs">
			<tr>
			<td scope="row"><input type="checkbox" class="checks" name="bookinfo" 
			value="${bookinfo.book.getId()}" onclick="checkAllorchecks()"></td>
			<%--name属性を配列にすることでcheckboxで複数選択された値をpost出来る --%>
			<td><c:out value="${bookinfo.book.getTitle()}" /></td>
			<td>
			<c:forEach var="author" items="${bookinfo.authors}">
			<c:out value="${author.name}"/>:
			<c:out value="${author.profession.getPFName()}" /><br>
			</c:forEach>
			</td>
			<td><c:out value="${bookinfo.book.getPublishDate()}" /></td>
			<td><c:out value="${bookinfo.book.getPublisher()}" /></td>
			<td><c:out value="${bookinfo.book.getPages()}" /></td>
			<td><c:out value="${bookinfo.book.getISBN()}" /></td>
			<td><c:out value="${bookinfo.book.getNDC()}" /></td>
			<td><c:out value="${bookinfo.book.getPrice()}" /></td>
			<td><c:out value="${bookinfo.book.getRegistationTime().toString()}" /></td>
			<td><c:out value="${bookinfo.book.getStatus().getName()}" /></td>
			<td><c:if test="${bookinfo.book.isFavorite() == true}">
			<c:out value="★"/></c:if></td>
			<%--ボタンを押してもダイアログが表示できない ->解決。bootstrapのバージョンにあったJQueryをCDNで利用すると解決した--%>
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
								<c:out value="${bookinfo.book.getComment()}"></c:out>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-warning"
								 data-dismiss="modal">閉じる</button>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<%--ここまで共通--%>
	</c:if>
	
	<c:if test="${not empty Pagenation}"><c:out value="${Pagenation}"/></c:if>

	<script src="${pageContext.request.contextPath}/JavaScript/checkbox.js"></script>
	<script src="${pageContext.request.contextPath}/JavaScript/handleSubmitbyChecked.js"></script>
	
	
