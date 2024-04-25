package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.DAOFacade;
import model.entity.BookInfo;

public class ViewLogic {
	private DAOFacade facade = new DAOFacade();
	
	
 public List<BookInfo> selectAll(){
	 List<BookInfo> infolist = new ArrayList<>();
	 if(facade.selectBookInfoAll().size() > 0) {
		 infolist = facade.selectBookInfoAll();
	 }
	 
	return infolist;
	 
 }
 
 public List<BookInfo> selectSome(List<Integer> idlist){
	 List<BookInfo> infolist = new ArrayList<>();
	 if(facade.selectBookInfoList(idlist).size() > 0) {
		 infolist = facade.selectBookInfoList(idlist);
	 }
	return infolist;
	 
 }
 public List<BookInfo> selectOne(Integer id){
	 List<BookInfo> infolist = new ArrayList<>();
	 List<Integer> idSolo = Arrays.asList(id);
	 if(facade.selectBookInfoList(idSolo).size() > 0) {
		 infolist = facade.selectBookInfoList(idSolo);
	 }
	return BookInfo.retributeList(infolist);
	 
 }
}
