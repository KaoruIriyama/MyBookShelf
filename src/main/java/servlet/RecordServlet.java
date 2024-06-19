package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecordServlet
 */
@WebServlet("/RecordServlet")
public class RecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		response.getWriter().append("Served at: ").append(request.getContextPath());

		String url = "WEB-INF/jsp/record.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//DBへの登録処理
//		List<BookInfo> infolist 
//			= new ArrayList<>();
//		RecordBookLogic record = new RecordBookLogic();
//		record.execute(infolist);
		
//		String url = "WEB-INF/jsp/main.jsp";
//		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
//		dispatcher.forward(request, response);
		response.sendRedirect("MainServlet");
	}

}
