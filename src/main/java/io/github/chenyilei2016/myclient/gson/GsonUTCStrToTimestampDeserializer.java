package io.github.chenyilei2016.myclient.gson;

import cn.hutool.core.date.DateUtil;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 高性能的兼容 utc字符串 和 时间戳
 * 在非utc 和 时间戳的情况下, 使用多种格式兼容
 *
 * @author chenyilei
 * @date 2023/04/21 10:10
 */
public class GsonUTCStrToTimestampDeserializer implements JsonDeserializer<Long> {

    private static final DateTimeFormatter utcDateMilliSecondFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter utcDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json instanceof JsonPrimitive) {
            JsonPrimitive jsonPrimitive = (JsonPrimitive) json;
            if (jsonPrimitive.isNumber()) {
                return json.getAsLong();
            } else if (jsonPrimitive.isString()) {
                String str = jsonPrimitive.getAsString();
                //解析
                if (str.length() == 20) {
                    return Instant.from(utcDateFormatter.parse(str)).toEpochMilli();
                } else if (str.length() == 24) {
                    return Instant.from(utcDateMilliSecondFormatter.parse(str)).toEpochMilli();
                }
            }
        }
        return DateUtil.parse(json.getAsString()).getTime();
    }
}
