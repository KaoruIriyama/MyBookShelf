package model.entity;

import java.io.Serializable;

public enum BookStatus implements Serializable{
	Unread("未読"), Finished("既読"), Reading("読書中");

	private String name;

	private BookStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public static BookStatus getByName(String name) {
	    // 値から enum 定数を特定して返す処理
	    for (BookStatus value : BookStatus.values()) {
	      if (value.getName().equals(name)) {
	        return value;
	      }
	    }
	    return null; // 特定できない場合
	  }
//	順番からenum 定数を特定して返す処理
	public static BookStatus getByOrder(int order) {
		for (BookStatus value : BookStatus.values()) {
		      if (value.ordinal() == order) {
		        return value;
		      }
		    }
		    return null; // 特定できない場合
	}
}
