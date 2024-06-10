package model.entity;

import java.io.Serializable;

public enum Profession implements Serializable{
	Author("著者"), Translater("訳者"), Editer("編者"), Writer("筆記者"), StoryTeller("原作者"), Artist("作画者"), Other("その他");

	private String pf_name;

	private Profession(String pf_name) {
		this.pf_name = pf_name;
	}

	public String getPFName() {
		return this.pf_name;
	}
	
	public static Profession getByPFName(String pfname) {
	    // nameから enum 定数を特定して返す処理
	    for (Profession value : Profession.values()) {
	      if (value.getPFName().equals(pfname)) {
	        return value;
	      }
	    }
	    return null; // 特定できない場合
	  }
//	順番からenum 定数を特定して返す処理
	public static Profession getByOrder(int order) {
		for (Profession value : Profession.values()) {
		      if (value.ordinal() == order) {
		        return value;
		      }
		    }
		return null; // 特定できない場合
	}
}
