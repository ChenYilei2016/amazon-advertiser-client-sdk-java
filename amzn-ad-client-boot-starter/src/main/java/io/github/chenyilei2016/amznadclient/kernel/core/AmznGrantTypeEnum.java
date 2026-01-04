package io.github.chenyilei2016.amznadclient.kernel.core;

import lombok.Getter;

/**
 * @author chenyilei
 * @date 2022/08/09 11:13
 */
@Getter
public enum AmznGrantTypeEnum implements BaseEnum{

    REFRESH_TOKEN("refresh_token", "刷新token"),
    ;

    private final String code;

    private final String desc;

    AmznGrantTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AmznGrantTypeEnum getEnum(String code) {
        for (AmznGrantTypeEnum type : AmznGrantTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
