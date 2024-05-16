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
	 DAOFacade facade = new DAOFacade();
	  try(Connection conn = facade.getManager().getConn()){
	 System.out.println("DAO接続成功");
	} catch (SQLException e) {
		e.printStackTrace();
	}
	 facade.getManager().closeConn();
	 
//	 DAOFacade test = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
//	 
//	 try{
//		 Connection conn = test.getManager().getConn();
//		 if(conn != null){
//			 System.out.println("TEST_DAO接続成功");
//			 }else {
//			 System.out.println("TEST_DAO接続失敗");
//		 }
//		 Connection conn2 = test.getManager().getConn();
//		 if(conn2 != null){
//			 System.out.println("TEST_DAO接続成功その2");
//			 }else {
//			 System.out.println("TEST_DAO接続失敗その2");
//		 }
//		 conn2.close();
//		 conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	 
//	 DAOFacade test = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
//		List<BookInfo> insert_list = new ArrayList<>();
//		//		書籍も作者も存在しない新規の書籍データ
//		insert_list.add(new BookInfo(
//				new Book("新版 思考の整理学", LocalDate.of(2024, 2, 13),
//						"筑摩書房", 256, "9784480439123", "なし", 630, ""),
//				new Author("外山 滋比古", Profession.Author)));
//		//		testdata.xmlに存在するデータ
//		insert_list.add(new BookInfo(
//				new Book("永遠平和のために", LocalDate.of(1985, 1, 16),
//						"岩波書店", 138, "9784003362594", "134.2", 638, ""),
//				new Author("Immanuel Kant", Profession.Author)));
//		//		testdata.xmlに作者のみが存在するデータ
//		insert_list.add(new BookInfo(
//				new Book("カント「視霊者の夢」", LocalDate.of(2013, 3, 12),
//						"講談社", 173, "9784062921619", "147", 680, "講談社学術文庫 ; 2161 心霊研究 「霊界と哲学の対話」(論創社 1991年刊)の抜粋"),
//				new Author("Immanuel Kant", Profession.Author)));
//		
//		List<Book> booklist = insert_list.stream().map(o -> o.getBook()).toList();
//		List<Author> authorlist = insert_list.stream().map(o -> o.getAuthors().get(0)).toList();
//		for(Book b : booklist) {
//			System.out.println(test.getBkdao().createInsertOneSQL(b));
//		}
//		System.out.println(test.getAthdao().createInsertListSQL(authorlist));
 }
}
