package model.entity.book;

import java.io.Serializable;
import java.util.Objects;

public class BookISBN implements Serializable{
	public String isbn = "なし";

	public BookISBN() {
	}
	
	public BookISBN(String isbn) {
		if(lengthCheck(isbn)) {
			if(isbnCheck(isbn)) {
				this.isbn = removeHyphen(isbn);
			}
		}
	}
	
	public String getValue() {return this.isbn;}
	
//	public void setValue(String data) {this.isbn = data;}
	
	public static boolean isbnCheck(String isbn) {
		// isbn-13 計算ルール 参考: https://isbn.jpo.or.jp/index.php/fix__calc_isbn/
		// ISBN-10 参考: https://ja.wikipedia.org/wiki/ISBN
		char[] iArray = removeHyphen(isbn).toCharArray();
		//		    System.out.println(iArray);
		if (isISBN13(isbn, iArray) ) {
		
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
				
				return isCorrectlyISBN13(iArray, checker_13c);
			
		} else if (isISBN10(iArray)) {
			//ISBN-10 
			int checker_ten = 0;
			for (int i = 0; i < 9; i++) {
				//チェックディジットを除いた左側の桁から10、9、8…2を掛けてそれらの和を取る
				checker_ten += Character.getNumericValue(iArray[i]) * (10 - i);
			}
			return isCorrectlyISBN10(iArray, checker_ten);
		}else {return false;} 
	}

	private static boolean isISBN13(String isbn, char[] iArray) {
		return iArray.length == 13 && 
				(isbn.startsWith("978") || isbn.startsWith("979"));
	}
	// (10 - Cの下1桁(Cを10で割った余り)) = isbn最終桁であるなら真
	private static boolean isCorrectlyISBN13(char[] iArray, int checker_13c) {
		return 10 - checker_13c % 10 == Character.getNumericValue(iArray[12]);
	}
	
	private static boolean isISBN10(char[] iArray) {
		return iArray.length == 10;
	}
	//和を11で割った余りをisbnの最終桁と比べる
	//ISBN-10 のチェックデジットが0～9又は11の場合は最終桁と同値になるはず
	private static boolean isCorrectlyISBN10(char[] iArray, int checker_ten) {
		int sample = 11 - checker_ten % 11;
		return sample == Character.getNumericValue(iArray[9]) || 
				(sample == 10 && iArray[9] == 'X');
	}

	static String removeHyphen(String isbn) {
		return isbn.replace("-", "");
	}

	private boolean lengthCheck(String isbn) {
		return Objects.nonNull(isbn) & isbn.length() <= 15;
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}

}