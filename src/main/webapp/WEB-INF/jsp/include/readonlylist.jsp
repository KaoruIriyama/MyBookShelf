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
	<table class="table table-striped">
		<thead class="thead-dark">
			<tr>
			<th scope="col">選択<br>
			<input type="checkbox" id="checksAll" name="books"></th>
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
			<td scope="row"><input type="checkbox" class="checks" name="bookinfo" 
			value="${bookinfo}" onclick="checkAllorchecks()"></td>
			<%--name属性を配列にすることでcheckboxで複数選択された値をpost出来る --%>
			<td><c:out value="${bookinfo.book.getTitle()}" /></td>
			<td><c:forEach var="author" items="${bookinfo.authors}">
			<c:out value="${author.name}"/>:
			<c:out value="${author.profession.getPFName()}" /><br>
			</c:forEach></td>
			<td><c:out value="${bookinfo.book.getPublishDate()}" /></td>
			<td><c:out value="${bookinfo.book.getPublisher()}" /></td>
			<td><c:out value="${bookinfo.book.getISBN()}" /></td>
			<td><c:out value="${bookinfo.book.getRegistationTime().toString()}" /></td>
			<td><c:out value="${bookinfo.book.getStatus().getName()}" /></td>
			<td><c:out value="${bookinfo.book.isFavorite()}" /></td>
			<%--<td><a href="DetailServlet?id=${bookinfo.book.getId()}">詳細</a></td>--%>
			<td><a href="DetailServlet?info=${bookinfo}">詳細</a></td>
			<%--新規登録画面ではまだIDがないがどうするか？-> 普通にbookinfoインスタンスをリクエストパラメータにする
			-> 画面遷移はしたがデータが受け継がれていない--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<%--ここまで共通--%>
	</c:if>
	
	<c:if test="${not empty Pagenation}"><c:out value="${Pagenation}"/></c:if>
	
	
	
