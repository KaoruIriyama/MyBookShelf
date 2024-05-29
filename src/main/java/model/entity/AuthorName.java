package model.entity;

import java.io.Serializable;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class AuthorName implements Serializable{
	private String name = "";

	public AuthorName() {
	}
	
	public AuthorName(String data) {
		if(checkLength(data)){
			Optional<String> opname = Optional.ofNullable(name);
			this.name = nameParser(opname.orElse(""));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean checkLength(String data) {
		return data.length() <= 50;
	}
	
	//apiで返ってきた著者名を姓 名の並びに変換する
	private String nameParser(String name) {
		//		最初に"　"などの文字を" "に置換(スペースは正規表現で指定)
		name = name.replaceAll("[\\h]", "");
		String result = "";
	
		if (name.contains(",")) {
			//			","で前後を分解する(姓・名の分離)
			String[] splited = name.split(",");
			//名前が半角(英字)かどうかを判定
			if (StringUtils.isAlphanumeric(name) || StringUtils.isAsciiPrintable(name)) {
				//				半角かつ、名前に","があれば、後ろ・前の順に並べる
				result = splited[1] + " " + splited[0];
			} else {
				//				全角ならばそのまま並べる
				result = splited[0] + " " + splited[1];
			}
		} else {
			result = name;
		}
		return result;
	}
}