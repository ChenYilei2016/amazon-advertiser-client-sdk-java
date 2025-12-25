package io.github.chenyilei2016.myclient.model.spv3;

import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;
import lombok.Getter;

/**
 * SponsoredProductsCreateOrUpdateBiddingStrategystring
 * The bidding strategy.
 * <p>
 * Value	Strategy name	Description
 * LEGACY_FOR_SALES	Dynamic bids - down only	Lowers your bids in real time when your ad may be less likely to convert to a sale. Campaigns created before the release of the bidding controls feature used this setting by default.
 * AUTO_FOR_SALES	Dynamic bids - up and down	Increases or decreases your bids in real time by a maximum of 100%. With this setting bids increase when your ad is more likely to convert to a sale, and bids decrease when less likely to convert to a sale.
 * MANUAL	Fixed bid	Uses your exact bid and any placement adjustments you set, and is not subject to dynamic bidding.
 * RULE_BASED	https://advertising.amazon.com/API/docs/en-us/sponsored-products/rule-based-bidding/overview
 *
 * @author chenyilei
 * @date 2023/04/17 16:00
 */
@Getter
public enum AmznSpCampaignStrategyEnum implements BaseEnum {
    LEGACY_FOR_SALES, AUTO_FOR_SALES, MANUAL, RULE_BASED;

    public String getCode() {
        return this.name();
    }
}