package model;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import model.entity.Book;
import model.entity.BookInfo;
import model.entity.Profession;

public class RSSParser {

	/** fetchRSS()で取得した書籍データをList<BookInfo>に加工して返すメソッド。
	 * RSSの読み取りにはjavax.xml.xpathパッケージを用いる*/
	
	public List<BookInfo> parseRSStoInfo(InputStream is) {

		List<BookInfo> infolist = new ArrayList<>();
		BookInfo info = new BookInfo();

		try {
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
				String title = (bookelement.getElementsByTagName("dc:title")).item(0).getTextContent();
				LocalDate publishDate = datePrettier(
						bookelement.getElementsByTagName("dcterms:issued").item(0).getTextContent());
				String publisher = bookelement.getElementsByTagName("dc:publisher").item(0).getTextContent();
				int pages = Integer.parseInt(
						(bookelement.getElementsByTagName("dc:extent")).item(0).getTextContent().replace("p", ""));

				String isbn = (bookelement.getElementsByTagName("dc:identifier")).item(0)
						.getTextContent().replace("-", "");

				int price = Integer.parseInt(
						bookelement.getElementsByTagName("dcndl:price").item(0).getTextContent().replace("円", ""));
				//ndcと補足情報のキーワードは同じタグに詰められていたのでここで一括処理
				NodeList subjects = bookelement.getElementsByTagName("dc:subject");
				String ndc = null;
				List<String> subs = new ArrayList<>();
				for (int j = 0; j < subjects.getLength(); j++) {
					Element e = (Element) subjects.item(j);
					if (e.hasAttribute("xsi:type")) {
						if (e.getAttribute("xsi:type").contains("dcndl:NDC")) {
							ndc = e.getTextContent();
						}
					} else {
						subs.add(e.getTextContent());
					}
				}
				//commentには文庫やシリーズの情報を詰める(在れば)
				String series = "";
				String volume = "";
				String desc = "";
				Node node_series = bookelement.getElementsByTagName("dcndl:seriesTitle").item(0);
				Node node_volume = bookelement.getElementsByTagName("dcndl:volume").item(0);
				NodeList node_desc = bookelement.getElementsByTagName("dc:description");
				if (node_series != null) {
					series = node_series.getTextContent();
				}
				if (node_volume != null) {
					volume = node_volume.getTextContent() + "巻";
				}
				if (node_desc != null) {
					for (int j = 0; j < subjects.getLength(); j++) {
						desc += node_desc.item(j).getTextContent() + " ";
					}
				}

				Optional<String> sub = Optional.ofNullable(subs.stream().toString());
				String comment = series + " " + volume + " " +
						sub.orElse("") + " " + desc;

				List<Author> alist = new ArrayList<>();
				Book book = new Book(title, publishDate, publisher, pages, isbn, ndc, price, comment);
				NodeList creators = bookelement.getElementsByTagName("dc:creator");
				for (int k = 0; k < creators.getLength(); k++) {
					String name = SearchBookLogic.nameParser(creators.item(k).getTextContent());
					//createrの数だけ新しい著者インスタンスをlistに詰める
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

	private LocalDate datePrettier(String month) {
		String date = month + ".1";
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.M.d");
		return LocalDate.parse(date, fmt);
	}
}
