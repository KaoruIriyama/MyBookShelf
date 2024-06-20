package test;

import model.entity.book.BookISBN;

public class DAOConnectionTest {
 public static void main(String[] args){
//		final String JDBC_URL 
////		= "jdbc:h2:tcp://localhost:8082/~/testShelf";
//		= "jdbc:h2:~/testShelf";
//		final String DB_USER = "sa";
//		final String DB_PASS = "test";
////	 ConnectionManager manager = ConnectionManager.getInstance();
////	 try(Connection conn = manager.getConn()){
////		 System.out.println("manager接続成功");
////	 } catch (SQLException e) {
////		
////		e.printStackTrace();
////	}
//	 DAOFacade facade = new DAOFacade();
//	  try(Connection conn = facade.getManager().getConn()){
//	 System.out.println("DAO接続成功");
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//	 facade.getManager().closeConn();
	
	 String data = "978-4-480-43912-3";
	 BookISBN isbn = new BookISBN(data);
	 System.out.println(isbn);
 	}

}
