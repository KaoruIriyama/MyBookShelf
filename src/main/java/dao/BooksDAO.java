package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.entity.Book;
import model.entity.BookStatus;
import model.entity.DTO;

public class BooksDAO extends DAOTemplate{

	private ConnectionManager manager;
	private String sql;

	public BooksDAO(){
		this.manager = ConnectionManager.getInstance();
	}
	
	BooksDAO(ConnectionManager manager) {
		this.manager = manager;
	}
	
//for test
	public BooksDAO(String db_url, String db_user, String db_pass){
		this.setManager(ConnectionManager.getInstance(db_url, db_user, db_pass));
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
	public String createSelectListSQL(List<Integer> idlist) {
		String placeholder = convertListtoPlaceholder(idlist);

		String sql = "SELECT * FROM BOOKS WHERE ID IN " + placeholder;
		return sql;
	}

	@Override
	public <T extends DTO> String createInsertOneSQL(T t) {
		Book book = null;
		if(t instanceof Book) {book = (Book) t;}
		List<String> values = getVarcharListFromDTO(book);
	
	String valueholder = convertListtoPlaceholder(values);
	//Merge Into文(対象テーブルに登録したいデータが存在しないときのみ登録)
	String sql	
//	= "MERGE INTO BOOKS (TITLE, PUBLISH_DATE, PUBLISHER, PAGES, "
//			+ "ISBN, NDC, PRICE, COMMENT) "
//	+ "KEY (ISBN, PUBLISH_DATE) VALUES " + valueholder;//決定版
	
	= "MERGE INTO BOOKS USING DUAL ON (ISBN = " + values.get(4)
	+ " AND PUBLISH_DATE = " + values.get(1)
	+ ") WHEN NOT MATCHED THEN INSERT "
	+ "(TITLE, PUBLISH_DATE, PUBLISHER, PAGES, ISBN, NDC, PRICE, COMMENT) " 
	+ "OVERRIDING USER VALUE VALUES " + valueholder;
	
//	= "MERGE INTO BOOKS T USING (VALUES " + valueholder + ") "
//	+ "S(TITLE, PUBLISH_DATE, PUBLISHER, PAGES, ISBN, NDC, PRICE, COMMENT) "
//	+ "ON (T.ISBN = S.ISBN AND T.PUBLISH_DATE = S.PUBLISH_DATE) "
//	+ "WHEN NOT MATCHED THEN INSERT VALUES "
//	+ "(S.TITLE, S.PUBLISH_DATE, S.PUBLISHER, S.PAGES, S.ISBN, S.NDC, S.PRICE, S.COMMENT)";
		return sql;
	}

	List<String> getVarcharListFromDTO(Book book) {
		List<String> values = new ArrayList<>();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		if(book.isNotEmpty()) {

			values.add(decorateBySingleQuote(book.getTitle()));
			values.add(decorateBySingleQuote(book.getPublishDate().format(fmt)));
			values.add(decorateBySingleQuote(book.getPublisher()));
			values.add(String.valueOf(book.getPages()));
			values.add(decorateBySingleQuote(book.getISBN().getValue()));
			values.add(decorateBySingleQuote(book.getNDC()));
			values.add(String.valueOf(book.getPrice()));
			values.add(decorateBySingleQuote(book.getComment()));
		}
		return values;
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
		String placeHolder = convertListtoPlaceholder(idList);
		//actionはこのクラスでは使わない
		String sql = "DELETE FROM BOOKS WHERE ID IN " + placeHolder ;
		return sql;
	}
}
