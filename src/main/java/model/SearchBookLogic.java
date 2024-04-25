package model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.entity.BookInfo;

public class SearchBookLogic {
	//query=値の形式の文字列をリストとして引数に渡す
	public List<BookInfo> searchByKeyword(List<String> keywords){
		List<BookInfo> infolist = new ArrayList<>();
		String query = String.join("&", keywords);
		
		/** 国立国会図書館webAPIを利用して、書籍データをRSS形式で取得する*/
		String ndcurl = "https://ndlsearch.ndl.go.jp/api/opensearch?" + query;
		RSSParser rssparser = new RSSParser();
		if (rssparser.parseRSStoInfo(fetchByISBN(ndcurl)).size() > 0) {
			infolist = rssparser.parseRSStoInfo(fetchByISBN(ndcurl));
		} else {
			throw new NullPointerException("書籍情報の取得に失敗しました。");
		}
		return infolist;
	}
	
	public List<BookInfo> searchByISBN(String isbn) {
		List<BookInfo> infolist = new ArrayList<>();

		if (isbnCheck(isbn)) {
			/** OpenBD APIを利用して、書籍データをJSON形式で取得する*/
			//			String openbdurl = "https://api.openbd.jp/v1/get?isbn=" + isbn + "&pretty";
			/** 国立国会図書館webAPIを利用して、書籍データをRSS形式で取得する*/
			String ndcurl = "https://ndlsearch.ndl.go.jp/api/opensearch?isbn=" + isbn;
			RSSParser rssparser = new RSSParser();
			if (rssparser.parseRSStoInfo(fetchByISBN(ndcurl)).size() > 0) {
				infolist = rssparser.parseRSStoInfo(fetchByISBN(ndcurl));
			} else {
				throw new NullPointerException("書籍情報の取得に失敗しました。");
			}
		}
		return infolist;
	}

	//  HttpClientを使ってWebAPIに接続、
	//	Getリクエストを送ってレスポンスをInputStreamReaderに変換して返す
	//	urlを引数として渡すことで、場合によって複数のWebAPIを切り替えることが出来る
	public static InputStream connectWebAPI(String urlreq) {
		InputStream is = null;
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(urlreq)).GET().build();

			HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

			if (response.statusCode() == 200 && response.body() != null) {
				is = response.body();
				//				System.out.println("InputStream:" + is);
			} else {
				System.out.println("ERROR" + response.statusCode());

			}
		} catch (URISyntaxException | IOException | InterruptedException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return is;
	}

	/** 国立国会図書館webAPIを利用して、書籍データをRSS形式で取得する*/
	public InputStream fetchByISBN(String ndcurl) {

		InputStream is = null;

		if (SearchBookLogic.connectWebAPI(ndcurl) != null) {
			is = SearchBookLogic.connectWebAPI(ndcurl);
		} else {
			throw new NullPointerException("書籍情報の取得に失敗しました。");
		}

		return is;
	}

	static boolean isbnCheck(String isbn) {
		// isbn-13 計算ルール 参考: https://isbn.jpo.or.jp/index.php/fix__calc_isbn/
		// ISBN-10 参考: https://ja.wikipedia.org/wiki/ISBN
		boolean flag = false;
		char[] iArray = isbn.replace("-", "").toCharArray();
		//		    System.out.println(iArray);
		if (iArray.length == 13) {
			if (isbn.startsWith("978") || isbn.startsWith("979")) {
				//ISBN-13
				int checker_13A = 0;
				int checker_13B = 0;
				for (int i = 0; i < 11; i = i + 2) {
					//charとして保管された数字を計算に使う場合はCharacter.getNumericValue()で対応するintに変換
					checker_13A += Character.getNumericValue(iArray[i]);
					//A = ９７８から始まる ISBN の奇数の桁の数字の合計
					checker_13B += Character.getNumericValue(iArray[i + 1]);
					// B = ９７８から始まる ISBN の偶数の桁の数字の合計
				}
				int checker_13c = checker_13A + checker_13B * 3; //C = A + B * 3
				//		            System.out.println(checker_13A + ":" + checker_13B + ":" + checker_13c);
				if (10 - checker_13c % 10 == Character.getNumericValue(iArray[12])) {
					// (10 - Cの下1桁(Cを10で割った余り)) = isbn最終桁であるなら真
					flag = true;
				}
			}
		} else if ((iArray.length == 10)) {
			//ISBN-10 
			int checker_ten = 0;
			for (int i = 0; i < 9; i++) {
				//チェックディジットを除いた左側の桁から10、9、8…2を掛けてそれらの和を取る
				checker_ten += Character.getNumericValue(iArray[i]) * (10 - i);
			}
			if ((11 - checker_ten % 11) == Character.getNumericValue(iArray[9])) { //和を11で割った余りをisbnの最終桁と比べる
				//ISBN-10 のチェックデジットが0～9又は11の場合は最終桁と同値になるはず
				flag = true;
			} else if (11 - (checker_ten % 11) == 10 && iArray[9] == 'X') {
				//ISBN-10 のチェックデジットが10の場合、最終桁はXのはず
				flag = true;
			}
		}
		return flag;
	}

	//apiで返ってきた著者名を姓 名の並びに変換する
	static String nameParser(String name) {
		//		最初に"　"などの文字を" "に置換(スペースは正規表現で指定)
		name = name.replaceAll("[\\h]", "");
		String result = "";

		if (name.contains(",")) {
			//			","で前後を分解する(姓・名の分離)
			String[] splited = name.split(",");
			//名前が半角(英字)かどうかを判定
			if (StringUtils.isAlphanumeric(name) || StringUtils.isAsciiPrintable(name)) {
				//				半角かつ、名前に","があれば、後ろ・前の順に並べる
				result = splited[1] + " " + splited[0];
			} else {
				//				全角ならばそのまま並べる
				result = splited[0] + " " + splited[1];
			}
		} else {
			result = name;
		}

		return result;
	}
}
