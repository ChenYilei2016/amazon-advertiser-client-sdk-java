package io.github.chenyilei2016.myclient.kernel.utils.enums;

import lombok.Getter;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author chenyilei
 * @date 2022/12/20 16:24
 */
@Getter
public enum CommonDatePatternEnum {

    yyyyMMdd("yyyyMMdd", "年月日 , 20221212", Pattern.compile(CommonDatePatternConstant.yyyyMMdd)),
    /**
     * 用于数据库的话注意了.  直接用pattern转可能会有问题
     *
     * @see com.zbycorp.fenghuo.domain.common.utils.MyDateUtils#getWeekOfYearAdapterMysql(Date)
     */
    yyyyww("yyyyww", "年周 ,  202234 , 2022年第34周", Pattern.compile("^\\d{4}\\d{1,2}$")),

    yyyyMM("yyyyMM", "月 , 202212; 2022年第12个月", Pattern.compile("^\\d{4}\\d{2}$")),


    yyyy_MM_dd("yyyy-MM-dd", "年月日 , 2022-12-12", Pattern.compile(CommonDatePatternConstant.yyyy_mm_dd)),

    yyyy_MM_dd_HH("yyyy-MM-dd HH", "年月日时, 2022-12-12 03", Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}$")),
    ;

    private final String code;

    private final String desc;

    private final Pattern pattern;

    CommonDatePatternEnum(String code, String desc, Pattern pattern) {
        this.code = code;
        this.desc = desc;
        this.pattern = pattern;
    }

    public static CommonDatePatternEnum getEnum(String code) {
        for (CommonDatePatternEnum type : CommonDatePatternEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
