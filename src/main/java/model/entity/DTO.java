package model.entity;

import java.util.Objects;
import java.util.stream.Stream;

public interface DTO {
//Entityとなるクラスに実装して継承関係を明らかにするためのクラス
	//インスタンスにnullのフィールドが含まれていないときtrueを返す
		//参考:https://qiita.com/omochisama/items/7a10295e23be92c1db76
	public default boolean isNotEmpty() {
		java.lang.reflect.Field[] fields = DTO.class.getDeclaredFields();

		return Stream.of(fields)
				.map(this::getFieldValue)
				.noneMatch(Objects::isNull);
	}
	
	//isNotEmpty()の内部で使われている
	private Object getFieldValue(java.lang.reflect.Field field) {
		try {
			return field.get(this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
