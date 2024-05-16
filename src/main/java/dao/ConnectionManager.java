package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager{
	private Connection conn = null;
//	private static DataSource source = null;
	private static ConnectionManager manager = null;
	public static String JDBC_URL 
	= "jdbc:h2:~/bookShelf";//こちらは接続成功
//  = "jdbc:h2:tcp://localhost/~/bookShelf"; //接続失敗(61行目のエラーメッセージが出る)
	public static String USER = "sa";
	public static String PASS  = "";
	
	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
	}

	private ConnectionManager() {
		
	}
	
	private ConnectionManager(String db_url, String db_user, String db_pass){
		ConnectionManager.JDBC_URL = db_url;
		ConnectionManager.USER = db_user;
		ConnectionManager.PASS = db_pass;
	}
	
	public static ConnectionManager getInstance() {
		if(manager == null) {
			manager = new ConnectionManager();
		}return ConnectionManager.manager;
	}
	
	public static ConnectionManager getInstance(String db_url, String db_user, String db_pass){
		if(manager == null) {
			manager = new ConnectionManager(db_url, db_user, db_pass);
		}return ConnectionManager.manager;
	}
	
	public void closeConn() {
		try {
			if(this.getConn() != null) {
				this.getConn().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Connection getConn(){
		
				try {
					conn = DriverManager.getConnection(JDBC_URL, USER, PASS);//broken
				} catch (SQLException e) {
					System.out.println("ＪＤＢＣドライバによるデータベースへの接続に失敗しました");//
					e.printStackTrace();
				}

		return conn;
		//Connection is broken Connection refused
	}
}

