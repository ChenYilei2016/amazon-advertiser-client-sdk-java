package io.github.chenyilei2016.myclient.kernel.gson;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.utils.LambdaFunction;
import io.github.chenyilei2016.myclient.kernel.utils.MyLambdaUtils;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 让其中的字段序列化为 null 传出 , 达到置空的效果
 *
 * @author chenyilei
 * @date 2023/06/20 14:42
 */
public abstract class NullableFieldSerialParent {
    @Getter
    public transient List<String> nullableFieldNames;

    public <T extends NullableFieldSerialParent, R> void addNullableField(LambdaFunction<T, R> function) {
        if (null == function) {
            return;
        }

        String[] names = new String[1];
        names[0] = MyLambdaUtils.getFieldName(function);
        addNullableFieldNames(names);
    }


    public void addNullableFieldNames(String... names) {
        if (nullableFieldNames == null) {
            this.nullableFieldNames = Lists.newArrayList();
        }

        if (null == names) {
            return;
        }

        for (String name : names) {
            Field field = ReflectUtil.getField(this.getClass(), name);
            String finalFieldName = name;

            if (field != null) {
                field.setAccessible(true);
                SerializedName annotation = field.getAnnotation(SerializedName.class);
                if (annotation != null) {
                    finalFieldName = annotation.value();
                }
            }
            this.nullableFieldNames.add(finalFieldName);
        }
    }
}
