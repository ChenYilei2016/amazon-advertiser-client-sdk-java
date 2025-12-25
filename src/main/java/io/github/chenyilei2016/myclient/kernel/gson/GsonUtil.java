package io.github.chenyilei2016.myclient.kernel.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * @author chenyilei
 * @since 2025/12/25 13:58
 */
public class GsonUtil {
    /**
     * GSON = new GsonBuilder()
     * .enableComplexMapKeySerialization() // 当Map的key为复杂对象时,需要开启该方法
     * .setDateFormat("yyyy-MM-dd HH:mm:ss") // 序列化日期格式
     * .disableHtmlEscaping() // 防止特殊字符出现乱码
     * .create();
     *
     * GSON_NULL = new GsonBuilder()
     * .enableComplexMapKeySerialization()
     * .serializeNulls() // 当字段值为空或null时，依然对该字段进行转换
     * .setDateFormat("yyyy-MM-dd HH:mm:ss")
     * .disableHtmlEscaping()
     * .create();
     * }
     */
    static Gson gson = new GsonBuilder().create();

    // 将字符串转化为对象
    public static <T> T parseObject(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    // 将JSON字符串转化为对应的实体对象
    public static <T> T parseObject(String json, TypeToken<T> typeOfT) {
        return gson.fromJson(json, typeOfT.getType());
    }

    // 转换为List
    public static <T> List<T> strToList(String gsonString, Class<T> cls) {
        return gson.fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
    }

    // 转换为包含Map的List
    public static <T> List<Map<String, T>> strToListMaps(String gsonString) {
        return gson.fromJson(gsonString, new TypeToken<List<Map<String, String>>>() {}.getType());
    }

    // 转换为Map
    public static <T> Map<String, T> strToMaps(String gsonString) {
        return gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {}.getType());
    }

    public static String toJsonString(Object src) {
        return gson.toJson(src);
    }
}
