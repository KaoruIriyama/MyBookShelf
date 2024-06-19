package test;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
	 boolean flg1 = isMonth("2019.1.1");//true
	 boolean flg2 = isMonth(null);//false
	 boolean flg3 = isMonth("34.333.1");//false 
	 boolean flg4 = isMonth("819.12.1");//true -> false!!
	 boolean flg5 = isMonth("1444,03,1");//false
	 System.out.println(flg1 + "/" + flg4 + " true");
	 System.out.println(flg2 + "/" + flg3 + "/" + flg5 + " false");
 	}
	 private static boolean isMonth(String month) {
		 DateTimeFormatter fmt = DateTimeFormatter.ofPattern("y.M.d");	
		 boolean flag = false;
		 //y.Mの書式にあった文字列かどうか調べる
		 if(month != null) {
			  try{ //存在しない日(例 2017/02/29)の場合はfalseとする
			 flag = month.equals(fmt.format(fmt.parse(month)));
			 }catch(DateTimeParseException e){
				 e.printStackTrace();
			 }
		 }return flag;
		}

}
