package com.brook.example.utils.mapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 * 兼容Java 8
 * @author liushaojun
 */
@Slf4j
public final class JsonMapper {

  /**
   * 单利模式
   */
	public static final JsonMapper INSTANCE = new JsonMapper();

	private static ObjectMapper mapper ;

	private JsonMapper() {
		this(null);
	}

	private JsonMapper(Include include) {
		mapper = new ObjectMapper();
		// 设置输出时包含属性的风格
		if (include != null) {
			mapper.setSerializationInclusion(include);
		}
		mapper.setTimeZone(TimeZone.getDefault());
		mapper.registerModule(new Jdk8Module());
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * 创建只输出非Null的属性到Json字符串的Mapper.
	 */
	public static JsonMapper nonNullMapper() {
		return new JsonMapper(Include.NON_NULL);
	}

	/**
	 * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
	 * <p>
	 * 注意，要小心使用, 特别留意empty的情况.
	 */
	public static JsonMapper nonEmptyMapper() {
		return new JsonMapper(Include.NON_EMPTY);
	}

	/**
	 * 默认的全部输出的Mapper, 区别于INSTANCE，可以做进一步的配置
	 */
	public static JsonMapper defaultMapper() {
		return new JsonMapper();
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
	 */
	public static JsonMapper nonDefaultMapper() {
		return new JsonMapper(Include.NON_DEFAULT);
	}

	/**
	 * Object可以是POJO，也可以是Collection或数组。
	 * 如果对象为Null, 返回"null".
	 * 如果集合为空集合, 返回"[]".
	 */
	public String toJson(Object object) {

		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			log.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * 反序列化POJO或简单Collection如List<String>.
	 * 
	 * 如果JSON字符串为Null或"null"字符串, 返回Null.
	 * 如果JSON字符串为"[]", 返回空集合.
	 * 
	 * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
	 * 
	 * @see #fromJson(String, JavaType)
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			log.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 反序列化复杂{@code Collection} 如{@code List<Bean>},
	 * 先使用 {@link #buildCollectionType(Class, Class)}
	 * 或 {@link #buildMapType(Class, Class, Class)}构造类型, 然后调用本函数.
	 * 
	 * @see #buildCollectionType(Class, Class)
	 */
	public  Object fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			log.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 反序列化
	 * ```
	 * fromJson("[{name:"json1","name":"json2"}]",new TypeReference<List<Entity>>(){
	 *
	 * });
	 * ```
	 * @param jsonString json数据
	 * @param typeReference 类型引用
	 * @param <T>
     * @return
     */
	public <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return  mapper.readValue(jsonString, typeReference);
		} catch (IOException e) {
			log.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 构造Collection类型.
	 */
	public JavaType buildCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}

	/**
	 * 构造Map类型.
	 */
	public JavaType buildMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
		return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
	}

	/**
	 * 当JSON里只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
	 */
	public void update(String jsonString, Object object) {
		try {
			mapper.readerForUpdating(object).readValue(jsonString);
		} catch (IOException e) {
			log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
	}

	/**
	 * 輸出JSONP格式數據.
	 */
	public String toJsonp(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}

	/**
	 * 設定是否使用Enum的toString函數來讀寫Enum,
	 * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
	 * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
	 */
	public void enableEnumUseToString() {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
	}



	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}


	public static boolean isJSON(String string) {
		try {
			INSTANCE.getMapper().readTree(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static <T> boolean isJSON(String string, Class<T> clazz) {
		if (StringUtils.isEmpty(string)) {
			return false;
		}

		try {
			INSTANCE.getMapper().readValue(string, clazz);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}