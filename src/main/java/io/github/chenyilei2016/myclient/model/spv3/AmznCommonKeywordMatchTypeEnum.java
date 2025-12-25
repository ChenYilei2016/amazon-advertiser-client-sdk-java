package io.github.chenyilei2016.myclient.model.spv3;

import com.zbycorp.fenghuo.domain.common.constants.BaseEnum;
import lombok.Getter;

/**
 * @author chenyilei
 * @date 2023/04/14 13:39
 */
@Getter
public enum AmznCommonKeywordMatchTypeEnum implements BaseEnum {

    EXACT("精准匹配"), PHRASE("短语匹配"), BROAD("宽泛匹配");


    private final String desc;

    AmznCommonKeywordMatchTypeEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    public static String getDescByCode(String code) {
        for (AmznCommonKeywordMatchTypeEnum anEnum : AmznCommonKeywordMatchTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum.getDesc();
            }
        }
        return null;
    }

}
