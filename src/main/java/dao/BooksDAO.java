package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.entity.Book;
import model.entity.BookStatus;
import model.entity.DTO;

class BooksDAO extends DAOTemplate{

	private ConnectionManager manager;
	private String sql;
//	private ResultSet rs;
//	private PreparedStatement pstmt;
	private Connection conn;
	
	BooksDAO(){
		this.manager = ConnectionManager.getInstance();
		this.conn = this.manager.getConn();
	}
	
	BooksDAO(ConnectionManager manager) {
		this.manager = manager;
		this.conn = this.manager.getConn();
	}
	
	BooksDAO(Connection conn) {
		this.conn = conn;
	}
//
//	BooksDAO(DataSource source) {
//		BooksDAO.source = source;
//	}

	int insertBook(Book book) throws SQLException {
		int result = 0;
		if (book.isNotEmpty()) {
			//			try{
			//Merge Into文(対象テーブルに登録したいデータが存在しないときのみ登録)
			sql = "MERGE INTO BOOKS USING"
					+ "(VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)) SOURCE "
					+ "(TITLE, PUBLISH_DATE, PUBLISHER,　"
					+ "PAGES, ISBN, NDC, "
					+ "PRICE, REGISTATION_TIME, COMMENT) "
					+ "ON ((SOURCE .ISBN = BOOKS .ISBN) AND "
					+ "(SOURCE .PUBLISH_DATE = BOOKS .PUBLISH_DATE)) "
					+ "WHEN NOT MATCHED BY BOOKS THEN "
					+ "INSERT VALUES "
					+ "(TITLE, PUBLISH_DATE, PUBLISHER, "
					+ "PAGES, ISBN, NDC, "
					+ "PRICE, REGISTATION_TIME, COMMENT)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, book.getTitle());
			pstmt.setDate(2, Date.valueOf(book.getPublishDate()));//LocalDate -> Date
			pstmt.setString(3, book.getPublisher());
			pstmt.setInt(4, book.getPages());
			pstmt.setString(5, book.getISBN());
			pstmt.setString(6, book.getNDC());
			pstmt.setInt(7, book.getPrice());
			pstmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));//ここが働いていない？
			pstmt.setString(9, book.getComment());
			//LocalDateTime -> TimeStamp
			//			pstmt.setString(3, person.getGender().getName());
			//定数名でなく変数名を入れる
			result = pstmt.executeUpdate();
			System.out.println("BOOKS" + sql + " "+ result);
//			this.closeConnection(pstmt, rs);
			this.conn = null;
		}
		return result;

		//			}catch(SQLException e) {
		//				e.printStackTrace();
		//				System.out.println("recordBook()の実行中に例外が発生しました。");
		//				return 0;
		//			}
		//			finally {
		//				this.closeConnection(pstmt, rs);
		//			}

	}

	List<Book> selectBookbyKeyword(String keyword) {
		if (keyword == null) {
			return null;
		} else {
			List<Book> blist = new ArrayList<>();
			try {

				//		    		sql = "SELECT * FROM BOOKS WHERE NAME = ?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				pstmt.setString(1, keyword);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					try {
						Book book = getInstancefromResultSet(rs);

						blist.add(book);
					} catch (NullPointerException e) {
						e.printStackTrace();
						System.out.println("Bookの取得に失敗しました。");
						return null;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("selectBookbyKeyword()の実行中に例外が発生しました。");
				return null;
			} finally {
//				this.closeConnection(pstmt, rs);
				this.conn = null;
			}
			return blist;
		}
	}

	boolean updateBookListData(List<Book> blist) throws SQLException {
		int result = 0;
		if (blist.stream().anyMatch(b -> b.isNotEmpty())) {
			{
				//				try {
				//				Merge文バージョン
				//					List<String> values = new ArrayList<>();
				//		    	for(Book b : blist){ 
				//		    		
				//		    			StringBuilder sb = new StringBuilder();
				//			    		sb.append(b.getTitle()).append(",").
				//			    		append(b.getPublishDate().toString()).append(",").//LocalDate
				//			    		append(b.getPublisher()).append(",").
				//			    		append(b.getPages()).append(",").//int
				//			    		append(b.getNDC()).append(",").
				//			    		append(b.getPrice()).append(",").//int
				//			    		append(b.getRegistationTime()).append(",").//LocalDateTime
				//			    		append(b.getComment()).append(",").
				//			    		append(b.getStatus().toString()).append(",").
				//			    		append(b.isFavorite());//boolean
				//			    		String value = "( "+ sb.toString() + " )";
				//			    		values.add(value);
				//		    		
				//		    	}String valholder = convertListtoPlaceholder(values);

				//					//Merge Into文(対象テーブルに登録したいデータが存在しないときのみ登録)
				//			sql = "MERGE INTO BOOKS USING"
				//					+ "(VALUES" + valholder + ") SOURCE "
				//					+ "(ID, TITLE, PUBLISH_DATE, PUBLISHER, "
				//					+ "PAGES, ISBN, NDC, PRICE, REGISTATION_TIME, COMMENT, STATUS, FAVORITE) "
				//					+ "ON ((SOURCE .ID = BOOKS .ID) "
				//					+ "AND (SOURCE .ISBN = BOOKS .ISBN)) "
				//					+ "WHEN MATCHED THEN "
				//					+ "UPDATE VALUES "
				//					+ "(ID, TITLE, PUBLISH_DATE, PUBLISHER, "
				//					+ "PAGES, ISBN, NDC, PRICE, REGISTATION_TIME, COMMENT, STATUS, FAVORITE)";

				//case演算子を使ったバージョン

				//				StringBuilder sbid = new StringBuilder();
				//				StringBuilder sbtitle = new StringBuilder();
				//				StringBuilder sbpub_date = new StringBuilder();
				//				StringBuilder sbpublisher = new StringBuilder();
				//				StringBuilder sbid = new StringBuilder();
				//				StringBuilder sbid = new StringBuilder();
				//				StringBuilder sbid = new StringBuilder();
				//				
				//				StringBuilder sbid = new StringBuilder();
				//				StringBuilder sbid = new StringBuilder();
				//				StringBuilder sbid = new StringBuilder();
				//				StringBuilder sbid = new StringBuilder();
				//	    		for(Book b : blist) {
				//	    			sbid.append(b.getId()).append(",");	
				//	    			sbtitle.append("WHEN ").append(b.getId()).append(" THEN '").append(p.getName()).append("' ");
				//	    			sbage.append("WHEN ").append(b.getId()).append(" THEN ").append(p.getAge()).append(" ");
				//	    			sbgender.append("WHEN ").append(b.getId()).append(" THEN '").append(p.getGender()).append("' ");
				//	    		}	
				//	    		
				//	    		String idHolder = sbid.deleteCharAt( sbid.length() -1 ).toString();
				//	    		
				//	    		//これでsbの末尾にある","を消すことが出来る
				//	    		
				//	    		String nameHolder = "CASE ID " + sbname.toString() + "END, ";
				//	    		String ageHolder = "CASE ID " + sbage.toString() + "END, ";
				//	    		String genderHolder = "CASE ID " + sbgender.toString() + "END ";
				//	    		
				//				sql = "UPDATE BOOKS SET "
				//							+ "NAME = " + nameHolder
				//							+ "AGE = " + ageHolder
				//							+ "GENDER = " + genderHolder
				//							+ "WHERE ID IN (" + idHolder + ")";
				//		    
				//		    sql = "UPDATE BOOKS SET "
				//		    		
				//		    		+ "WHERE ID IN (" + idHolder + ")";
				//				
				System.out.println(sql);//テスト用
				//				
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
				//					
				result = pstmt.executeUpdate();
				//					return (result == blist.size());
//				this.closeConnection(pstmt, rs);
				this.conn = null;			
				//				}catch(SQLException e) {
				//					e.printStackTrace();
				//					System.out.println("updatePersonList()の実行中に例外が発生しました。");
				//					return false;
				//				}
				//			finally {
				//					this.closeConnection(pstmt, rs);
				//				}
			}
		}
		return (result == blist.size());
	}

	@Override
	Book getInstancefromResultSet(ResultSet rs) throws SQLException {
		int id = 0;
		if(rs.getInt("ID") != 0) {id = rs.getInt("ID");}
		else if(rs.getInt("BOOK_ID") != 0) {id = rs.getInt("BOOK_ID");}
		String title = rs.getString("TITLE");
		LocalDate pubdate = rs.getDate("PUBLISH_DATE").toLocalDate();
		//DateからLocalDateへの変換
		String publisher = rs.getString("PUBLISHER");
		int pages = rs.getInt("PAGES");
		String isbn = rs.getString("ISBN");
		String ndc = rs.getString("NDC");
		int price = rs.getInt("PRICE");
		LocalDateTime registime = rs.getTimestamp("REGISTATION_TIME").toLocalDateTime();
		//TimestampからLocalDateTimeへの変換
		String comment = rs.getString("COMMENT");
		BookStatus status = BookStatus.getByName(rs.getString("STATUS"));
		//					valueOf() String から enum への変換
		//					列挙型の定数からフィールド変数を呼び出す
		boolean favorite = rs.getBoolean("FAVORITE");
		Book book = new Book(id, title, pubdate, publisher, pages, isbn, ndc, price, registime, comment, status,
				favorite);
		return book;
	}

	@Override
	protected String createSelectAllSQL() {
		String sql = "SELECT * FROM BOOKS";
		return sql;
	}

	
	@Override
	protected String createSelectListSQL(List<Integer> idlist) {
		String placeholder = convertListtoPlaceholder(idlist);

		String sql = "SELECT * FROM BOOKS WHERE ID IN " + placeholder;
		return sql;
	}

	@Override
	protected String createInsertOneSQL(DTO dto) {
		String sql = "MERGE INTO BOOKS USING"
				+ "(VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)) SOURCE "
				+ "(TITLE, PUBLISH_DATE, PUBLISHER,　"
				+ "PAGES, ISBN, NDC, "
				+ "PRICE, REGISTATION_TIME, COMMENT) "
				+ "ON ((SOURCE .ISBN = BOOKS .ISBN) AND "
				+ "(SOURCE .PUBLISH_DATE = BOOKS .PUBLISH_DATE)) "
				+ "WHEN NOT MATCHED BY BOOKS THEN "
				+ "INSERT VALUES "
				+ "(TITLE, PUBLISH_DATE, PUBLISHER, "
				+ "PAGES, ISBN, NDC, "
				+ "PRICE, REGISTATION_TIME, COMMENT)";
		return sql;
	}

	@Override
	protected String createInsertListSQL(List<DTO> list) {
		
		return null;
	}

	@Override
	protected String createUpdateListSQL(List<DTO> list) {
	
		return null;
	}

	@Override
	protected String createDeleteListSQL(List<Integer> idList, String action) {
		String placeHolder = convertListtoPlaceholder(idList);
		//actionはこのクラスでは使わない
		String sql = "DELETE FROM BOOKS WHERE ID IN " + placeHolder ;
		return sql;
	}
}
