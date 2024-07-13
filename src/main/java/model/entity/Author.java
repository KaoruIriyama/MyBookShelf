package model.entity;

import java.io.Serializable;
import java.util.Objects;

public class Author implements DTO, Serializable{
	private int id = 0;
	private AuthorName name = new AuthorName("");
	private Profession profession = Profession.Author; //テーブル上ではINTEGERとして扱う

	/** JavaBeansの条件を満たすためのコンストラクタ*/
	public Author() {
	}

	/** APIから取得した情報を詰める用のコンストラクタ
	 *  apiからデータが取れなかった時に備えてnullチェックと代替の値を用意する*/
	public Author(String name, Profession profession) {
		

		this.name = new AuthorName(name);
		this.profession = profession;
	}

	/** テーブル登録用のコンストラクタ
	 *  */
	public Author(int id, String name, Profession profession) {
		this.id = id;
		this.name = new AuthorName(name);
		this.profession = profession;
	}

	public int getId() {
		return this.id;
	}

	public AuthorName getName() {
		return this.name;
	}

	public Profession getProfession() {
		return this.profession;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name.getValue(), profession);
	}

	@Override
	/**名前と職位が同じなら同じと見なす */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Author))
			return false;
		Author other = (Author) obj;
		return Objects.equals(name.getValue(), other.name.getValue()) && profession == other.profession;
	}

	public String toString() {
		
		return this.getName().getValue() + ":" + this.getProfession().getPFName() + "\n";
	}
	
}
