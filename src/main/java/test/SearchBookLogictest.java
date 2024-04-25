package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.RSSParser;
import model.SearchBookLogic;
import model.entity.Author;
import model.entity.Book;
import model.entity.BookInfo;
import model.entity.Profession;

public class SearchBookLogictest {
	private SearchBookLogic logic = new SearchBookLogic();
	private RSSParser rssp = new RSSParser();
//	private OriginalJsonParser jsonp = new OriginalJsonParser();
	BookInfo example = new BookInfo(
			new Book("新版 思考の整理学", LocalDate.of(2024, 2, 13),
					"筑摩書房", 256, "9784480439123", "なし", 630, ""),
			new Author("外山 滋比古", Profession.Author));
//	@Test
	public void searchByKeywordOK() {
		List<String> querylist = new ArrayList<>();
		List<BookInfo> list = logic.searchByKeyword(querylist);
		
	}
	
//	@Test
	public void searchByKeywordNG() {
		List<String> querylist = new ArrayList<>();
		List<BookInfo> list = logic.searchByKeyword(querylist);
		assertEquals(list.size(), 0);
	}
	
	@Test
	public void searchByISBNOK() {
		String isbn = "978-4-480-43912-3";
		List<BookInfo> list = logic.searchByISBN(isbn);
		assertEquals(list.get(0), example);
	}

	@Test
	public void searchByISBNNG() {
		String isbn = "978-4-480-43912-7";//無効なISBN
		List<BookInfo> list = logic.searchByISBN(isbn);
		assertEquals(list.size(), 0);
		//		assertThrows(NullPointerException.class, () -> {logic.executeByISBN(isbn);});
	}

	//	@Test
//	public void parseJsontoInfoTestOK() {
//		//openbdから返ってきたjsonファイルを読み取れるかどうかのテスト
//
//		//		"openBD get 978-4-480-43912-3.json"
//		InputStream fis = null;
//		try {
//			Path path = Paths.get("C:/programming//openBD get 978-4-480-43912-3.json");
//			fis = Files.newInputStream(path);
//			List<BookInfo> list = jsonp.parseJsontoInfo(fis);
//			//			 BookInfo info = jsonp.parseJsontoInfo(fis);
//			//			 assertNotNull(info);
//			//			 assertEquals(example, info);
//			System.out.println(list);
//			assertEquals(list.get(0), example);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail();
//		} finally {
//			try {
//				if (fis != null) {
//					fis.close();
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	//	 @Test
//	public void parseJsontoInfoTestNG() {
//		//正しい形式のjsonfileでないものを渡されてきちんとエラーが出るかのテスト
//		//ここではxmlデータを渡す
//		InputStream fis = null;
//		try {
//			Path path = Paths.get("C:/programming/opensearch 978-4-480-43912-3.xml");
//			fis = Files.newInputStream(path);
//			List<BookInfo> list = jsonp.parseJsontoInfo(fis);
//			assertEquals(list.size(), 0);
//			//				 BookInfo info = jsonp.parseJsontoInfo(fis);
//			//				 assertNull(info);
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail();
//		} finally {
//			try {
//				if (fis != null) {
//					fis.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	//	 @Test
	public void parseRSStoInfoOK() {
		InputStream fis = null;
		try {
			Path path = Paths.get("C:/programming/opensearch 978-4-480-43912-3.xml");
			fis = Files.newInputStream(path);
			//org.xml.sax.SAXParseException; 
			//				lineNumber: 1; columnNumber: 1;
			//				プロローグにはコンテンツを指定できません。　
			//				PC内のxmlファイルは問題なく読み込めたのでEclipse内部に問題がある

			//				 BookInfo info = rssp.parseRSStoInfo(fis);
			//				 assertNotNull(info);

			List<BookInfo> list = rssp.parseRSStoInfo(fis);
			assertNotNull(list);
			System.out.println(list.get(0));
			assertEquals(list.get(0), example);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//	 @Test
	public void parseRSStoInfoNG() {
		InputStream fis = null;
		try {
			Path path = Paths.get("C:/programming//openBD get 978-4-480-43912-3.json");
			fis = Files.newInputStream(path);
			//NoSuchFile
			//				InputStreamReader isr = new InputStreamReader(fis);
			//				 BookInfo info = rssp.parseRSStoInfo(fis);
			List<BookInfo> list = rssp.parseRSStoInfo(fis);
			assertEquals(list.size(), 0);

		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
