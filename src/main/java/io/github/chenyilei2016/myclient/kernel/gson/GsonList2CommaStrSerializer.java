package io.github.chenyilei2016.myclient.kernel.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.zbycorp.fenghuo.domain.common.utils.StrUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chenyilei
 * @date 2023/05/11 11:09
 */
public class GsonList2CommaStrSerializer implements JsonSerializer<List> {
    @Override
    public JsonElement serialize(List src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(StrUtils.commaJoiner.join(src));
    }
}
