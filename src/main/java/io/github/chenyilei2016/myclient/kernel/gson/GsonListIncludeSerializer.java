package io.github.chenyilei2016.myclient.kernel.gson;

import cn.hutool.core.collection.CollectionUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chenyilei
 * @formatter:off =>
 * {
 * "include" :[
 * <p>
 * ]
 * }
 * @formatter:on
 * @date 2023/04/06 09:38
 */
public class GsonListIncludeSerializer implements JsonSerializer<List> {

    public static class ListIncludeWrapper {
        @Expose
        @Getter
        @Setter
        private List include;

        public ListIncludeWrapper(List include) {
            this.include = include;
        }
    }

    @Override
    public JsonElement serialize(List src, Type typeOfSrc, JsonSerializationContext context) {
        if (CollectionUtil.isEmpty(src)) {
            return context.serialize(src);
        }
        return context.serialize(new ListIncludeWrapper(src));
    }
}
