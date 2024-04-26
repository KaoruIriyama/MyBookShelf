package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.entity.Author;
import model.entity.BookInfo;
import model.entity.DTO;
import model.entity.Tagging;

class TaggingDAO extends DAOTemplate{

	private ConnectionManager manager;
	private String sql;

	private Connection conn;

	TaggingDAO(){
		this.manager = ConnectionManager.getInstance();
		this.conn = this.manager.getConn();
	}
	
	TaggingDAO(Connection conn) {
		this.conn = conn;
	}
	
	TaggingDAO(ConnectionManager manager) {
		this.manager = manager;
		this.conn = this.manager.getConn();
	}

	int insertTaggingfromBookInfo(BookInfo bi) throws SQLException {
		int tagcount = 0;
		int result = 0;
		//		List<String> namelist = bi.getAuthors().stream().map(au -> au.getName()).toList();
		//		List<String> proflist = bi.getAuthors().stream().map(au -> au.getProfession().toString()).toList();

		//		sql = "INSERT INTO AUTHORS VALUES "
		//			+ "(BOOK_ID = (SELECT ID FROM BOOKS WHERE ISBN = "
		//			+  bi.getBook().getISBN() + ") AND "
		//			+ "(AUTHOR_ID IN (SELECT ID FROM AUTHORS WHERE NAME IN "
		//			+ "( " + convertListtoPlaceholder(namelist)+ ") AND PROFESSION IN "
		//			+ "( " + convertListtoPlaceholder(proflist)+ "))";
		//一冊の本に付属する著者名の数だけタグの登録を行い、タグの総登録数が
		//		著者名の数と一致したときだけ1(＝true)を返す
		for (Author a : bi.getAuthors()) {
			sql = "INSERT INTO AUTHORS (BOOK_ID , AUTHOR_ID) VALUES "
					+ "((SELECT ID FROM BOOKS WHERE ISBN = "
					+ bi.getBook().getISBN() + "), "
					+ "(SELECT ID FROM AUTHORS WHERE NAME = "
					+ a.getName() + " AND PROFESSION = "
					+ a.getProfession() + "))";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			tagcount += pstmt.executeUpdate();
		}
		if (tagcount == bi.getAuthors().size()) {
			result += 1;
		}
//		this.closeConnection(pstmt, rs);
		this.conn = null;
		//		pstmt = conn.prepareStatement(sql);
		//		int result = pstmt.executeUpdate();
		System.out.println("AUTHORS" + sql + " "+ result);
		return result;
	}

//	List<Tagging> selectList(List<Integer> idlist, String action) {
//
//		List<Tagging> list = new ArrayList<>();
//		String id = null;
//		if (action.equals("book")) {
//			id = "BOOK_ID";
//		} else if (action.equals("author")) {
//			id = "AUTHOR_ID";
//		} else {
//		}
//		String placeholder = super.convertListtoPlaceholder(idlist);
//
//		sql = "SELECT * FROM TAGGING WHERE " + id + " IN " + placeholder;
//
//		try {
//			PreparedStatement pstmt = conn.prepareStatement(sql);
//			ResultSet rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				Tagging tag = getInstancefromResultSet(rs);
//				list.add(tag);
//			}
//
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//			return null;
//		} finally {
////			this.closeConnection(pstmt, rs);
//			this.conn = null;
//		}
//
//		return list;
//	}

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

	@Override
	protected String createInsertOneSQL(DTO dto) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected String createInsertListSQL(List<DTO> list) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected String createUpdateListSQL(List<DTO> list) {
		// TODO 自動生成されたメソッド・スタブ
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
