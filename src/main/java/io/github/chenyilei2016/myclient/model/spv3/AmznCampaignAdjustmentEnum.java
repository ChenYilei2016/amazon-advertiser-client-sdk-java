package io.github.chenyilei2016.myclient.model.spv3;

import com.zbycorp.fenghuo.domain.common.constants.BaseEnum;
import lombok.Getter;

/**
 * @author chenyilei
 * @date 2023/02/07 14:56
 */
@Getter
public enum AmznCampaignAdjustmentEnum implements BaseEnum {

    placementTop("placementTop", "Top of search (first page)", "搜索结果顶部"),
    placementProductPage("placementProductPage", "Product pages", "商品页面");

    private final String code;

    private final String desc;

    private final String chDesc;

    AmznCampaignAdjustmentEnum(String code, String desc, String chDesc) {
        this.code = code;
        this.desc = desc;
        this.chDesc = chDesc;
    }

    public static AmznCampaignAdjustmentEnum getEnum(String code) {
        for (AmznCampaignAdjustmentEnum type : AmznCampaignAdjustmentEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
