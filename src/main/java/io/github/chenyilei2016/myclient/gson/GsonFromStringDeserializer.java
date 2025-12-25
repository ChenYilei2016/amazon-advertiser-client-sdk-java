package io.github.chenyilei2016.myclient.gson;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * 让字段返序列化认为是一个string
 *
 * @author chenyilei
 * @date 2023/04/12 16:21
 */
public class GsonFromStringDeserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonPrimitive) {
            return json.getAsString();
        }
        return json.toString();
    }
}
