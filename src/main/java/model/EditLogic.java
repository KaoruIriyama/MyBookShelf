package model;

import java.util.List;

import dao.DAOFacade;

public class EditLogic {
	private DAOFacade facade;
	public EditLogic() {
		this.facade = new DAOFacade();
	}
	
	public EditLogic(String db_url, String db_user, String db_pass) {
		this.facade = new DAOFacade(db_url, db_user, db_pass);
	}
	
	 public int executeUpdate() {
		 int result = 0;
		 
		 return result;
	 }
	 public int executeDelete(List<Integer> idlist, String action) {
		 int result = 0;
		 
		 return result += facade.deleteBookInfoList(idlist, action);
	 }
}
