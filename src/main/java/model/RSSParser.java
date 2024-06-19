package model;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.entity.Author;
import model.entity.BookInfo;
import model.entity.Profession;
import model.entity.book.Book;

public class RSSParser {
	
	public static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("y.M.d");
	
//	Map<String, String> RSStags = new HashMap<>(); 
	
	/** fetchRSS()で取得した書籍データをList<BookInfo>に加工して返すメソッド。
	 * RSSの読み取りにはjavax.xml.xpathパッケージを用いる*/
	
	public List<BookInfo> parseRSStoInfo(InputStream is) {
		
		List<BookInfo> infolist = new ArrayList<>();
		BookInfo info = new BookInfo();

		try {
			//1.InputStreamとして返ってきたデータを読み取るためにDocumentインスタンスに変換
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			// 2.XPathの処理を実行するXPathのインスタンスを取得する
			javax.xml.xpath.XPath xpath = XPathFactory.newInstance().newXPath();
			// 3.XPathでの検索条件を作る
			javax.xml.xpath.XPathExpression expr = xpath.compile("/rss/channel/item");
			// 4.DocumentをXPathで検索して、結果をDOMのNodeListで受け取る
			NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

			// 5.XPathでの検索結果を持っているNodeListの内容でループ
			for (int i = 0; i < nodeList.getLength(); i++) {
				// 6.要素を検索しているのでNodeの実体はElement。Elementにキャストして使う。
				Element bookelement = (Element) nodeList.item(i);

				// 7.Elementから必要な情報を取得して出力する(getElementsByTagName()の戻り値はNodeListであることに注意)
				String title = getTextByTagName(bookelement,"dc:title",0);
				
				String pubdateStr = getTextByTagName(bookelement,"dcterms:issued",0);
				
				String publisher = getTextByTagName(bookelement,"dc:publisher",0);
				
				String pageStr = getTextByTagName(bookelement,"dc:extent", 0);

				String isbn = getTextByTagName(bookelement,"dc:identifier",0);//改善しないとだめ？

				String priceStr = getTextByTagName(bookelement,"dcndl:price",0);
				//ndcと補足情報のキーワードは同じ"dc:subject"タグに詰められていたのでここで一括処理
				
				NodeList subjects = bookelement.getElementsByTagName("dc:subject");
				String ndc = null;
				List<String> subs = new ArrayList<>();
				for (int j = 0; j < subjects.getLength(); j++) {
					Element e = (Element) subjects.item(j);
					if (e.hasAttribute("xsi:type")) {
						if (e.getAttribute("xsi:type").contains("dcndl:NDC")) {
							ndc = e.getTextContent();//ndc
						}
					} else {
						subs.add(e.getTextContent());//補足情報
					}
				}
				Optional<String> sub = Optional.ofNullable(String.join(" ", subs));
				
				//commentには文庫やシリーズの情報を詰める(在れば)
			
				String series = getTextByTagName(bookelement,"dcndl:seriesTitle",0);
				String volume = getTextByTagName(bookelement,"dcndl:volume",0).length() > 0 ? 
						getTextByTagName(bookelement,"dcndl:volume",0) + "巻": "";
				String desc = String.join(" ", getTextListByTagName(bookelement, "dc:description"));

				String comment = series + " " + volume + " " +sub.orElse("") + " " + desc;

				List<Author> alist = new ArrayList<>();
				Book book = new Book
						(title, datePrettier(pubdateStr), publisher, 
						integerPrettier(pageStr, "p"), isbn, ndc, 
						integerPrettier(priceStr, "円"), comment);
				//Authorの氏名はdc:creatorタグから取る
				List<String> authornames = getTextListByTagName(bookelement, "dc:creator");
				for(String name : authornames) {
					alist.add(new Author(name, Profession.Author));
				}
				info = new BookInfo(book, alist);
				infolist.add(info);
			}
		} catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return infolist;
	}
	
	private String getTextByTagName(Element element, String tagname, int index) {
		Node result = element.getElementsByTagName(tagname).item(index);
		return (result != null ? result.getTextContent() : "");
	}
	
	private List<String> getTextListByTagName(Element element, String tagname) {
		List<String> resultlist = new ArrayList<>();
		NodeList nodes = element.getElementsByTagName(tagname);
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getTextContent() != null) {
			resultlist.add(nodes.item(i).getTextContent());
			}
		}
		return resultlist;
	}
	
	int integerPrettier(String pageStr, String s) {
		//pageStrがnullなら0を、そうでないならStringを(余計な字sを取り除いて)intに変換したものを返す
		return (pageStr != null? Integer.parseInt(pageStr.replace(s, "")) : 0);
	}
	//NDC－apiの出版年月には「日」がないので、日付を1日として挿入するためのメソッド
	//nullチェックも行う
	private LocalDate datePrettier(String month) {
		String date = month + ".1";
		return (isMonth(month)? LocalDate.parse(date, fmt): LocalDate.now());
	}
	
	private boolean isMonth(String month) {
		 boolean flag = false;
		 //y.M.dの書式にあった文字列かどうか調べる
		 if(month != null) {
			  try{ //存在しない日(例 2017/02/29)の場合はfalseとする
			 flag = month.equals(fmt.format(fmt.parse(month)));
			 }catch(DateTimeParseException e){
				 e.printStackTrace();
			 }
		 }return flag;
	}
}
