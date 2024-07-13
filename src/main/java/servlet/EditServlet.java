package servlet;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] sArray = request.getParameterValues("bookinfo");
		
		List<Integer> idList = new ArrayList<>();
		for(int i = 0; i < sArray.length; i++) {
			System.out.print(sArray[i] + " ");
			idList.add(Integer.parseInt(sArray[i]));
		}
		
		ViewLogic view  = new ViewLogic();
		List<BookInfo> infolist = view.selectSome(idList);
		System.out.println(infolist);//debug
		request.setAttribute("infoList", infolist);
		
		String url = "WEB-INF/jsp/editlist.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
//		画面遷移はしたが選択した書籍データが表示されていない！！
//		sArray内のidは正しいものが格納されている(5 6 7)
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//actionの値によって更新と削除の処理を分岐
		//actionの値が取得できていない
		//formactionについて調べる
	
		String url = null;
	
			//更新処理の後、直前のページに戻る
			url = "WEB-INF/jsp/main.jsp";
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
