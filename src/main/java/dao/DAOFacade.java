package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.entity.Author;
import model.entity.Book;
import model.entity.BookInfo;
import model.entity.BookStatus;
import model.entity.DTO;
import model.entity.Profession;

public class DAOFacade extends DAOTemplate{
	private ConnectionManager manager = null;
	private String sql;

	
	private BooksDAO bkdao;
	private AuthorsDAO athdao;
	private TaggingDAO tagdao;
	private int newbook = 0;
	private int newauthor = 0;

	public DAOFacade() {
		this.setManager(ConnectionManager.getInstance());
		}
	//てすと用
	public DAOFacade(String db_url, String db_user, String db_pass){
		this.setManager(ConnectionManager.getInstance(db_url, db_user, db_pass));
	}
	
	public ConnectionManager getManager() {	return this.manager;}
	
	public void setManager(ConnectionManager manager) {this.manager = manager;}
	
	public int getNewbook() {return newbook;}

	public void setNewbook(int newbook) {this.newbook = newbook;}

	public int getNewauthor() {	return newauthor;}

	public void setNewauthor(int newauthor) {this.newauthor = newauthor;}

	public boolean insertBookAuthor(List<BookInfo> data) {
		this.setNewbook(0);
		this.setNewauthor(0);
		int newtag = 0;
		boolean flg = false;

		try {
			Connection conn = this.getManager().getConn();
			conn.setAutoCommit(false);
			bkdao = new BooksDAO();
			athdao = new AuthorsDAO();
			tagdao = new TaggingDAO();
			for (BookInfo b : data) {

				if (bkdao.insertBook(b.getBook()) == 1) {
					setNewbook(this.getNewbook() + bkdao.insertBook(b.getBook()));
					System.out.println("Newbook:" + this.getNewbook() + "NewAuthor:" + this.getNewauthor() 
					+ "NewTag:" + newtag);
					try {
						setNewauthor(this.getNewauthor() + athdao.insertAuthorList(b.getAuthors()));
						System.out.println("Newbook:" + this.getNewbook() + "NewAuthor:" + this.getNewauthor() 
						+ "NewTag:" + newtag);
					} catch (SQLException e) {
						e.printStackTrace();
						conn.rollback();
					}
				} else {
					conn.rollback();
				}
				//				TAGGINGテーブルへの登録用に<Book>一対一＜Author＞対応のリストへ変換
				List<BookInfo> mappeddata = BookInfo.retributeList(data);
				for (BookInfo bi : mappeddata) {
					if (tagdao.insertTaggingfromBookInfo(bi) == 1) {
						//本一冊ごとにTAGGINGへの登録を行う(内部では著者名の数だけ登録をする)
						//						全ての著者が登録できたとき、insertメソッドの返り値は1になるので、
						//						それが確認出来たら本一冊の登録が終わったと判断し新規タグの登録完了カウントを
						//						1増やす
						newtag++;
					} else {
						conn.rollback();
					}
				}
				if (newtag == this.getNewbook()) {
					conn.commit();
					flg = true;
				} else {
					conn.rollback();
					flg = false;
				}
				System.out.println("Newbook:" + this.getNewbook() + "NewAuthor:" + this.getNewauthor() 
				+ "NewTag:" + newtag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			flg = false;
		} finally {
			this.manager.closeConn();
			
		}
		return flg;
	}
//	org.opentest4j.AssertionFailedError: expected: <false> but was: <true>
//	at org.junit.jupiter.api.AssertionFailureBuilder.build(AssertionFailureBuilder.java:151)
//	at org.junit.jupiter.api.AssertionFailureBuilder.buildAndThrow(AssertionFailureBuilder.java:132)
//	at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:197)
//	at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:182)
//	at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:177)
//	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1145)
//	at test.DAOFacadeTest.insertTestOK(DAOFacadeTest.java:124)
//	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)


	public boolean insertBookInfo(List<BookInfo> data) {

		//BOOK＿AUTHOR(ビュー)に直接insertするメソッドも書く
		return false;
	}
	
	public List<BookInfo> selectBookInfoAll(){
		List<BookInfo> list = new ArrayList<>();
		try(Connection conn = this.getManager().getConn()){
			list = this.selectAll(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<BookInfo> selectBookInfoList(List<Integer> idList){
		List<BookInfo> list = new ArrayList<>();
		try(Connection conn = this.getManager().getConn()){
			list = this.selectList(conn, idList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public int updateBookInfoList() {
		return 0;
	}

	public int deleteBookInfoList(List<Integer> idList, String action) {
		int result = 0;
		int flag = 0;
		int count = 0;//該当するIDのあるすべての組み合わせ行数
		count = countBookAuthorList(idList, action);
		if (checkIDList(idList)) {
			if ("book".equals(action) || "author".equals(action)) {
				try (Connection conn = this.getManager().getConn()){			
					conn.setAutoCommit(false);

					bkdao = new BooksDAO();
					athdao = new AuthorsDAO();
					tagdao = new TaggingDAO();
//					ここの各ＤＡＯへのコネクション受け渡しが上手く行っていない？
					result = tagdao.deleteList(conn, idList, action);
					if (result == count) {
						//TAGGINGから削除した行数がBOOK＿AUTHORの該当組み合わせ行数と等しいなら
						if ("book".equals(action)) {
							flag = bkdao.deleteList(conn, idList, action);
						} else if ("author".equals(action)) {
							flag = athdao.deleteList(conn, idList, action);
						}	
						 if(idList.size() == flag) {//消したentityテーブルの行数が引数のidの総数と等しいなら
							conn.commit();
						}else {
							conn.rollback();
							throw new SQLException(action + "削除に失敗しました");
						}
					}else {
						conn.rollback();
						throw new SQLException("Tagging削除に失敗しました");//これが出た
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} 
//				finally {
				
//					this.getManager().closeConn();
//				}
				System.out.println("result:" + result + " count:" + count + " flag:" + flag);
			}else {
				throw new IllegalArgumentException("actionの値が不正です");
			}
		}
		return flag;

	} 
	
	int countBookAuthorList(List<Integer> idlist, String action){
		int count = 0;
		
		String id = null;
		if (action.equals("book")) {
			id = "BOOK_ID";
		} else if (action.equals("author")) {
			id = "AUTHOR_ID";
		}
		String placeholder = super.convertListtoPlaceholder(idlist);

		sql = "SELECT COUNT ( * ) AS COUNTING "
			+ "FROM BOOK_AUTHOR WHERE " + id + " IN " + placeholder;
		try (Connection conn = this.getManager().getConn()){

			PreparedStatement pstmt = conn.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				count = rs.getInt("COUNTING");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	BookInfo getInstancefromResultSet(ResultSet rs) throws SQLException {
		
		int bookid = rs.getInt("BOOK_ID");
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
		//No enum constant model.entity.BookStatus.既読
		boolean favorite = rs.getBoolean("FAVORITE");
		Book book = new Book(bookid, title, pubdate, publisher, pages, isbn, ndc, price, registime, comment, status,
				favorite);
		
		int authorid = rs.getInt("AUTHOR_ID");
		String name = rs.getString("NAME");
		Profession profession 
//		= Profession.getByOrder(rs.getInt("PROFESSION"));
		= Profession.getByPFName(rs.getString("PROFESSION"));
		Author author = new Author(authorid, name, profession);
		
		return new BookInfo(book, author);
	}
	
	@Override
	protected String createSelectAllSQL() {
		String sql = "SELECT * FROM BOOK_AUTHOR";
		return sql;
	}
	
	@Override
	protected String createSelectListSQL(List<Integer> idlist) {
		String placeholder = convertListtoPlaceholder(idlist);

		String sql = "SELECT * FROM BOOK_AUTHOR WHERE BOOK_ID IN " + placeholder;
		return sql;
	}
	@Override
	protected String createInsertOneSQL(DTO dto) {
		return null;
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
		return null;
	}
}

