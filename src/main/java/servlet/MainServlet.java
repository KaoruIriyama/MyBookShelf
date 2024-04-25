package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = null;
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		ViewLogic view = new ViewLogic();
//		List<BookInfo> infolist = view.selectAll();
//		if(infolist.size()> 0) {
//			request.setAttribute("infolist", infolist);
	//		if(total > LIMIT){
	//        //ページ数
	//        int pageCount=total%LIMIT == 0 ? total/LIMIT : total/LIMIT +1;
	//        String link="";
	//        StringBuilder sb=new StringBuilder();
	//        sb.append("<div class='paginationBox'>\n");
	//        sb.append("<ul class='pagination'>\n");
	//        //ページ数が20ページで収まるか？
	//        if(pageCount<20){
	//          for(int i=1;i<=pageCount;i++){
	//            link="/ejword/main?searchWord="+searchWord+"&mode="+mode+"&page="+i;
	//            sb.append("<li class='"+(pageNo==i? "active":"") +"'><a href='"+link+"'>"+i+"</a></li>\n");
	//          }
	//        }else{
	//          //大量にページがある場合先頭へのリンクを追加する
	//          link="/ejword/main?searchWord="+searchWord+"&mode="+mode+"&page="+1;
	//          sb.append("<li class='"+(pageNo==1? "disabled":"") +"'><a href='"+link+"'>&laquo;</a></li>\n");
	//          //現在ページから前後５件を表示
	//          for(int i=pageNo-5;i<=pageNo+5;i++){
	//            if(i<1 || i>pageCount){continue;}
	//            link="/ejword/main?searchWord="+searchWord+"&mode="+mode+"&page="+i;
	//            sb.append("<li class='"+(pageNo==i? "active":"") +"'><a href='"+link+"'>"+i+"</a></li>\n");
	//          }
	//          //最後へのリンクを追加する
	//          link="/ejword/main?searchWord="+searchWord+"&mode="+mode+"&page="+pageCount;
	//          sb.append("<li class='"+(pageNo==total/LIMIT+1? "disabled":"") +"'><a href='"+link+"'>&raquo;</a></li>\n");     
	//        } 
	//        sb.append("</ul>\n");
	//        sb.append("</div>\n");
	//        request.setAttribute("Pagenation", sb.toString());  
	//      } 

//		}else {
//			msg = "目録取得に失敗しました。";
//		}
//		
//		request.setAttribute("Msg", msg);
		String url = "WEB-INF/jsp/main.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String[] infoArray = request.getParameterValues("");
		String url = null;
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

}
