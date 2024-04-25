package test;

import java.sql.Connection;
import java.sql.SQLException;

import dao.DAOFacade;

public class DAOConnectionTest {
 public static void main(String[] args){
		final String JDBC_URL 
//		= "jdbc:h2:tcp://localhost:8082/~/testShelf";
		= "jdbc:h2:~/testShelf";
		final String DB_USER = "sa";
		final String DB_PASS = "test";
//	 ConnectionManager manager = ConnectionManager.getInstance();
//	 try(Connection conn = manager.getConn()){
//		 System.out.println("manager接続成功");
//	 } catch (SQLException e) {
//		
//		e.printStackTrace();
//	}
//	 DAOFacade facade = new DAOFacade();
//	  try(Connection conn = facade.getManager().getConn()){
//	 System.out.println("DAO接続成功");
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//	 facade.getManager().closeConn();
	 
	 DAOFacade test = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
	 
	 try{
		 Connection conn = test.getManager().getConn();
		 if(conn != null){
			 System.out.println("TEST_DAO接続成功");
			 }else {
			 System.out.println("TEST_DAO接続失敗");
		 }
		 Connection conn2 = test.getManager().getConn();
		 if(conn2 != null){
			 System.out.println("TEST_DAO接続成功その2");
			 }else {
			 System.out.println("TEST_DAO接続失敗その2");
		 }
		 conn2.close();
		 conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
 }
}
