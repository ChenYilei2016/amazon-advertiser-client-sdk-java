package io.github.chenyilei2016.myclient.model.spv3;

import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;



/**
 * SponsoredProducts Placement string
 * You can enable controls to adjust your bid based on the placement location. Specify a location where you want to use bid controls. The percentage value set is the percentage of the original bid for which you want to have your bid adjustment increased. For example, a 50% adjustment on a $1.00 bid would increase the bid to $1.50 for the opportunity to win a specified placement.
 *
 * Predicate	Placement
 * PLACEMENT_TOP	Top of search (first page)
 * PLACEMENT_PRODUCT_PAGE	Product pages
 * PLACEMENT_REST_OF_SEARCH	Rest of the search
 *
 * @author chenyilei
 * @date 2023/04/17 15:59
 */
@Getter
@AllArgsConstructor
public enum AmznSpCampaignPlacementEnum implements BaseEnum {
    PLACEMENT_TOP(AmznSpCampaignPlacementOriginalEnum.TOP_OF_SEARCH_ON_AMAZON),
    PLACEMENT_PRODUCT_PAGE(AmznSpCampaignPlacementOriginalEnum.DETAIL_PAGE_ON_AMAZON),
    PLACEMENT_REST_OF_SEARCH(AmznSpCampaignPlacementOriginalEnum.OTHER_ON_AMAZON),
    SITE_AMAZON_BUSINESS(AmznSpCampaignPlacementOriginalEnum.SITE_AMAZON_BUSINESS)
    ;

    private final AmznSpCampaignPlacementOriginalEnum originalEnum;

    public String getCode() {
        return this.name();
    }

    public static AmznSpCampaignPlacementEnum findByEnum(String originalEnumName) {
        return Arrays.stream(AmznSpCampaignPlacementEnum.values())
                .filter(amznSpCampaignPlacementEnum -> amznSpCampaignPlacementEnum.getOriginalEnum().getCode().equals(originalEnumName))
                .findFirst()
                .orElse(null);
    }
}
