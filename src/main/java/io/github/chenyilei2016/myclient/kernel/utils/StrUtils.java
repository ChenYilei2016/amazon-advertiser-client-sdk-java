package io.github.chenyilei2016.myclient.kernel.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.chenyilei2016.myclient.kernel.utils.enums.CommonDatePatternEnum;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author chenyilei
 * @date 2022/08/25 14:13
 */
public class StrUtils {
    private static final Cache<String, String> camel2UpperUnderLineCachedMap = CacheBuilder.newBuilder().maximumSize(2000).build();

    private static final Cache<String, String> upperUnderLine2CamelCachedMap = CacheBuilder.newBuilder().maximumSize(2000).build();

    public static Joiner commaJoiner = Joiner.on(",").skipNulls();

    public static Splitter commaSplitter = Splitter.on(",").omitEmptyStrings().trimResults();

    public static Splitter hashSplitter = Splitter.on("#").omitEmptyStrings().trimResults();

    /**
     * @param strDate        传入的时间 字符串格式
     * @param dateFormat     预期的时间格式
     * @param setFormatValue 调整时间参数的方法
     */
    public static void expected(String strDate, CommonDatePatternEnum dateFormat, Consumer<String> setFormatValue) {
        if (StringUtils.isBlank(strDate)) {
            return;
        }
        if (dateFormat.getPattern().matcher(strDate).find()) {
            return;
        }
        //尝试各种时间格式
        DateTime adapterDate = DateUtil.parse(strDate);

        setFormatValue.accept(DateUtil.format(adapterDate, dateFormat.getCode()));
    }

    public static String expected(String strDate, CommonDatePatternEnum dateFormat) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }
        if (dateFormat.getPattern().matcher(strDate).find()) {
            return strDate;
        }
        //尝试各种时间格式
        DateTime adapterDate = DateUtil.parse(strDate);

        return DateUtil.format(adapterDate, dateFormat.getCode());
    }


    @SneakyThrows
    public static String camel2UpperUnderLineCached(String str) {
        if (null == str) {
            return null;
        }
        return camel2UpperUnderLineCachedMap.get(str, () -> {
            String underlineCase = StrUtil.toUnderlineCase(str);
            return underlineCase.toUpperCase();
        });
    }

    /**
     * 大写下划线 -> 小写驼峰
     */
    @SneakyThrows
    public static String upperUnderLine2CamelCached(String str) {
        if (null == str) {
            return null;
        }
        return upperUnderLine2CamelCachedMap.get(str, () -> {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
        });
    }

    public static void upperUnderLine2CamelCached(List<String> strList) {
        if (strList == null) {
            return;
        }
        for (int i = 0; i < strList.size(); i++) {
            strList.set(i, StrUtils.upperUnderLine2CamelCached(strList.get(i)));
        }
    }

    public static void camel2UpperUnderLineCached(List<String> strList) {
        if (strList == null) {
            return;
        }
        for (int i = 0; i < strList.size(); i++) {
            strList.set(i, StrUtils.camel2UpperUnderLineCached(strList.get(i)));
        }
    }

    public static String join(Collection<?> list, String separator, String itemPrefix, String itemSuffix) {
        StringBuilder sb = new StringBuilder();
        if (itemPrefix != null) {
            sb.append(itemPrefix);
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        int size = list.size();
        CollectionUtil.forEach(list, (o, i) -> {
            sb.append(o);
            if (i != size - 1) {
                sb.append(separator);
            }
        });
        if (itemPrefix != null) {
            sb.append(itemSuffix);
        }
        return sb.toString();
    }

    public static String itemJoin(Collection<?> list, String separator, String itemPrefix, String itemSuffix) {
        if (CollectionUtil.isEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        int size = list.size();
        CollectionUtil.forEach(list, (o, i) -> {
            if (itemPrefix != null) {
                sb.append(itemPrefix);
            }
            sb.append(o);
            if (itemSuffix != null) {
                sb.append(itemSuffix);
            }
            if (i != size - 1) {
                sb.append(separator);
            }
        });
        return sb.toString();
    }
}
