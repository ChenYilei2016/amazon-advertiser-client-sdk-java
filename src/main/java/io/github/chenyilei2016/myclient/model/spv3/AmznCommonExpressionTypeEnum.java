package io.github.chenyilei2016.myclient.model.spv3;

import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;
import lombok.Getter;

/**
 * @author chenyilei
 * @date 2023/04/12 19:52
 */
@Getter
public enum AmznCommonExpressionTypeEnum implements BaseEnum {
    AUTO, MANUAL, OTHER;

    @Override
    public String getCode() {
        return this.name();
    }
}
