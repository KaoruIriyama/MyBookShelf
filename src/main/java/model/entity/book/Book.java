package model.entity.book;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import model.entity.DTO;

public class Book implements DTO, Serializable{

	private int id = 0;//INTEGER
	private BookTitle title = new BookTitle("無題");
	private LocalDate publishDate = LocalDate.now();//DATE
	private BookPublisher publisher = new BookPublisher("不明");
	private int pages = 0;//INTEGER
	private BookISBN isbn = new BookISBN("なし");
	private BookNDC ndc = new BookNDC("なし");
	private int price = 0;//INTEGER
	private LocalDateTime registationTime = LocalDateTime.now();//DATE
	private String comment = "なし";//TEXT
	private BookStatus status = BookStatus.Unread;// VARCHAR(5)
	private boolean favorite =  false;//BOOLEAN

	/** JavaBeansの条件を満たすためのコンストラクタ
	 *  */
	public Book() {
	}

	/** APIから取得した情報を詰める用のコンストラクタ
	 * apiからデータが取れなかった時に備えてnullチェックと代替の値を用意する*/
	public Book(String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price, String comment) {
		
		Optional<LocalDate> opdate = Optional.ofNullable(publishDate);
		Optional<Integer> oppages = Optional.ofNullable(pages);
		Optional<Integer> opprice = Optional.ofNullable(price);
		Optional<String> opcomment = Optional.ofNullable(comment);

		this.title = new BookTitle(title);
		this.publishDate = opdate.orElse(LocalDate.now());
		this.publisher = new BookPublisher(publisher);
		this.pages = oppages.orElse(0);
		this.isbn = new BookISBN(isbn);
		this.ndc  = new BookNDC(ndc);
		this.price = opprice.orElse(0);
		this.comment = opcomment.orElse("なし");
	}

	/** テーブル登録用のコンストラクタ
	 *  */
	public Book(String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price,
			LocalDateTime registationTime, String comment) {

		new Book(title, publishDate, publisher,
				pages, isbn, ndc, price, comment);
		
		this.registationTime = registationTime;
	}

	/** テーブルからデータを取得する用のコンストラクタ
	 *  */
	public Book(int id, String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price,
			LocalDateTime registationTime, String comment, BookStatus status, boolean favorite) {
		
		new Book(id, title, publishDate, publisher,
				pages, isbn, ndc, price,
				comment, status, favorite);
		
		this.registationTime = registationTime;
	}
	
	/** テーブルからデータを取得する用のコンストラクタ
	 *  */
	public Book(int id, String title, LocalDate publishDate, String publisher,
			int pages, String isbn, String ndc, int price,
			String comment, BookStatus status, boolean favorite) {

		new Book(title, publishDate, publisher,
				pages, isbn, ndc, price, comment);
		this.id = id;
		this.status = status;
		this.favorite = favorite;
	}

	public int getId() { return this.id; }

	public String getTitle() { return title.getTitle(); }

	public LocalDate getPublishDate() {	return publishDate;	}

	public String getPublisher() { return publisher.getPublisher();	}

	public int getPages() {	return pages; }

	public String getISBN() { return this.isbn.getValue(); }

	public String getNDC() { return ndc.getNdc(); }

	public int getPrice() {	return price;}

	public LocalDateTime getRegistationTime() {	return registationTime;	}
	
	public String getComment() {return comment;}

	public BookStatus getStatus() {	return status;}

	public boolean isFavorite() {return favorite;}

	@Override
	public int hashCode() {
		return Objects.hash(comment, favorite, id, isbn, ndc.getNdc(), pages, price, publishDate, publisher.getPublisher(),
				registationTime, status, title.getTitle());
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
		
		return "書名 " + title.getTitle() + ", 発行日 " + publishDate.toString()
				+ ", 出版者 " + publisher.getPublisher() + ", ページ数 " + pages +
				", ISBN " + isbn.toString() + ", NDC分類 " + ndc.getNdc()+ ", 価格 " + price + 
				", コメント " + comment + "\n";
		
	}
}
