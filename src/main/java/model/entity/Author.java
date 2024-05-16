package model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class Author extends DTO implements Serializable{
	private int id;
	private String name;
	private Profession profession; //テーブル上ではINTEGERとして扱う

	/** JavaBeansの条件を満たすためのコンストラクタ*/
	public Author() {
	}

	/** APIから取得した情報を詰める用のコンストラクタ
	 *  apiからデータが取れなかった時に備えてnullチェックと代替の値を用意する*/
	public Author(String name, Profession profession) {
		Optional<String> opname = Optional.ofNullable(name);
		Optional<Profession> opprof = Optional.ofNullable(profession);

		this.name = opname.orElse("");
		this.profession = opprof.orElse(Profession.Author);
	}

	/** テーブル登録用のコンストラクタ
	 *  */
	public Author(int id, String name, Profession profession) {
		this.id = id;
		this.name = name;
		this.profession = profession;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Profession getProfession() {
		return profession;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, profession);
	}

	@Override
	/**名前と職位が同じなら同じと見なす */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Author))
			return false;
		Author other = (Author) obj;
		return Objects.equals(name, other.name) && profession == other.profession;
	}

	public String toString() {
		Optional<String> opname = Optional.ofNullable(this.name);
		Optional<Profession> opprof = Optional.ofNullable(this.profession);
		String showname = opname.orElse("");
		Profession showprofession = opprof.orElse(Profession.Author);
		return showname + ":" + showprofession.getPFName() + "\n";
	}

	//apiで返ってきた著者名を姓 名の並びに変換する
	public static String nameParser(String name) {
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

	//authorインスタンスにnullのフィールドが含まれていないときtrueを返す
	//参考:https://qiita.com/omochisama/items/7a10295e23be92c1db76
	
}
