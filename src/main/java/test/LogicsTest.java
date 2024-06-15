package test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.time.LocalDate;
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
import model.entity.Author;
import model.entity.BookInfo;
import model.entity.Profession;
import model.entity.book.Book;

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
		List<BookInfo> insert_list = new ArrayList<>();
		
		//		testdata.xmlに存在するデータ
		insert_list.add(new BookInfo(
				new Book("永遠平和のために", LocalDate.of(1985, 1, 16),
						"岩波書店", 138, "9784003362594", "134.2", 638, ""),
				new Author("Immanuel Kant", Profession.Author)));
		//		testdata.xmlに作者のみが存在するデータ
		insert_list.add(new BookInfo(
				new Book("カント「視霊者の夢」", LocalDate.of(2013, 3, 12),
						"講談社", 173, "9784062921619", "147", 680, "講談社学術文庫 ; 2161 心霊研究 「霊界と哲学の対話」(論創社 1991年刊)の抜粋"),
				new Author("Immanuel Kant", Profession.Author)));
//		書籍も作者も存在しない新規の書籍データ
		insert_list.add(new BookInfo(
				new Book("新版 思考の整理学", LocalDate.of(2024, 2, 13),
						"筑摩書房", 256, "9784480439123", "なし", 630, ""),
				new Author("外山 滋比古", Profession.Author)));
		//複数の著者を持つBookInfoを加える
		List<Author> authorlist = new ArrayList<>(
				Arrays.asList(new Author("Erich Fromm", Profession.Author),
						new Author("鈴木 晶", Profession.Translater)));
		insert_list.add(new BookInfo(
				new Book("愛するということ", LocalDate.of(2020,8,28),
						"紀伊國屋書店", 209, "978-4-314-01177-8", "158", 1300, "新訳版 1991年刊の改訳・新装版 原タイトル: THE ART OF LOVING"),
				authorlist));
		assertEquals(record.execute(insert_list), 3);//int
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
