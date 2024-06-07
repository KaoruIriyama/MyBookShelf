package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SearchBookLogic;
import model.entity.BookInfo;

/**
 * Servlet implementation class BookSearchServlet
 */
@WebServlet("/BookSearchServlet")
public class BookSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());

		String url = "WEB-INF/jsp/booksearch.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		<%--（3）引数（[query]）の形式
//		リクエストの中で指定できる項目は以下である。
//		表 4-1 OpenSearch 検索項目
//		No. 参照名 内容 一致条件 複数

//		3 any すべての項目を対象に検索 部分一致 ○
//		4 title タイトル 部分一致 ○
//		5 creator 作成者 部分一致 ○
//		6 publisher 出版者 部分一致 ○

//		9 from 開始出版年月日（YYYY、YYYY-MM、YYYY-MM-DD）×
//		10 until 終了出版年月日（YYYY、YYYY-MM、YYYY-MM-DD）×

//		注）項目間の論理条件は全て AND である。
//		注）一つの検索項目内に空白区切り（半角スぺ―ス）でキーワードを複数指定することがで
//		きる。
//		この場合、dpid と mediatype に関しては OR 検索、他は AND 検索となる。dpid のみ
//		指定した検索はできない。
//		注）引数（パラメータ）誤りの場合には検索結果ゼロ件となる。
//		注）from,until に関しては、YYYY、YYYY-MM、あるいは YYYY-MM-DD 形式で指定が
//		可能である。fromとuntilのuntil両方を指定する場合、両方の指定フォーマットは同じ（from
//		が YYYY-MM なら、until も YYYY-MM 形式で指定する。）とする必要がある
//		 --%>
		
		SearchBookLogic sbLogic = new SearchBookLogic();
		List<BookInfo> infolist = new ArrayList<>();
		
			Map<String, String> keyWordMap = new HashMap<>();
			for(String word : sbLogic.querywords) {
				keyWordMap.put(word, request.getParameter(word));
			}
			infolist = sbLogic.execute(keyWordMap);
		
		request.setAttribute("infolist", infolist);
		System.out.println(infolist);
		String url = "WEB-INF/jsp/record.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
}
