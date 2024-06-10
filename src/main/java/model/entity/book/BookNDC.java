package model.entity.book;

import java.io.Serializable;
import java.util.Objects;

public class BookNDC implements Serializable{
	private String ndc = "なし";

	public BookNDC(String ndc) {
		if(lengthCheck(ndc)) {this.ndc = ndc;}
	}

	public String getNdc() {
		return ndc;
	}
	
	private boolean lengthCheck(String ndc) {
		return Objects.nonNull(ndc) & ndc.length() <= 9;
	}
}