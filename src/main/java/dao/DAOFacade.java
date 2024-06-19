package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.entity.Author;
import model.entity.BookInfo;
import model.entity.DTO;
import model.entity.Profession;
import model.entity.book.Book;
import model.entity.book.BookStatus;

public class DAOFacade extends DAOTemplate{
	private ConnectionManager manager;
	private String sql;
	private int newbook = 0;
	private int newauthor = 0;
	
	private BooksDAO bkdao;
	private AuthorsDAO athdao;
	private TaggingDAO tagdao;
	

	public DAOFacade() {
		this.setManager(ConnectionManager.getInstance());
		this.bkdao = new BooksDAO();
		this.athdao = new AuthorsDAO();
		this.tagdao = new TaggingDAO();
		}
	//てすと用
	public DAOFacade(String db_url, String db_user, String db_pass){
		this.setManager(ConnectionManager.getInstance(db_url, db_user, db_pass));
		this.bkdao = new BooksDAO();
		this.athdao = new AuthorsDAO();
		this.tagdao = new TaggingDAO();
	}
	
	public ConnectionManager getManager() {	return this.manager;}
	
	public void setManager(ConnectionManager manager) {this.manager = manager;}
	
	public int getNewbook() {return newbook;}

	public void setNewbook(int newbook) {this.newbook = newbook;}

	public int getNewauthor() {	return newauthor;}

	public void setNewauthor(int newauthor) {this.newauthor = newauthor;}

	public BooksDAO getBkdao() {
		return bkdao;
	}
	public AuthorsDAO getAthdao() {
		return athdao;
	}
	public TaggingDAO getTagdao() {
		return tagdao;
	}
	public boolean insertBookInfo(List<BookInfo> data) {
		this.setNewbook(0);
		this.setNewauthor(0);
		int newtag = 0;
		boolean flg = false;
		try (Connection conn = this.getManager().getConn()){
			if(conn != null) {
			conn.setAutoCommit(false);
			
				for (BookInfo b : data) {
					try {
						this.setNewbook(this.getNewbook() + 
								bkdao.insertOne(conn, b.getBook()));
						if(b.getAuthors().size() >= 2) {//ここの分岐が上手く行っていない
							int acount = 0;
							for(int i = 0; i < b.getAuthors().size(); i++){
								acount += athdao.insertOne(conn, b.getAuthors().get(i));
							}
//							this.setNewauthor(this.getNewauthor() 
//									+ acount
//									+ athdao.insertList(conn, b.getAuthors())
//									);
							this.setNewauthor(this.getNewauthor() + acount);
						}else{
							this.setNewauthor(this.getNewauthor() 
									+ athdao.insertOne(conn, b.getAuthors().get(0)));
						}
					} catch (SQLException e) {
						e.printStackTrace();
						conn.rollback();
					} 
				}
				for (BookInfo bi : data) {
						//本一冊ごとにTAGGINGへの登録を行う(内部では著者名の数だけ登録をする)
						//						全ての著者が登録できたとき、insertメソッドの返り値は1になるので、
						//						それが確認出来たら本一冊の登録が終わったと判断し新規タグの登録完了カウントを
						//						1増やす
						newtag += tagdao.insertTaggingfromBookInfo(conn, bi);
				}
				
				if (newtag > 0 && newtag >= newbook) {
					conn.commit();
					flg = true;
				} else {
					conn.rollback();
					flg = false;
				}
				
//				System.out.println("Newbook:" + newbook + "NewAuthor:" + newauthor
//				+ "NewTag:" + newtag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			flg = false;
		}

		return flg;
	}
	
	public List<BookInfo> selectBookInfoAll(){
		List<BookInfo> list = new ArrayList<>();
		try(Connection conn = this.getManager().getConn()){
			if(conn != null) {
			list = this.selectAll(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public List<BookInfo> selectBookInfoList(List<Integer> idList){
		List<BookInfo> list = new ArrayList<>();
		try(Connection conn = this.getManager().getConn()){
			if(conn != null) {
				list = this.selectList(conn, idList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public int updateBookInfoList(List<BookInfo> infolist) {
		int result = 0;
		//infolistをBookSDAO, AuthorsDAOにそれぞれ渡し、mergeを行う
		
//		両方上手く行ったらTaggingDAOでタグ付けを更新(または追加、削除)する
		//BookｓDAOの時はISBNのハイフンの削除（または追加)、出版日の更新に対応できるようにする
		try (Connection conn = this.getManager().getConn()){
			if(conn != null) {
				conn.setAutoCommit(false);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return result;
	}

	public int deleteBookInfoList(List<Integer> idList, String action) {
		int result = 0;
		int flag = 0;
		int count = 0;//該当するIDのあるすべての組み合わせ行数
		count += countBookAuthorList(idList, action);
		if (checkIDList(idList)) {
			if ("book".equals(action) || "author".equals(action)) {
				try (Connection conn = this.getManager().getConn()){
					if(conn != null) {
						conn.setAutoCommit(false);
	
						bkdao = new BooksDAO();
						athdao = new AuthorsDAO();
						tagdao = new TaggingDAO();
	//					ここの各ＤＡＯへのコネクション受け渡しが上手く行っていない？
						result += tagdao.deleteList(conn, idList, action);
						if (result == count) {
							//TAGGINGから削除した行数がBOOK＿AUTHORの該当組み合わせ行数と等しいなら
							if ("book".equals(action)) {
								flag += bkdao.deleteList(conn, idList, action);
							} else if ("author".equals(action)) {
								flag += athdao.deleteList(conn, idList, action);
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
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return 0;
				} 
//				System.out.println("result:" + result + " count:" + count + " flag:" + flag);
			}else {
				throw new IllegalArgumentException("actionの値が不正です");
			}
		}
		return flag;

	} 
	int[] countDAOList(Connection conn) throws SQLException {
		int[] count = new int[2];
		String sqlbook = "SELECT COUNT ( * ) AS B_COUNT FROM BOOKS;";
		String sqlauthor = "SELECT COUNT ( * ) AS A_COUNT FROM AUTHORS;";
		
			ResultSet rsbook = executeQueryfromSQL(conn, sqlbook);
			ResultSet rsauthor = executeQueryfromSQL(conn, sqlauthor);
			while(rsbook.next() && rsauthor.next()) {
				count[0] = rsbook.getInt("B_COUNT");
				count[1] = rsauthor.getInt("A_COUNT");
			}
		
		return count;
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
			if(conn != null) {
				ResultSet rs = executeQueryfromSQL(conn, sql);
				while(rs.next()) {
					count = rs.getInt("COUNTING");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
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
		Book book = new Book(bookid, title, pubdate, publisher, 
				pages, isbn, ndc, price, 
				registime, comment, status, favorite);
		
		int authorid = rs.getInt("AUTHOR_ID");
		String name = rs.getString("NAME");
		Profession profession 
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
	public <T extends DTO> String createInsertOneSQL(T t) {
		return null;
	}
	@Override
	public <T extends DTO> String createInsertListSQL(List<T> list) {
		return null;
	}
	@Override
	protected <T extends DTO> String createUpdateListSQL(List<T> list) {
	
		return null;
	}
	@Override
	protected String createDeleteListSQL(List<Integer> idList, String action) {
		return null;
	}
}

