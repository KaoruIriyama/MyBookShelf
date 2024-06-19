package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//		Integer book_id = Integer.parseInt(request.getParameter("id"));
//		ViewLogic view = new ViewLogic();
//		List<BookInfo> infolist = view.selectOne(book_id);
		
		BookInfo info = (BookInfo)request.getAttribute("info");//null
		request.setAttribute("bookinfo", info);
		String url = "WEB-INF/jsp/detail.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);//java.lang.NumberFormatException: For input string: "book"
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
