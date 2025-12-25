package io.github.chenyilei2016.myclient.model.spv3;

import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;
import lombok.Getter;

/**
 * 亚马逊广告位报表枚举
 *
 * @author czy
 * @date 2024-11-07 15:18:12
 **/
@Getter
public enum AmznSpCampaignPlacementOriginalEnum implements BaseEnum {
    TOP_OF_SEARCH_ON_AMAZON("Top of Search on-Amazon", "搜索结果顶部（首页）"),
    DETAIL_PAGE_ON_AMAZON("Detail Page on-Amazon", "商品页面"),
    OTHER_ON_AMAZON("Other on-Amazon", "搜索结果的其余位置"),
    OFF_AMAZON("Off Amazon", "亚马逊站外"),
    SITE_AMAZON_BUSINESS("Site Amazon Business", "亚马逊商业站点"),
    ;

    private String code;
    private String desc;


    AmznSpCampaignPlacementOriginalEnum(String code, String desc) {
        this.desc = desc;
        this.code = code;
    }

}
