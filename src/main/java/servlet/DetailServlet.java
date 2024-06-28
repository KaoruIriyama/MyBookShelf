package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ViewLogic;
import model.entity.BookInfo;

/**
 * Servlet implementation class DetailServlet
 */
@WebServlet("/DetailServlet")
public class DetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strid = request.getParameter("id");
		Integer book_id = (strid == null? 0 : Integer.parseInt(strid));
		ViewLogic view = new ViewLogic();
		List<BookInfo> infolist = view.selectOne(book_id);
		
//		BookInfo info = (BookInfo)request.getAttribute("info");//null
//		request.setAttribute("bookinfo", info);
		String url = "WEB-INF/jsp/detail.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
//		画面遷移はするしリクエストパラメータはちゃんとついていた(id=数字)がデータが画面に表示されない
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = null;
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
