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
		this.isbn = removeHyphen(opisbn.orElse("なし"));
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
		this.isbn = removeHyphen(isbn);
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
		this.isbn = removeHyphen(isbn);
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
		this.isbn = removeHyphen(isbn);
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
		return isbn;
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
		//ISBNが同じなら同じとみなす
		return Objects.equals(isbn, other.isbn);
	}

	@Override
	public String toString() {
		
		return "書名 " + title + ", 発行日 " + publishDate.toString()
				+ ", 出版者 " + publisher + ", ページ数 " + pages +
				", ISBN " + isbn + ", NDC分類 " + ndc+ ", 価格 " + price + 
				", コメント " + comment + "\n";
		
	}

	public static boolean isbnCheck(String isbn) {
		// isbn-13 計算ルール 参考: https://isbn.jpo.or.jp/index.php/fix__calc_isbn/
		// ISBN-10 参考: https://ja.wikipedia.org/wiki/ISBN
		boolean flag = false;
		char[] iArray = removeHyphen(isbn).toCharArray();
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
				//System.out.println(checker_13A + ":" + checker_13B + ":" + checker_13c);
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

	static String removeHyphen(String isbn) {
		return isbn.replace("-", "");
	}
}
