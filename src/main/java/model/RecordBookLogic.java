package model;

import java.util.List;

import dao.DAOFacade;
import model.entity.BookInfo;

public class RecordBookLogic {
private DAOFacade facade = new DAOFacade();
	
	public int execute(List<BookInfo> infolist) {
		int bookresult = 0;
		
		boolean flag = facade.insertBookAuthor(infolist);
		if(flag == true) {
			bookresult = facade.getNewbook();
		}
		return bookresult;
	}
}
