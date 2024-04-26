package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.DTO;

public abstract class DAOTemplate {
	private ConnectionManager manager;
//	private String sql;

//	static {
//		try {
//			Class.forName("org.h2.Driver");
//		} catch (ClassNotFoundException e) {
//			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
//		}
//	}

	public ConnectionManager getManager() {	return this.manager;}
	
	public void setManager(ConnectionManager manager) {this.manager = manager;}
	
//	public void closeConnection(PreparedStatement pstmt, ResultSet rs) {
//
//		try {
//			if (pstmt != null) {
//				pstmt.close();
//			}
//			if (rs != null) {
//				rs.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public final int insertOne(Connection conn, DTO dto) throws SQLException{
		int result = 0;
		if(dto.isNotEmpty()) {
			result = executeUpdatefromSQL(conn, createInsertOneSQL(dto));
		}
		return result;
	}

	public final int insertList(Connection conn, List<DTO> list) throws SQLException{
		int result = 0;
		if (list.stream().allMatch(dto -> dto.isNotEmpty())) {
			result = executeUpdatefromSQL(conn, createInsertListSQL(list));
		}
		return result;
	}

	protected final <T extends DTO> List<T> selectAll(Connection conn) throws SQLException{
		List<T> list = new ArrayList<>();
		
			ResultSet rs = executeQueryfromSQL(conn, createSelectAllSQL());

			while (rs.next()) {
				T dto = getInstancefromResultSet(rs);
				list.add(dto);
			}
			
			if(rs != null) {
				rs.close();
			}
		return list;
	}

	protected final <T extends DTO> List<T> selectList(Connection conn,List<Integer> idList)
			throws SQLException{
		List<T> list = new ArrayList<>();
		if (checkIDList(idList)) {

				ResultSet rs = executeQueryfromSQL(conn, createSelectListSQL(idList));
	
				while (rs.next()) {
					T dto = getInstancefromResultSet(rs);
					list.add(dto);
				}
				
				if(rs != null) {
					rs.close();
				}
		}
		return list;
	}
	
	protected final <T extends DTO> int updateList(Connection conn,List<DTO> list)
			throws SQLException {
		int result = 0;
		if (list.stream().allMatch(dto -> dto.isNotEmpty())) {
			result = executeUpdatefromSQL(conn, createUpdateListSQL(list));
		}
		return result; 
	}

	protected final <T extends DTO> int deleteList(Connection conn,List<Integer> idList, String action)
			throws SQLException {
		int result = 0;
		if (checkIDList(idList)) {
			result = executeUpdatefromSQL(conn, createDeleteListSQL(idList, action));
		}
		return result; 
	}

	protected abstract String createInsertOneSQL(DTO dto);

	protected abstract String createInsertListSQL(List<DTO> list);
	
	protected abstract String createSelectAllSQL();
	
	protected abstract String createSelectListSQL(List<Integer> idlist);
	
	protected abstract String createUpdateListSQL(List<DTO> list);
	
	protected abstract String createDeleteListSQL(List<Integer> idList, String action);

	private ResultSet executeQueryfromSQL(Connection conn, String sql) throws SQLException {
//		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = conn.prepareStatement(sql).executeQuery();
		return rs;
	}

	private int executeUpdatefromSQL(Connection conn, String sql) throws SQLException {
//		PreparedStatement pstmt = conn.prepareStatement(sql);
		int result = conn.prepareStatement(sql).executeUpdate();
		return result;
	}

	protected static String convertListtoPlaceholder(List<?> list) {
		List<String> strlist = list.stream().map(o -> o.toString()).toList();
		return " ( " + String.join(" , ", strlist) + " ) ";
	}

	//	インスタンスごとに(va, vb, ... , vn)という文字列を
	//	つくって返すメソッドを作る
	protected static String convertListtoValueholder(List<DTO> list) {

		List<String> values = new ArrayList<>();
	    	for(DTO o : list){ 
//		インスタンスoのもつフィールド一覧を取得し、
//		    		それぞれのフィールドデータがnullでなければsbに加える
//		    		StringBuilder sb = new StringBuilder();
					List<String> valuelist = o.getAllValue();
		    		String value = "( "+ String.join(" , ", valuelist) + " )";
		    		values.add(value);
		   		}
		String valueHolder = String.join(" , ", values);
		return valueHolder;
	}

	//idリストに0が含まれるかどうか調べる
	protected boolean checkIDList(List<Integer> idList) {
		return idList.stream().noneMatch(i -> i == 0);
	}
	
	abstract <T extends DTO> T getInstancefromResultSet(ResultSet rs) throws SQLException;
}