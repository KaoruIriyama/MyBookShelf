package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.entity.DTO;

public abstract class DAOTemplate {
	private ConnectionManager manager;

	public ConnectionManager getManager() {	return this.manager;}
	
	public void setManager(ConnectionManager manager) {this.manager = manager;}
	
	public final <T extends DTO> int insertOne(Connection conn, T t) throws SQLException{
		int result = 0;
		if(checkDTO(t)) {
			result += executeUpdatefromSQL(conn, createInsertOneSQL(t));
		}
		return result;
	}

	public final <T extends DTO> int insertList(Connection conn, List<T> list) throws SQLException{
		int result = 0;
		if (isListNotEmpty(list)) {
			result += executeUpdatefromSQL(conn, createInsertListSQL(list));
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
	
	protected final <T extends DTO> int updateList(Connection conn,List<T> list)
			throws SQLException {
		int result = 0;
		if (isListNotEmpty(list)) {
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

	protected abstract <T extends DTO> String createInsertOneSQL(T t);

	public abstract <T extends DTO> String createInsertListSQL(List<T> list);
	
	protected abstract String createSelectAllSQL();
	
	protected abstract String createSelectListSQL(List<Integer> idlist);
	
	protected abstract <T extends DTO> String createUpdateListSQL(List<T> list);
	
	protected abstract String createDeleteListSQL(List<Integer> idList, String action);

	protected ResultSet executeQueryfromSQL(Connection conn, String sql) throws SQLException {
//		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = conn.prepareStatement(sql).executeQuery();
		return rs;
	}

	protected int executeUpdatefromSQL(Connection conn, String sql) throws SQLException {
//		PreparedStatement pstmt = conn.prepareStatement(sql);
		int result = conn.prepareStatement(sql).executeUpdate();
		return result;
	}

	protected static String convertListtoPlaceholder(List<?> list) {
		List<String> strlist = list.stream().map(o -> o.toString()).toList();
		return "(" + String.join(",", strlist) + ")";
	}

	protected static String decorateBySingleQuote(String s) {
		return "\'" + s + "\'";
	}
	
	//idリストに0が含まれるかどうか調べる
	protected boolean checkIDList(List<Integer> idList) {
		return idList.stream().noneMatch(i -> i == 0);
	}
	//DTOのリストにNullが含まれていたら除外し、残りのインスタンスのフィールドにnullである値が含まれていないか調べる
	private <T extends DTO> boolean isListNotEmpty(List<T> list) {
		return list.stream().filter(Objects::nonNull).allMatch(t -> t.isNotEmpty());
	}
	//DTOインスタンスそのものと、インスタンスのフィールドに対するnullチェック
	private <T extends DTO> boolean checkDTO(T t) {
		return Objects.nonNull(t) && t.isNotEmpty();
	}
	
	abstract <T extends DTO> T getInstancefromResultSet(ResultSet rs) throws SQLException;
}