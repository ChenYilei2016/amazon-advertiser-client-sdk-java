package io.github.chenyilei2016.amznadclient.model.account;

import io.github.chenyilei2016.amznadclient.kernel.core.BaseEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenyilei
 * @date 2024/02/22 14:27
 */
@Getter
public enum AmznProfileTypeEnum implements BaseEnum {

    VC, SC, AGENCY;

    public String getCode() {
        return this.name();
    }

    public static Boolean isVC(String code) {
        return StringUtils.equalsIgnoreCase(VC.getCode(), code);
    }

    public static Boolean isSC(String code) {
        return StringUtils.equalsIgnoreCase(SC.getCode(), code);
    }
}
