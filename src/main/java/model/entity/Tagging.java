package model.entity;

import java.io.Serializable;

public class Tagging extends DTO implements Serializable{
	private Integer bookid;
	private Integer authorid;

	public Tagging() {
	}

	public Tagging(Integer bookid, Integer authorid) {

		this.bookid = bookid;
		this.authorid = authorid;
	}

	public Integer getBookid() {
		return bookid;
	}

	public Integer getAuthorid() {
		return authorid;
	}

}
