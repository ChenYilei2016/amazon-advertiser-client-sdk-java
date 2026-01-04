package io.github.chenyilei2016.amznadclient.kernel.gson;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使gson支持{@link JsonSerializeNullable}
 * 使gson支持{@link NullableFieldSerialParent}
 * <p>
 * 被此注解标记的字段即使为null也输出
 *
 * @author chenyilei
 * @date 2023/06/09 13:56
 */
public class GsonNullableAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        List<String> nullableFieldNames = new ArrayList<>();
        List<String> nonNullableFieldNames = new ArrayList<>();

        ReflectionUtils.doWithFields(type.getRawType(), declaredField -> {
            if (declaredField.isAnnotationPresent(JsonSerializeNullable.class)) {
                if (declaredField.getAnnotation(SerializedName.class) != null) {
                    nullableFieldNames.add(declaredField.getAnnotation(SerializedName.class).value());
                } else {
                    nullableFieldNames.add(declaredField.getName());
                }
            } else {
                if (declaredField.getAnnotation(SerializedName.class) != null) {
                    nonNullableFieldNames.add(declaredField.getAnnotation(SerializedName.class).value());
                } else {
                    nonNullableFieldNames.add(declaredField.getName());
                }
            }
        });

        JsonSerializeNullable annotation = AnnotationUtils.findAnnotation(type.getRawType(), JsonSerializeNullable.class);

        if (nullableFieldNames.size() == 0 && annotation == null) {
            return null;
        }

        TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(GsonNullableAdapterFactory.this, type);
        TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                List<String> nullableFieldNamesInParent = null;

                if (value instanceof NullableFieldSerialParent) {
                    nullableFieldNamesInParent = ((NullableFieldSerialParent) value).getNullableFieldNames();
                }

                //不是空的字段, 如果为空则去除
                JsonObject jsonObject = delegateAdapter.toJsonTree(value).getAsJsonObject();
                for (String name : nonNullableFieldNames) {
                    if (jsonObject.has(name) && jsonObject.get(name) instanceof JsonNull) {
                        if (nullableFieldNamesInParent != null && nullableFieldNamesInParent.contains(name)) {
                            continue;
                        }
                        jsonObject.remove(name);
                    }
                }
                out.setSerializeNulls(true);
                elementAdapter.write(out, jsonObject);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                return delegateAdapter.read(in);
            }

        };
    }
}