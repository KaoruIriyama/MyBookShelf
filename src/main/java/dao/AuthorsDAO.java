package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.Author;
import model.entity.DTO;
import model.entity.Profession;

public class AuthorsDAO extends DAOTemplate{

	private ConnectionManager manager;

	public AuthorsDAO(){
		this.manager = ConnectionManager.getInstance();
	}
	
	AuthorsDAO(ConnectionManager manager) {
		this.manager = manager;
	}

	//for test
	public AuthorsDAO(String db_url, String db_user, String db_pass){
		this.setManager(ConnectionManager.getInstance(db_url, db_user, db_pass));
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
	public <T extends DTO> String createInsertOneSQL(T t) {
		Author au= null;
		String value = null;
		List <String> varchars = new ArrayList<>();
		if(t instanceof Author) {au = (Author) t;}
		if (au.isNotEmpty()) {
			varchars = getVarcharListFromDTO(au);
			value = convertListtoPlaceholder(varchars);
			}

		//Merge Into文(対象テーブルに登録したいデータが存在しないときのみ登録)
		String sql 
//		= "MERGE INTO AUTHORS (NAME, PROFESSION) "
//		+ "KEY (NAME, PROFESSION) VALUES " + value;//決定版
		
		= "MERGE INTO AUTHORS USING DUAL ON (NAME = " + varchars.get(0)
		+ "AND PROFESSION = " + varchars.get(1)
		+ ") WHEN NOT MATCHED THEN INSERT (NAME, PROFESSION) "
		+ "OVERRIDING USER VALUE VALUES " + value;
		
//		= "MERGE INTO AUTHORS T USING (VALUES " + value + ") "
//				+ "S(NAME, PROFESSION)"
//				+ "ON (T.NAME = S.NAME AND T.PROFESSION= S.PROFESSION) "
//				+ "WHEN NOT MATCHED THEN INSERT VALUES "
//				+ "(S.NAME, S.PROFESSION)";
		return sql;
	}

	@Override
	public <T extends DTO> String createInsertListSQL(List<T> list) {
		Author au= null;
		
		List<String> values = new ArrayList<>();

		for (T t : list) {
			if(t instanceof Author) {au = (Author) t;}
			if (au.isNotEmpty()) {
				values.add(" ROW" + convertListtoPlaceholder(getVarcharListFromDTO(au)));
			}
		}
		
		String valueholder 
//		= String.join(",", values);
		= convertListtoPlaceholder(values);
		//Merge Into文(対象テーブルに登録したいデータが存在しないときのみ登録)
		String sql 
//		= null;
//		= "MERGE INTO AUTHORS USING (NAME, PROFESSION) "
//		+ "KEY (NAME, PROFESSION) VALUES " + valueholder ;//決定版
		
		= "MERGE INTO AUTHORS USING DUAL ON ((NAME, PROFESSION) IN "
		+ valueholder 
		+ ") WHEN NOT MATCHED THEN INSERT (NAME, PROFESSION) "
		+ "OVERRIDING USER VALUE VALUES "
		+ valueholder;
//		org.h2.jdbc.JdbcSQLDataException: 
//			列 "PROFESSION CHARACTER VARYING(5)" の値が長過ぎます:
//				"ROW (U&'\\9234\\6728 \\6676', U&'\\8a33\\8005') (2147483647)"
//			Value too long for column "PROFESSION CHARACTER VARYING(5)": 
//				"ROW (U&'\\9234\\6728 \\6676', U&'\\8a33\\8005') (2147483647)";
//		SQL statement:
//			MERGE INTO AUTHORS USING DUAL ON ((NAME, PROFESSION) 
//					IN (('Erich Fromm','著者'),('鈴木 晶','訳者'))) 
//			WHEN NOT MATCHED THEN INSERT (NAME, PROFESSION) 
//			OVERRIDING USER VALUE VALUES (('Erich Fromm','著者'),('鈴木 晶','訳者')) [22001-224]
		
//		= "MERGE INTO AUTHORS T USING (VALUES " + String.join(", ", values)+ ") "
//		+ "S(NAME, PROFESSION) "
//		+ "ON (T.NAME = S.NAME AND T.PROFESSION= S.PROFESSION) "
//		+ "WHEN NOT MATCHED THEN INSERT VALUES "
//		+ "(S.NAME, S.PROFESSION)";
		return sql;
 
//		org.h2.jdbc.JdbcSQLSyntaxErrorException: 列番号が一致しません
//		Column count does not match; SQL statement:
//		MERGE INTO AUTHORS T USING 
//		(VALUES ('Erich Fromm','著者'), ('鈴木 晶','訳者')) 
//		S(NAME, PROFESSION) 
//		ON (T.NAME = S.NAME AND T.PROFESSION= S.PROFESSION) 
//		WHEN NOT MATCHED THEN INSERT VALUES (S.NAME, S.PROFESSION) [21002-224]
	}

	static List<String> getVarcharListFromDTO(Author au) {
		List<String> values = new ArrayList<>();
		if (au.isNotEmpty()) {
			values.add(decorateBySingleQuote(au.getName()));
			values.add(decorateBySingleQuote(au.getProfession().getPFName()));
		}
		return values;
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
	protected <T extends DTO> String createUpdateListSQL(List<T> list) {
	
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
