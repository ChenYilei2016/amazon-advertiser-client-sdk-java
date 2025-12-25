package io.github.chenyilei2016.myclient.kernel.utils;

import io.github.chenyilei2016.myclient.kernel.utils.enums.CommonDatePatternEnum;
import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

/**
 * @author chenyilei
 * @date 2023/05/05 13:50
 */
@UtilityClass
public class StrDateUtil {

    public static final long oneDayMillis = 24 * 60 * 60 * 1000;

    /**
     * @param strDate        传入的时间 字符串格式
     * @param dateFormat     预期的时间格式
     * @param setFormatValue 调整时间参数的方法
     */
    public static void expected(String strDate, CommonDatePatternEnum dateFormat, Consumer<String> setFormatValue) {
        StrUtils.expected(strDate, dateFormat, setFormatValue);
    }

    public static String expected(String strDate, CommonDatePatternEnum dateFormat) {
        return StrUtils.expected(strDate, dateFormat);
    }



}
