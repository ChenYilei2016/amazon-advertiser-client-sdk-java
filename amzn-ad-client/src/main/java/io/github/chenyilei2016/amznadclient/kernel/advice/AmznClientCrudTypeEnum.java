package io.github.chenyilei2016.amznadclient.kernel.advice;

import io.github.chenyilei2016.amznadclient.kernel.core.BaseEnum;
import lombok.Getter;

/**
 * @author chenyilei
 * @date 2023/05/10 11:22
 */
@Getter
public enum AmznClientCrudTypeEnum implements BaseEnum {
    QUERY,
    CREATE,
    UPDATE,
    DELETE,
    OTHER;

    public String getCode() {
        return this.name();
    }
}
