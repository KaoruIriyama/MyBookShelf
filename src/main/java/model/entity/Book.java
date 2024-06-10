package model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Book extends DTO implements Serializable{

	private int id;//INTEGER
	private String title;//VARCHAR(100)
	private LocalDate publishDate;//DATE
	private String publisher;//VARCHAR(50)
	private int pages;//INTEGER
	private BookISBN isbn;
	private String ndc;//日本十進分類表（Nippon Decimal Classification) VARCHAR (9)
	private int price;//INTEGER
	private LocalDateTime registationTime;//DATE
	private String comment;//TEXT
	private BookStatus status;// VARCHAR(5)
	private boolean favorite;//BOOLEAN

	/** JavaBeansの条件を満たすためのコンストラクタ
	 *  */
	public Book() {
	}

	/** APIから取得した情報を詰める用のコンストラクタ
	 * apiからデータが取れなかった時に備えてnullチェックと代替の値を用意する*/
	public Book(String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price, String comment) {
		Optional<String> optitle = Optional.ofNullable(title);
		Optional<LocalDate> opdate = Optional.ofNullable(publishDate);
		Optional<String> oppublisher = Optional.ofNullable(publisher);
		Optional<Integer> oppages = Optional.ofNullable(pages);
		
		Optional<String> opndc = Optional.ofNullable(ndc);
		Optional<Integer> opprice = Optional.ofNullable(price);
		Optional<String> opcomment = Optional.ofNullable(comment);

		this.title = optitle.orElse("無題");
		this.publishDate = opdate.orElse(LocalDate.now());
		this.publisher = oppublisher.orElse("不明");
		this.pages = oppages.orElse(0);
		this.isbn = new BookISBN(isbn);
		this.ndc = opndc.orElse("なし");
		this.price = opprice.orElse(0);
		this.comment = opcomment.orElse("なし");
	}

	/** テーブル登録用のコンストラクタ
	 *  */
	public Book(String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price,
			LocalDateTime registationTime, String comment) {

		this.title = title;
		this.publishDate = publishDate;
		this.publisher = publisher;
		this.pages = pages;
		this.isbn = new BookISBN(isbn);
		this.ndc = ndc;
		this.price = price;
		this.registationTime = registationTime;
		this.comment = comment;
	}

	/** テーブルからデータを取得する用のコンストラクタ
	 *  */
	public Book(int id, String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price,
			LocalDateTime registationTime, String comment, BookStatus status, boolean favorite) {

		this.id = id;
		this.title = title;
		this.publishDate = publishDate;
		this.publisher = publisher;
		this.pages = pages;
		this.isbn = new BookISBN(isbn);
		this.ndc = ndc;
		this.price = price;
		this.registationTime = registationTime;
		this.comment = comment;
		this.status = status;
		this.favorite = favorite;
	}
	
	/** テーブルからデータを取得する用のコンストラクタ
	 *  */
	public Book(int id, String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price,
			String comment, BookStatus status, boolean favorite) {

		this.id = id;
		this.title = title;
		this.publishDate = publishDate;
		this.publisher = publisher;
		this.pages = pages;
		this.isbn = new BookISBN(isbn);
		this.ndc = ndc;
		this.price = price;
		this.comment = comment;
		this.status = status;
		this.favorite = favorite;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public String getPublisher() {
		return publisher;
	}

	public int getPages() {
		return pages;
	}

	public String getISBN() {
		return this.isbn.getValue();
	}

	public String getNDC() {
		return ndc;
	}

	public int getPrice() {
		return price;
	}

	public LocalDateTime getRegistationTime() {
		return registationTime;
	}
	
	public String getComment() {
		return comment;
	}

	public BookStatus getStatus() {
		return status;
	}

	public boolean isFavorite() {
		return favorite;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment, favorite, id, isbn, ndc, pages, price, publishDate, publisher,
				registationTime, status, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		//ISBNと発行日が同じなら同じとみなす
		return Objects.equals(isbn, other.isbn) & Objects.equals(publishDate, other.publishDate);
	}

	@Override
	public String toString() {
		
		return "書名 " + title + ", 発行日 " + publishDate.toString()
				+ ", 出版者 " + publisher + ", ページ数 " + pages +
				", ISBN " + isbn.toString() + ", NDC分類 " + ndc+ ", 価格 " + price + 
				", コメント " + comment + "\n";
		
	}
}
