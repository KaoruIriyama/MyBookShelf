package model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import model.entity.BookInfo;

public class SearchBookLogic {
	public final List<String> querywords = 
		Arrays.asList("isbn", "any", "title", "creator", "publisher", "from", "until");
	
	
	
	//query=値の形式の文字列をリストとして引数に渡す
	public List<BookInfo> execute(Map<String, String> keyWordMap){
		List<BookInfo> infolist = new ArrayList<>();
		/** OpenBD APIを利用して、書籍データをJSON形式で取得する*/
		//			String openbdurl = "https://api.openbd.jp/v1/get?isbn=" + isbn + "&pretty";
		String query = parseKeyWords(keyWordMap);
		/** 国立国会図書館webAPIを利用して、書籍データをRSS形式で取得する*/
		String ndcurl = "https://ndlsearch.ndl.go.jp/api/opensearch?" + query;
		RSSParser rssparser = new RSSParser();
		if (rssparser.parseRSStoInfo(fetchByISBN(ndcurl)).size() > 0) {
			infolist = rssparser.parseRSStoInfo(fetchByISBN(ndcurl));
		} 
//			else {
//			throw new NullPointerException("書籍情報の取得に失敗しました。");
//		}
		return infolist;
	}
	
	public String parseKeyWords(Map<String, String> keyWordMap){
		List<String> keywords = new ArrayList<>();
		for(String key : keyWordMap.keySet()) {
			Optional<String> value = Optional.ofNullable(keyWordMap.get(key));
			if(value.orElse("").length() >= 1) {
				keywords.add(key + "=" + value.get());
			}
		}
		String query = String.join("&", keywords);
		return query;
	}

//	public List<BookInfo> searchByISBN(String isbn) {
//		List<BookInfo> infolist = new ArrayList<>();
//
//		if (BookISBN.isbnCheck(isbn)) {
//			/** OpenBD APIを利用して、書籍データをJSON形式で取得する*/
//			//			String openbdurl = "https://api.openbd.jp/v1/get?isbn=" + isbn + "&pretty";
//			/** 国立国会図書館webAPIを利用して、書籍データをRSS形式で取得する*/
//			String ndcurl = "https://ndlsearch.ndl.go.jp/api/opensearch?isbn=" + isbn;
//			RSSParser rssparser = new RSSParser();
//			if (rssparser.parseRSStoInfo(fetchByISBN(ndcurl)).size() > 0) {
//				infolist = rssparser.parseRSStoInfo(fetchByISBN(ndcurl));
//			} else {
//				throw new NullPointerException("書籍情報の取得に失敗しました。");
//			}
//		}
//		return infolist;
//	}
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
	//  HttpClientを使ってWebAPIに接続、
	//	Getリクエストを送ってレスポンスをInputStreamReaderに変換して返す
	//	urlを引数として渡すことで、場合によって複数のWebAPIを切り替えることが出来る
	public static InputStream connectWebAPI(String urlreq) {
		InputStream is = null;
		HttpClient client;
		
		String encodedURL = queryURLPrettier(urlreq);//半角スペースなどをエンコード
		System.out.println(encodedURL);
		try {
			client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(encodedURL)).GET().build();//java.net.URISyntaxException

			HttpResponse<InputStream> response = 
					client.send(request, HttpResponse.BodyHandlers.ofInputStream());

			if (response.statusCode() == 200 && response.body() != null) {
				is = response.body();
			} else {
				System.out.println("ERROR" + response.statusCode());
			}
		} catch (URISyntaxException | IOException | InterruptedException | IllegalArgumentException e) {
			e.printStackTrace();
		} 
		return is;
	}

	private static String queryURLPrettier(String queryURL) {
		//認識されてくれない
		return queryURL.replaceAll(" ", "%20");
	}
//	
//	private String keywordPrettier(String keyword) {
//		if(!AuthorName.isHankaku(keyword)) {
//			keyword = "\"" + keyword + "\""; 
//		}
//		return keyword;
//	}
}
