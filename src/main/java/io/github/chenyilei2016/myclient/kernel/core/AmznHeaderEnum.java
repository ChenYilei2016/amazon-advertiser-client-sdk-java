package io.github.chenyilei2016.myclient.kernel.core;

import lombok.Getter;

/**
 * @author chenyilei
 * @date 2023/04/17 10:41
 */
@Getter
public enum AmznHeaderEnum {

    PREFER_RETURN_REPRESENTATION("Prefer", "return=representation", "增删改操作返回实体数据"),
    LOCALE_CHINESE("locale", "zh_CN", "期望语言"),
    ;

    private final String key;

    private final String value;

    private final String desc;

    AmznHeaderEnum(String key, String value, String desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }

    public static AmznHeaderEnum getEnum(String code) {
        for (AmznHeaderEnum type : AmznHeaderEnum.values()) {
            if (type.value.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
