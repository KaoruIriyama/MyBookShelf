package model.entity;

import java.io.Serializable;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class AuthorName implements Serializable{
	private String name = "";

	public AuthorName() {
	}
	
	public AuthorName(String data) {
		Optional<String> opname = Optional.ofNullable(data);
		if(checkLength(data)){
			this.name = nameParser(opname.orElse(""));
		}
	}

	public String getValue() {
		return this.name;
	}
	
	public boolean checkLength(String data) {
		return data.length() <= 50;
	}
	
	//apiで返ってきた著者名を姓 名の並びに変換する
	private String nameParser(String name) {
		String result = "";
		
		
			if(name.contains(",")){
//				前後を分解する(姓・名の分離)
				result = arrangeName(name, name.split(","));
			}else {
				result = name;
			}
		
		return result;
	}

	public static boolean isHankaku(String name) {
		return StringUtils.isAlphanumeric(name) || StringUtils.isAsciiPrintable(name);
	}

	private String arrangeName(String name, String[] splited) {
		//名前が半角(英字)かどうかを判定
		if (isHankaku(name)) {
			//				半角かつ、名前に","があれば、後ろ・前の順に並べる
			 return splited[1].replaceAll(" ", "") + " " + splited[0].replaceAll(" ", "");
		} else {
			//				全角ならばそのまま並べる
			return splited[0].replaceAll(" ", "") + " " + splited[1].replaceAll(" ", "");
		}
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}
	
	
}