package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import model.RSSParser;
import model.SearchBookLogic;
import model.entity.Author;
import model.entity.BookInfo;
import model.entity.Profession;
import model.entity.book.Book;

public class SearchBookLogictest {
	private SearchBookLogic logic = new SearchBookLogic();
	private RSSParser rssp = new RSSParser();
//	private OriginalJsonParser jsonp = new OriginalJsonParser();
	BookInfo example = new BookInfo(
			new Book("思考の整理学", LocalDate.of(2024, 2, 13),
					"筑摩書房", 256, "9784480439123", "なし", 630, ""),
			new Author("外山 滋比古", Profession.Author));
	Map<String, String> keyWordsingle;
	Map<String, String> keyWordplural;
	
	@Test
	public void executeTry() {
		keyWordsingle = new HashMap<String, String>();
		keyWordsingle.put("title", "赤ずきんの森の少女たち");
		List<BookInfo> list = logic.execute(keyWordsingle);
		System.out.println(list);//ISBNが全巻取れてない
	}
	
	//success 2024/06/06
//	@Test
	public void executeOKByISBN() {
		keyWordsingle = new HashMap<String, String>();
		keyWordsingle.put("isbn", "978-4-480-43912-3");
//		String isbn = "978-4-480-43912-3";
		List<BookInfo> list = logic.execute(keyWordsingle);
		System.out.println(list);
		assertEquals(list.get(0).getBook().getTitle(), example.getBook().getTitle());
		assertEquals(list.get(0).getAuthors().get(0), example.getAuthors().get(0));
		assertEquals(list.get(0).getBook().getPublisher(), example.getBook().getPublisher());
	}

//	@Test
	public void executeNGByISBN() {
		keyWordsingle = new HashMap<String, String>();
		keyWordsingle.put("isbn", "978-4-480-43912-7");
		List<BookInfo> list = logic.execute(keyWordsingle);
		assertEquals(list.size(), 0);
//		assertThrows(NullPointerException.class, () -> {logic.execute(keyWordsingle);});
	}
//success 2024/06/07
//	@Test
	public void executeOKByKeyWords() {
		keyWordsingle = new HashMap<String, String>();
		keyWordsingle.put("title", "思考の整理学");
		keyWordsingle.put("creator", "外山 滋比古");//fail
		keyWordsingle.put("from", "2024-02-01");
		keyWordsingle.put("until", "2024-03-01");
		List<BookInfo> singlelist = logic.execute(keyWordsingle);
		assertEquals(singlelist.get(0).getBook().getTitle(), example.getBook().getTitle());
		assertEquals(singlelist.get(0).getAuthors().get(0), example.getAuthors().get(0));
		assertEquals(singlelist.get(0).getBook().getPublisher(), example.getBook().getPublisher());
		
		keyWordplural = new HashMap<String, String>();
		keyWordplural.put("title", "めしにしましょう");
		keyWordplural.put("from", "2017-01-01");
		keyWordplural.put("until", "2017-12-31");
		List<BookInfo> plurallist = logic.execute(keyWordplural);
		assertEquals(plurallist.size(), 3);
		assertEquals(plurallist.get(2).getBook().getISBN(), "9784065105931");
	}

//	@Test
	public void executeNGByKeyWords() {
		keyWordsingle = new HashMap<String, String>();
		keyWordsingle.put("title", "思考の整理学");
		keyWordsingle.put("creator", "外山 滋比古");//success
		keyWordsingle.put("from", "2023-01-01");
		keyWordsingle.put("until", "2023-12-31");//検索結果が0件になる出版日の範囲指定
		List<BookInfo> singlelist = logic.execute(keyWordsingle);
		assertEquals(singlelist.size(), 0);//2のハズ？

		keyWordplural = new HashMap<String, String>();
		keyWordplural.put("title", "めしにしましょう");
		keyWordplural.put("from", "2014-01-01");
		keyWordplural.put("until", "2015-01-02");//検索結果が0件になる出版日の範囲指定
		List<BookInfo> plurallist = logic.execute(keyWordplural);
		assertEquals(plurallist.size(), 0);
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

//		 @Test
	public void parseRSStoInfoOK() {
		InputStream fis = null;
		try {
			Path path = Paths.get("C:/programming/opensearch 978-4-480-43912-3.xml");
			fis = Files.newInputStream(path);
			//org.xml.sax.SAXParseException; 
			//				lineNumber: 1; columnNumber: 1;
			//				プロローグにはコンテンツを指定できません。　
			//				PC内のxmlファイルは問題なく読み込めたのでEclipse内部に問題がある


			List<BookInfo> list = rssp.parseRSStoInfo(fis);
			System.out.println(list);
			assertNotNull(list);
			assertEquals(list.get(0).getBook().getTitle(), example.getBook().getTitle());
			assertEquals(list.get(0).getAuthors().get(0), example.getAuthors().get(0));
			assertEquals(list.get(0).getBook().getPublisher(), example.getBook().getPublisher());
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
