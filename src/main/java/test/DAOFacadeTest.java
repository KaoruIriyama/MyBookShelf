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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import dao.DAOFacade;
import model.entity.Author;
import model.entity.Book;
import model.entity.BookInfo;
import model.entity.Profession;

//参考にした：https://recruit.gmo.jp/engineer/jisedai/blog/db-unit-introduction/

//インメモリモードでのテストの参考
//https://qiita.com/hitomatagi/items/42fbb031ca95af50bb7e

public class DAOFacadeTest {

	

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


	@BeforeClass
	public static void createSchema() throws SQLException {
		//テスト用にデータベースを作成
				RunScript.execute(JDBC_URL, DB_USER, DB_PASS, "data/schema.sql", Charset.forName("UTF-8"), false);
	}
	@BeforeAll
	public static void setUp(){
		try {
			databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, DB_USER, DB_PASS);
//			databaseconn = databaseTester.getConnection();
//			PreparedStatement pstemp = databaseconn.prepareStatement("SET REFERENTIAL_INTEGRITY FALSE");
//			pstemp.execute();
			//データベースにテストデータを読み込ませる
			dataset = new FlatXmlDataSetBuilder().build(new InputSource("data/testdata.xml"));
			databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
			databaseTester.setDataSet(dataset);
			databaseTester.onSetup();//java.net.ConnectException: Connection refused: 
//				no further information: localhost"
//			new InsertIndentityOperation(DatabaseOperation.CLEAN_INSERT).execute(connection,dataSet); 
			
			//setUpOperation()の引数にDatabaseOperation.CLEAN_INSERTを指定すると
			//org.dbunit.dataset.NoSuchTableException: TAGGING
			//引数にDatabaseOperation.INSERTを指定すると
			//org.dbunit.dataset.NoSuchTableException: BOOKS
			
//			facade = new DAOFacade((Connection) databaseconn);
//			facade = new DAOFacade(dataSource());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

		@AfterAll
		public static void tearDown() throws Exception {
			databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
			databaseTester.setTearDownOperation(DatabaseOperation.TRUNCATE_TABLE);
			databaseTester.setDataSet(dataset);
//			databaseTester.setTearDownOperation(DatabaseOperation.CLOSE_CONNECTION(databaseTester.getConnection()));
//			databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);			
			databaseTester.onTearDown();
//			org.h2.jdbc.JdbcSQLSyntaxErrorException: "PUBLIC.AUTHORS" を空にできません
//			Cannot truncate "PUBLIC.AUTHORS"; SQL statement:
//			truncate table AUTHORS [90106-224]
		}

		@Test
	public void insertTestOK() {
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<BookInfo> insert_list = new ArrayList<>();
		//		書籍も作者も存在しない新規の書籍データ
		insert_list.add(new BookInfo(
				new Book("新版 思考の整理学", LocalDate.of(2024, 2, 13),
						"筑摩書房", 256, "9784480439123", "なし", 630, ""),
				new Author("外山 滋比古", Profession.Author)));
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
		System.out.println(facade.getNewbook() + ": " + facade.getNewauthor());
		//0:0
		
		assertEquals(facade.getNewbook(), 2);
		assertEquals(facade.getNewauthor(), 1);
		assertEquals(facade.insertBookInfo(insert_list), true);//false
		System.out.println(facade.selectBookInfoAll());
		
//		System.out.println(facade.selectBookInfoAll());
	}

		@Test
	public void insertTestNGBoth() {
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<BookInfo> insert_both = new ArrayList<>();
		//		書籍も作者も不完全なデータ
		insert_both.add(new BookInfo(
				new Book("新版 思考の整理学", null,
						"筑摩書房", 256, "9784480439123", null, 630, ""),
				new Author(null, Profession.Author)));
		assertEquals(facade.insertBookInfo(insert_both), false);
		assertEquals(facade.getNewbook(), 0);
		assertEquals(facade.getNewauthor(), 0);
	}

		@Test
	public void insertTestNGBook() {
		//		書籍のみが不完全なデータ
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<BookInfo> insert_book = new ArrayList<>();
		insert_book.add(new BookInfo(
				new Book(null, LocalDate.of(1985, 1, 16),
						"岩波書店", 138, null, "134.2", 638, ""),
				new Author("Immanuel Kant", Profession.Author)));
		assertEquals(facade.insertBookInfo(insert_book), false);
		assertEquals(facade.getNewbook(), 0);
		assertEquals(facade.getNewauthor(), 0);
	}

	@Test
	public void insertTestNGAuthor() {
		//		作者のみが不完全なデータ
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<BookInfo> insert_author = new ArrayList<>();
		insert_author.add(new BookInfo(
				new Book("新版 思考の整理学", LocalDate.of(2024, 2, 13),
						"筑摩書房", 256, "9784480439123", "なし", 630, ""),
				new Author(null, null)));

		assertEquals(facade.insertBookInfo(insert_author), false);
		assertEquals(facade.getNewbook(), 0);
		assertEquals(facade.getNewauthor(), 0);
	}

//	SELECT SUCCESS(20240425)	
	
//	@Test
	public void selectBookInfoAllTest() {
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<BookInfo> all_list = BookInfo.retributeList(facade.selectBookInfoAll());
//		System.out.println(all_list);
		//リストの長さ、最初の書籍情報、最後から二番目の書籍の著者情報を調べる
		assertEquals(all_list.size(), 5);
		assertEquals(all_list.get(0).getBook().getTitle(), "永遠平和のために");
		assertEquals(all_list.get(3).getAuthors().size(), 3);
		assertEquals(all_list.get(1).getAuthors().get(0).getProfession().getPFName(),"編者");
	}

//	@Test
	public void selectBookInfoListTestOK() {
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<Integer> idlist = Arrays.asList(1, 3, 5);
//		List<BookInfo> select_list = facade.selectBookAuthorList(idlist);
		List<BookInfo> select_list = BookInfo.retributeList(facade.selectBookInfoList(idlist));
		assertEquals(select_list.size(), 3);
	}

//	@Test
	public void selectBookInfoListTestNG() {
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		//idは1以上であるため、0を指定しても取得できない
		List<Integer> idlist = Arrays.asList(0, 1, 2);
//		List<BookInfo> select_list = facade.selectBookAuthorList(idlist);
		List<BookInfo> select_list = BookInfo.retributeList(facade.selectBookInfoList(idlist));
		assertEquals(select_list.size(), 0);
	}

	//	@Test
	public void updateListTestOK() {

	}

	//	@Test
	public void updateListTestNG() {

	}

//	DELETE SUCCESS(20240425)
//		@Test
	public void deleteListTestOK() {
		facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<Integer> bookidlist = Arrays.asList(1, 3);
		assertEquals(facade.deleteBookInfoList(bookidlist, "book"), 2);
		List<Integer> authoridlist = Arrays.asList(5, 6);
		assertEquals(facade.deleteBookInfoList(authoridlist, "author"), 2);
		
	}

//		@Test
	public void deleteListTestNG() {
		//idは1以上であるため、0を指定しても取得できない
			facade = new DAOFacade(JDBC_URL, DB_USER, DB_PASS);
		List<Integer> bookidlist = Arrays.asList(0, 2);
		assertEquals(facade.deleteBookInfoList(bookidlist, "book"), 0);
		List<Integer> authoridlist = Arrays.asList(0, 4);
		assertEquals(facade.deleteBookInfoList(authoridlist, "author"), 0);
	}

	//	JUnitでDataSourceを用いる場合は込み入った準備が必要
	//	参考：https://okinaka.hatenablog.com/entry/20120408/1333823789
}
