package model.entity.book;

import java.io.Serializable;
import java.util.Objects;

public class BookTitle implements Serializable{
	private String title = "無題";
	
	public BookTitle() {
	}

	public BookTitle(String title) {
		if(lengthCheck(title)) {this.title = title;}
	}

	public String getTitle() {
		return title;
	}
	
	private boolean lengthCheck(String title) {
		return Objects.nonNull(title) & title.length() <= 100;
	}
}