<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

	<%--デフォルトが非表示状態になっていない --%>
		<colgroup><%----%>
		<col><col><%--選択--%>
		<col><col><%--タイトル--%>
		<col><col><%--著者--%>
		<col><col><%--出版日--%>
		<col><col><%--出版社--%>
		<col id="detail" class="d-none"><col><%--ページ数 デフォルトでは非表示--%>
		<col><col><%--ISBN--%>
		<col id="detail" class="d-none"><col><%--NDC デフォルトでは非表示--%>
		<col id="detail" class="d-none"><col><%--価格 デフォルトでは非表示--%>
		<col><col><%--登録時刻--%>
		<col><col><%--ステータス--%>
		<col id="detail" class="d-none"><col><%--お気に入り デフォルトでは非表示--%>
		<col><col><%--コメント--%>
		</colgroup>
		<thead class="thead-dark">
			<tr>
			<th scope="col">選択<br>
			<input type="checkbox" id="checksAll" name="books"></th>
			<th scope="col">タイトル</th>
			<th scope="col">著者</th>
			<th scope="col">出版日</th>
			<th scope="col">出版社</th>
			<th scope="col">ページ数</th>
			<th scope="col">ISBN</th>
			<th scope="col">NDC</th>
			<th scope="col">価格</th>
			<th scope="col">登録時刻</th>
			<th scope="col">ステータス</th>
			<th scope="col">お気に入り</th>
			<th scope="col">コメント</th>
			<%--<th scope="col">詳細</th>--%>
			</tr>
		</thead>

	<script src="${pageContext.request.contextPath}/JavaScript/showDetail.js"></script>
	