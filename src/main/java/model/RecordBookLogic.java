package model;

import java.util.List;

import dao.DAOFacade;
import model.entity.BookInfo;

public class RecordBookLogic {
	private DAOFacade facade;
	public RecordBookLogic() {
		this.facade = new DAOFacade();
	}
	
	public RecordBookLogic(String db_url, String db_user, String db_pass) {
		this.facade = new DAOFacade(db_url, db_user, db_pass);
	}
	
	public int execute(List<BookInfo> infolist) {
		int bookresult = 0;
		
		boolean flag = facade.insertBookInfo(infolist);
		if(flag == true) {
			bookresult = facade.getNewbook();
		}
		return bookresult;
	}
}
