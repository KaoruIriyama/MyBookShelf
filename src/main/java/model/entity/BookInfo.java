package model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** データベースのBOOK＿AUTHORビューのレコードに対応するクラス*/
public class BookInfo extends DTO implements Serializable{
	private Book book;
	private List<Author> authors = new ArrayList<>();

	public BookInfo(Book b, Author au) {
		this.book = b;
		this.authors.add(au);
	}

	public BookInfo(Book b, List<Author> au) {
		this.book = b;
		if (au.size() == 1) {
			this.authors.add(au.get(0));
		} else {
			this.authors.addAll(au);
		}
	}

	public BookInfo() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static List<BookInfo> retributeList(List<BookInfo> temp) {
		List<BookInfo> infolist = new ArrayList<>();
		// int j = 0;
		for (int j = 0; j < temp.size(); j++) {
			BookInfo info = temp.get(j);
			//(今の要素と次の要素を比べて、BOOKが同じかつAUTHORが異なる場合のみリストに代入)
			for (int k = j; k < temp.size() - 1; k++) {
				if (temp.get(j + 1).getBook().equals(temp.get(j).getBook())) {
					info.addAuthor(temp.get(j + 1).getAuthors().get(0));
					j++;
				}
			}
			infolist.add(info);
		}
		return infolist;
	}

	//retributeListの逆
	public static List<BookInfo> antitributeList(List<BookInfo> listed) {
		List<BookInfo> infolist = new ArrayList<>();
		for (int j = 0; j < listed.size(); j++) {
			Book book = listed.get(j).getBook();
			for (int k = 0; k < listed.get(j).getAuthors().size(); k++) {
				Author author = listed.get(j).getAuthors().get(k);
				BookInfo tagged = new BookInfo(book, author);
				infolist.add(tagged);
			}
		}
		return infolist;
	}

	public void addAuthor(Author au) {
		this.authors.add(au);
	}

	public Book getBook() {
		return book;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authors, book);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BookInfo))
			return false;
		BookInfo other = (BookInfo) obj;
		return Objects.equals(authors, other.authors) && Objects.equals(book, other.book);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Author a : this.authors) {
			sb.append(a.toString()).append(" ");
		}
		return this.book + ":" + sb.toString();
	}
}
