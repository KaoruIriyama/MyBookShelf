package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.RecordBookLogic;
import model.entity.BookInfo;
import model.entity.book.Book;

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

		String url = "WEB-INF/jsp/record.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] titlelist = getParameterListOrNull(request, "title");
		String[] authornamelist = getParameterListOrNull(request, "authorname");
		String[] authorproflist = getParameterListOrNull(request, "authorprof");
		String[] publishdatelist = getParameterListOrNull(request, "publishdate");
		String[] publisherlist = getParameterListOrNull(request, "publisher");
		String[] pageslist = getParameterListOrNull(request, "pages");
		String[] isbnlist = getParameterListOrNull(request, "isbn");
		String[] ndclist = getParameterListOrNull(request, "ndc");
		String[] pricelist = getParameterListOrNull(request, "price");
		String[] registationtimelist = getParameterListOrNull(request, "registationtime");
		String[] bookstatuslist = getParameterListOrNull(request, "bookstatus");
		String[] favoritelist = getParameterListOrNull(request, "favorite");
		String[] commentlist = getParameterListOrNull(request, "comment");
		for(int t = 0; t < titlelist.length;t++) {
			Book book = new Book(titlelist[t], publishdatelist[t], publisherlist[t], pageslist[t], 
		isbnlist[t], ndclist[t], pricelist[t], registationtimelist[t], bookstatuslist[t], favoritelist[t], commentlis[t]);
//			Author author = 
		}
		
//		DBへの登録処理
		List<BookInfo> infolist = new ArrayList<>();
		
//		System.out.print(sArray[i] + " ");
		RecordBookLogic record = new RecordBookLogic();
		record.execute(infolist);
		
		String url = "WEB-INF/jsp/main.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
		response.sendRedirect("MainServlet");
	}
	
	String[] getParameterListOrNull(HttpServletRequest request, String keyname) {
		Optional<String[]> opList = Optional.ofNullable(request.getParameterValues(keyname));
		return opList.orElse(new String[0]);
	}
	
	List<Integer> getIDListFromValues(HttpServletRequest request, String keyname) {
		String[] sArray = getParameterListOrNull(request,keyname);
		List<Integer> idList = new ArrayList<>();
		if(sArray.length > 0 ) {
			for(int i = 0; i < sArray.length; i++) {
					if(!sArray[i].equals("on")) {
						idList.add(Integer.parseInt(sArray[i]));
					}
				}
		}
		return idList;
	}

}
