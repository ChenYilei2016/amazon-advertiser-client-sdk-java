package io.github.chenyilei2016.myclient.model.spv3;

import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.validate.EnumValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author chenyilei
 * @date 2023/04/12 15:48
 */
@NoArgsConstructor
@Data
public class AmznSpCampaignListResponse {

    @SerializedName("totalResults")
    private Integer totalResults;
    @SerializedName("campaigns")
    private List<CampaignDTO> campaigns;
    @SerializedName("nextToken")
    private String nextToken;

    @NoArgsConstructor
    @Data
    public static class CampaignDTO {
        @SerializedName("portfolioId")
        private String portfolioId;
        @SerializedName("endDate")
        private String endDate;
        @SerializedName("campaignId")
        private String campaignId;
        @SerializedName("name")
        private String name;
        @SerializedName("targetingType")
        @EnumValue(enumClass = AmznSpProductTargetExpressionTypeEnum.AmznSpCampaignTargetTypeEnum.class)
        private String targetingType;
        @SerializedName("state")
        private String state;
        @SerializedName("dynamicBidding")
        private AmznSpCampaignDynamicBiddingDTO dynamicBidding;
        @SerializedName("startDate")
        private String startDate;
        @SerializedName("budget")
        private AmznSpCampaignBudgetDTO budget;
        @SerializedName("tags")
        private String tags;

        @SerializedName("siteRestrictions")
        private List<String> siteRestrictions;

        @SerializedName("extendedData")
        private AmznCommonExtendedDataDTO extendedData;

        @SerializedName("offAmazonSettings")
        private AmznSpOffAmazonSettingsDTO offAmazonSettings;

    }
}
