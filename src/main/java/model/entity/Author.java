package model.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class Author extends DTO implements Serializable{
	private int id;
	private AuthorName name;
	private Profession profession; //テーブル上ではINTEGERとして扱う

	/** JavaBeansの条件を満たすためのコンストラクタ*/
	public Author() {
	}

	/** APIから取得した情報を詰める用のコンストラクタ
	 *  apiからデータが取れなかった時に備えてnullチェックと代替の値を用意する*/
	public Author(String name, Profession profession) {
		
		Optional<Profession> opprof = Optional.ofNullable(profession);

		this.name = new AuthorName(name);
		this.profession = opprof.orElse(Profession.Author);
	}

	/** テーブル登録用のコンストラクタ
	 *  */
	public Author(int id, String name, Profession profession) {
		this.id = id;
		this.name.setName(name);
		this.profession = profession;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name.getName();
	}

	public Profession getProfession() {
		return profession;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name.getName(), profession);
	}

	@Override
	/**名前と職位が同じなら同じと見なす */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Author))
			return false;
		Author other = (Author) obj;
		return Objects.equals(name.getName(), other.name.getName()) && profession == other.profession;
	}

	public String toString() {
		Optional<String> opname = Optional.ofNullable(this.name.getName());
		Optional<Profession> opprof = Optional.ofNullable(this.profession);
		String showname = opname.orElse("");
		Profession showprofession = opprof.orElse(Profession.Author);
		return showname + ":" + showprofession.getPFName() + "\n";
	}


	//authorインスタンスにnullのフィールドが含まれていないときtrueを返す
	//参考:https://qiita.com/omochisama/items/7a10295e23be92c1db76
	
}
