package test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import dao.DAOFacade;
import model.EditLogic;
import model.RecordBookLogic;
import model.ViewLogic;
import model.entity.BookInfo;

class LogicsTest {

	private static IDatabaseTester databaseTester = null;
//	private static IDatabaseConnection databaseconn = null; 
	private static IDataSet dataset;
	private static DAOFacade facade = null;

	
	private static final String JDBC_DRIVER ="org.h2.Driver";
//	private static final String JDBC_URL = "jdbc:h2:mem:bookShelf;DB_CLOSE_DELAY=-1";//インメモリモードで起動 
	private static final String JDBC_URL 
//	= "jdbc:h2:tcp://localhost/~/testShelf";
	= "jdbc:h2:~/testShelf";
	private static final String DB_USER = "sa";
	private static final String DB_PASS = "test";
	private List<BookInfo> infolist = new ArrayList<>();
	private List<BookInfo> resultlist = new ArrayList<>();
	
	@BeforeClass
	public static void createSchema() throws SQLException {
		//テスト用にデータベースを作成
		RunScript.execute(JDBC_URL, DB_USER, DB_PASS, "data/schema.sql", Charset.forName("UTF-8"), false);
	}
	
	@BeforeEach
	void setUpBeforeClass() throws Exception {
		try {
			databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, DB_USER, DB_PASS);
			//データベースにテストデータを読み込ませる
			dataset = new FlatXmlDataSetBuilder().build(new InputSource("data/testdata.xml"));
			databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
			databaseTester.setDataSet(dataset);
			databaseTester.onSetup();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}

	@AfterEach
	void tearDownAfterClass() throws Exception {
			databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);

			databaseTester.setDataSet(dataset);
		
			databaseTester.onTearDown();

	}

//	@Test
	void RecordBookLogicExecuteOK() {
		RecordBookLogic record = new RecordBookLogic(JDBC_URL, DB_USER, DB_PASS);
		//int
	}
	
//	@Test
	void RecordBookLogicExecuteNG() {
		RecordBookLogic record = new RecordBookLogic(JDBC_URL, DB_USER, DB_PASS);
	}
	//Select 2024/05/02 success
	@Test
	void ViewLogicSelectAllOK() {
		ViewLogic view = new ViewLogic(JDBC_URL, DB_USER, DB_PASS);
		this.resultlist = view.selectAll();
		System.out.println(resultlist);
	}
	
	@Test
	void ViewLogicSelectSomeOK() {
		ViewLogic view = new ViewLogic(JDBC_URL, DB_USER, DB_PASS);
		List<Integer> idlist = Arrays.asList(1, 3, 5);
		this.resultlist = view.selectSome(idlist);
		assertEquals(resultlist.size(), 3);
	}
	
	@Test
	void ViewLogicSelectSomeNG() {
		ViewLogic view = new ViewLogic(JDBC_URL, DB_USER, DB_PASS);
		List<Integer> idlist = Arrays.asList(0, 1, 2);
		this.resultlist = view.selectSome(idlist);
		assertEquals(resultlist.size(), 0);
	}
	
//	@Test
	void EditLogicExecuteUpdateOK() {
		EditLogic edit = new EditLogic(JDBC_URL, DB_USER, DB_PASS);
		fail("まだ実装されていません");
	}
	
//	@Test
	void EditLogicExecuteUpdateNG() {
		EditLogic edit = new EditLogic(JDBC_URL, DB_USER, DB_PASS);
		fail("まだ実装されていません");
	}
	//ExecuteDelete 2024/05/02 success
//	@Test
	void EditLogicExecuteDeleteOK() {
		EditLogic edit = new EditLogic(JDBC_URL, DB_USER, DB_PASS);
		List<Integer> bookidlist = Arrays.asList(1, 3);
		assertEquals(edit.executeDelete(bookidlist, "book"), 2);
		List<Integer> authoridlist = Arrays.asList(5, 6);
		assertEquals(edit.executeDelete(authoridlist, "author"), 2);
	}
	
//	@Test
	void EditLogicExecuteDeleteNG() {
		EditLogic edit = new EditLogic(JDBC_URL, DB_USER, DB_PASS);
		List<Integer> bookidlist = Arrays.asList(0, 2);
		assertEquals(edit.executeDelete(bookidlist, "book"), 0);
		List<Integer> authoridlist = Arrays.asList(0, 4);
		assertEquals(edit.executeDelete(authoridlist, "author"), 0);
	}

}
