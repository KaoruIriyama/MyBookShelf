package model.entity.book;

import java.io.Serializable;
import java.util.Objects;

public class BookPublisher implements Serializable{
	private String publisher = "不明";

	public BookPublisher(String publisher) {
		if(lengthCheck(publisher)) {this.publisher = publisher;}
	}

	public String getPublisher() {
		return publisher;
	}
	
	private boolean lengthCheck(String publisher) {
		return Objects.nonNull(publisher) & publisher.length() <= 50;
	}
}