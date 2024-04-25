package model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Book extends DTO implements Serializable{

	private int id;
	private String title;
	private LocalDate publishDate;
	private String publisher;
	private int pages;
	private String isbn;
	private String ndc;//日本十進分類表（Nippon Decimal Classification)
	private int price;
	private LocalDateTime registationTime;
	private String comment;
	private BookStatus status;
	private boolean favorite;

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
		Optional<String> opisbn = Optional.ofNullable(isbn);
		Optional<String> opndc = Optional.ofNullable(ndc);
		Optional<Integer> opprice = Optional.ofNullable(price);
		Optional<String> opcomment = Optional.ofNullable(comment);

		this.title = optitle.orElse("無題");
		this.publishDate = opdate.orElse(LocalDate.now());
		this.publisher = oppublisher.orElse("不明");
		this.pages = oppages.orElse(0);
		this.isbn = opisbn.orElse("なし");
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
		this.isbn = isbn;
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
		this.isbn = isbn;
		this.ndc = ndc;
		this.price = price;
		this.registationTime = registationTime;
		this.comment = comment;
		this.status = status;
		this.favorite = favorite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	//	public List<Author> getAuthor() {return author;}
	//
	//	public void setAuthor(List<Author> author) {this.author = author;}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getISBN() {
		return isbn;
	}

	public void setISBN(String isbn) {
		this.isbn = isbn;
	}

	public String getNDC() {
		return ndc;
	}

	public void setNDC(String ndc) {
		this.ndc = ndc;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public LocalDateTime getRegistationTime() {
		return registationTime;
	}

	public void setRegistationTime(LocalDateTime registationTime) {
		this.registationTime = registationTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
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
		//ISBNが同じなら同じとみなす
		return Objects.equals(isbn, other.isbn);
	}

	@Override
	public String toString() {
		return "title=" + title + ", publishDate=" + publishDate.toString()
				+ ",  publisher=" + publisher + ", pages=" + pages +
				", isbn=" + isbn + ", ndc=" + ndc+ ", price=" + price + 
				", regisitation_time=" + registationTime.toString() + 
				",  favorite=" + favorite + ", status=" + status + ", comment=" + comment + "\n";
		
	}
}
