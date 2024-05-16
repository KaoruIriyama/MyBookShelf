package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.Author;
import model.entity.BookInfo;
import model.entity.DTO;
import model.entity.Tagging;

public class TaggingDAO extends DAOTemplate{

	private ConnectionManager manager;

	public TaggingDAO(){
		this.manager = ConnectionManager.getInstance();
	}
	
	TaggingDAO(ConnectionManager manager) {
		this.manager = manager;
	}
	
	//for test
	public TaggingDAO(String db_url, String db_user, String db_pass){
		this.setManager(ConnectionManager.getInstance(db_url, db_user, db_pass));
	}	

	public int insertTaggingfromBookInfo(Connection conn, BookInfo info) throws SQLException {
		int tagcount = 0;
		int result = 0;
		//一冊の本に付属する著者名の数だけタグの登録を行い、タグの総登録数が
		//		著者名の数と一致したときだけ1(＝true)を返す
		tagcount += this.insertOne(conn, info);
		if (tagcount != 0) {
			result++;
		}
//			List<BookInfo> mappedlist = BookInfo.antitributeOne(info);
//			for(BookInfo bi : mappedlist) {
//				tagcount += 
//						this.executeUpdatefromSQL(conn, this.createInsertTaggedSQL(bi));
//						
//				if (tagcount != 0) {
//					result++;
//				}
//			}

		return result;
	}

	int updateTaggingList() {
		return 0;
	}
	
	@Override
	Tagging getInstancefromResultSet(ResultSet rs) throws SQLException {
		int bookid = rs.getInt("BOOK_ID");
		int authorid = rs.getInt("AUTHOR_ID");
		return new Tagging(bookid, authorid);
	}

	@Override
	protected String createSelectAllSQL() {
		return "SELECT * FROM TAGGING";
	}

	@Override
	protected String createSelectListSQL(List<Integer> idList) {
		String placeholder = super.convertListtoPlaceholder(idList);
		String sql = "SELECT * FROM TAGGING WHERE " 
//		+ id 
		+ " IN " + placeholder;
		return sql;
	}
	
	protected <T extends DTO> String createInsertTaggedSQL(T t) {
		BookInfo info = null;
		String sql = null;
		if(t instanceof BookInfo) {info = (BookInfo) t;}
		List<String> varchars = AuthorsDAO.getVarcharListFromDTO(info.getAuthors().get(0));
		if(info.isNotEmpty()) {
			sql 
				= "MERGE INTO TAGGING (BOOK_ID , AUTHOR_ID) "
				+ "KEY (BOOK_ID , AUTHOR_ID) "
				+ "VALUES "
				+ "((SELECT ID FROM BOOKS WHERE ISBN = "
				+ decorateBySingleQuote(info.getBook().getISBN()) + "), "
				+ "(SELECT ID FROM AUTHORS WHERE NAME = "
				+ varchars.get(0) + " AND PROFESSION = "
				+ varchars.get(1) + "))";//決定版
		}		
		return sql;//文はあってる
	}

	@Override
	protected <T extends DTO> String createInsertOneSQL(T t) {
		BookInfo info = null;
		String sql = null;
		String valueholder = null;
		if(t instanceof BookInfo) {info = (BookInfo) t;}
		List<String> values = new ArrayList<>();

			for (Author au : info.getAuthors()) {
			
				if (au.isNotEmpty()) {
					values.add(convertListtoPlaceholder(AuthorsDAO.getVarcharListFromDTO(au)));
				}
			}
			if(values.size() > 2) {valueholder = "IN " + convertListtoPlaceholder(values);}
			else{valueholder = "= " + values.get(0);}
//			sql	= "MERGE INTO TAGGING (BOOK_ID , AUTHOR_ID) KEY (BOOK_ID , AUTHOR_ID) "
//					+ "VALUES "
//					+ "SELECT BOOKS.ID AS BOOK_ID, AUTHORS.ID AS AUTHOR_ID "
//					+ "FROM BOOKS JOIN AUTHORS "
//					+ "WHERE BOOKS.ISBN = " 
//					+ decorateBySingleQuote(info.getBook().getISBN()) + " AND "
//					+ "(AUTHORS.NAME, AUTHORS.PROFESSION) IN "
//					+ valueholder ;
			
			sql = "MERGE INTO TAGGING (BOOK_ID , AUTHOR_ID) KEY (BOOK_ID , AUTHOR_ID) "
					+ "SELECT BOOKS.ID AS BOOK_ID, AUTHORS.ID AS AUTHOR_ID "
					+ "FROM BOOKS "
					+ "JOIN AUTHORS "
					+ "WHERE BOOKS.ISBN = " 
					+ decorateBySingleQuote(info.getBook().getISBN()) + " AND "
					+ "(AUTHORS.NAME, AUTHORS.PROFESSION) "
					+ valueholder ;
		
		return sql;//文はあってる
	}
	
	//BookInfoをBookInfo.antitributeListで一対一に直してから入れること
	@Override
	public <T extends DTO> String createInsertListSQL(List<T> list) {
		BookInfo info = null;
		String sql = null;
		String valueholder = null;
		List<String> values = new ArrayList<>();
		for(T t : list) {
			if(t instanceof BookInfo) {info = (BookInfo) t;}
			
				if (info.isNotEmpty()) {
					values.add(convertListtoPlaceholder(AuthorsDAO.getVarcharListFromDTO(info.getAuthors().get(0))));
				}
			
			
				
		}
			valueholder = convertListtoPlaceholder(values);
			sql	
			= "MERGE INTO TAGGING (BOOK_ID , AUTHOR_ID) "
					+ "KEY (BOOK_ID , AUTHOR_ID) "
					+ "VALUES "
					+ "((SELECT ID FROM BOOKS WHERE ISBN = "
					+ decorateBySingleQuote(info.getBook().getISBN()) + "), "
					+ "(SELECT ID FROM AUTHORS WHERE (NAME, PROFESSION)　IN "
					+ valueholder + "))";
		
		return sql;
	}

	@Override
	protected <T extends DTO> String createUpdateListSQL(List<T> list) {
		return null;
	}

	@Override
	protected String createDeleteListSQL(List<Integer> idList, String action) {
		String id = null;
		if (action.equals("book")) {
			id = "BOOK_ID";
		} else if (action.equals("author")) {
			id = "AUTHOR_ID";
		} 
		String placeholder = super.convertListtoPlaceholder(idList);
		String sql = "DELETE FROM TAGGING WHERE " + id + " IN " + placeholder;
		return sql;
	}
}
