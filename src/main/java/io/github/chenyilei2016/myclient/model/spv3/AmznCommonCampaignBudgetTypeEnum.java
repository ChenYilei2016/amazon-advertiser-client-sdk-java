package io.github.chenyilei2016.myclient.model.spv3;

import com.zbycorp.fenghuo.domain.common.constants.BaseEnum;
import lombok.Getter;

/**
 * @author chenyilei
 * @date 2023/05/10 14:05
 */
@Getter
public enum AmznCommonCampaignBudgetTypeEnum implements BaseEnum {
    DAILY, //sp + sd + sb
    LIFETIME //sb
    ;

    public String getCode() {
        return this.name();
    }
}
