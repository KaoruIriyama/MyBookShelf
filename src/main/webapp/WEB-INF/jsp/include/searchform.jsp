<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%--booksearch.jspとlistsearch.jspに共通の閲覧専用リスト--%>
<%--ISBN検索の時はISBNのみrequiredが、キーワード検索の時はキーワードのどれか一つにでも
		値が入力されていたらオーケーにしたい --%>
		<%--ここから共通 --%>
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
			<%--（3）引数（[query]）の形式
			リクエストの中で指定できる項目は以下である。
			表 4-1 OpenSearch 検索項目
			No. 参照名 内容 一致条件 複数
			1 dpid データプロバイダID、データグループID 完全一致 ○
			2 dpgroupid データプロバイダグループID 完全一致 ×
			3 any すべての項目を対象に検索 部分一致 ○
			4 title タイトル 部分一致 ○
			5 creator 作成者 部分一致 ○
			6 publisher 出版者 部分一致 ○
			7 digitized_publisher デジタル化した製作者 部分一致 ○
			8 ndc 分類（NDC） 前方一致 ×
			9 from 開始出版年月日（YYYY、YYYY-MM、YYYY-MM-DD）×
			10 until 終了出版年月日（YYYY、YYYY-MM、YYYY-MM-DD）×
			11 cnt 出力レコード上限値（省略時は200 とする） ×
			12 idx レコード取得開始位置（省略時は1 とする） ×
			13 isbn ISBN 10 桁または13 桁->10 桁、13 桁の両方に変換して完全一致検索
			それ以外の桁->前方一致検索
			完全一致または前方一致 ×
			14 mediatype mediaType 一覧参照 完全一致 ○
			注）項目間の論理条件は全て AND である。
			注）一つの検索項目内に空白区切り（半角スぺ―ス）でキーワードを複数指定することがで
			きる。
			この場合、dpid と mediatype に関しては OR 検索、他は AND 検索となる。dpid のみ
			指定した検索はできない。
			注）引数（パラメータ）誤りの場合には検索結果ゼロ件となる。
			注）from,until に関しては、YYYY、YYYY-MM、あるいは YYYY-MM-DD 形式で指定が
			可能である。fromとuntilの両方を指定する場合、両方の指定フォーマットは同じ（from
			が YYYY-MM なら、until も YYYY-MM 形式で指定する。）とする必要がある
			 --%>
			
		<%--ここまで共通 --%>
