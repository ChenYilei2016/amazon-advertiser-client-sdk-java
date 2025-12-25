package io.github.chenyilei2016.myclient.model.spv3;

import com.google.common.collect.Lists;
import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;
import lombok.Getter;

import java.util.List;

/**
 * @author chenyilei
 * @date 2023/04/12 15:39
 */
@Getter
public enum AmznCommonEntityStateEnum implements BaseEnum {

    ENABLED, PAUSED,
    ARCHIVED, ENABLING, USER_DELETED, OTHER,
    PENDING, DRAFT, //sb keyword
    ;

    @Override
    public String getCode() {
        return this.name();
    }

    public static AmznCommonEntityStateEnum getEnum(String code) {
        for (AmznCommonEntityStateEnum type : AmznCommonEntityStateEnum.values()) {
            if (type.name().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }


    public static List<String> enablePausedList() {
        return Lists.newArrayList(ENABLED.getCode(), PAUSED.getCode());
    }
}
