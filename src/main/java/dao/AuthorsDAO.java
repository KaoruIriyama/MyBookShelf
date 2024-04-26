package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.Author;
import model.entity.DTO;
import model.entity.Profession;

class AuthorsDAO extends DAOTemplate{

	private ConnectionManager manager;
//	protected static DataSource source = null;
//	protected Connection conn = null;
	private String sql;
	private ResultSet rs;
	private PreparedStatement pstmt;
	private Connection conn;
	
	AuthorsDAO(){
		this.manager = ConnectionManager.getInstance();
		this.conn = this.manager.getConn();
	}
	
	AuthorsDAO(Connection conn){
		this.conn = conn;
	}
	
	AuthorsDAO(ConnectionManager manager) {
		this.manager = manager;
		this.conn = this.manager.getConn();
	}

	int insertAuthorList(List<Author> alist) throws SQLException {
		int result = 0;
		if (alist.stream().allMatch(a -> a.isNotEmpty())) {
			//		try{

			//				インスタンスごとに(va, vb, ... , vn)という文字列を
			//				つくって返すメソッドを作る
			//面倒なのでとりあえず力業
			List<String> values = new ArrayList<>();
			for (Author au : alist) {
				if (au.isNotEmpty()) {
					StringBuilder sb = new StringBuilder();
					sb.append(au.getName().toString()).append(",").append(au.getProfession().getPFName());
					String value = "( " + sb.toString() + " )";
					values.add(value);
				}
			}
			String valueholder = convertListtoPlaceholder(values);

			//Merge Into文(対象テーブルに登録したいデータが存在しないときのみ登録)
			sql = "MERGE INTO AUTHORS USING"
					+ "(VALUES " + valueholder + ") "
					+ "SOURCE (NAME, PROFESSION) "
					+ "ON ((SOURCE.NAME = AUTHORS.NAME) AND "
					+ "(SOURCE.PROFESSION = AUTHORS.PROFESSION)) "
					+ "WHEN NOT MATCHED BY SOURCE THEN "
					+ "INSERT VALUES "
					+ "(NAME, PROFESSION) ";
			
			this.pstmt = this.conn.prepareStatement(sql);

			result = pstmt.executeUpdate();
			
			System.out.println("AUTHORS" + sql + " "+ result);
		}
//		this.closeConnection(pstmt, rs);
		this.conn = null;
		//		}catch(SQLException e) {
		//			e.printStackTrace();
		//			System.out.println("recordBook()の実行中に例外が発生しました。");
		//			return 0;//ここが引っかかった
		//		}
		//		finally {
		//			this.closeConnection(pstmt, rs);
		//		}
		return result;
	}

	int updateAuthorList() {
		return 0;
	}

	@Override
	Author getInstancefromResultSet(ResultSet rs) throws SQLException {
		int id = 0;
		if(rs.getInt("ID") != 0) {id = rs.getInt("ID");}
		else if(rs.getInt("AUTHOR_ID") != 0) {id = rs.getInt("AUTHOR_ID");}
		String name = rs.getString("NAME");
		Profession profession 
//		= Profession.getByOrder(rs.getInt("PROFESSION"));
		= Profession.getByPFName(rs.getString("PROFESSION"));

		return new Author(id, name, profession);
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
	protected String createSelectAllSQL() {
		String sql = "SELECT * FROM AUTHORS";
		return sql;
	}

	@Override
	protected String createSelectListSQL(List<Integer> idList) {		
		String placeHolder = convertListtoPlaceholder(idList);	
		String sql = "SELECT * FROM AUTHORS WHERE ID IN " + placeHolder;
		return sql;
	}

	@Override
	protected String createUpdateListSQL(List<DTO> list) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected String createDeleteListSQL(List<Integer> idList, String action) {
		String placeHolder = convertListtoPlaceholder(idList);	
		//actionはこのクラスでは使わない
		String sql = "DELETE FROM AUTHORS WHERE ID IN " + placeHolder;
		return sql;
	}
}
