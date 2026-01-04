package io.github.chenyilei2016.amznadclient.model.spv3;

import cn.hutool.core.collection.CollectionUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.amznadclient.kernel.validate.EnumValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenyilei
 * @date 2023/04/17 15:48
 */
@NoArgsConstructor
@Data
public class AmznSpCampaignDynamicBiddingDTO {

    @Expose
    @SerializedName("placementBidding")
    @Valid
    private List<PlacementBiddingDTO> placementBidding;

    @Expose
    @SerializedName("strategy")
    @EnumValue(enumClass = AmznSpCampaignStrategyEnum.class)
    private String strategy;

    @Expose
    @SerializedName("shopperCohortBidding")
    @Size(max = 1)
    @Valid
    private List<ShopperCohortBidAdjustmentsDTO> shopperCohortBidding;

    @NoArgsConstructor
    @Data
    public static class PlacementBiddingDTO {
        @Expose
        @SerializedName("percentage")
        @DecimalMax("900")
        @DecimalMin("0")
        @NotNull
        private BigDecimal percentage;

        @Expose
        @SerializedName("placement")
        @EnumValue(enumClass = AmznSpCampaignPlacementEnum.class)
        @NotNull
        private String placement;
    }


    @NoArgsConstructor
    @Data
    public static class ShopperCohortBidAdjustmentsDTO {
        @Expose
        @SerializedName("shopperCohortType")
        @NotNull
        @EnumValue(enumClass = BiddingShopperCohortTypeEnum.class)
        private String shopperCohortType;

        @Expose
        @SerializedName("percentage")
        @DecimalMin("0")
        @DecimalMax("900")
        private BigDecimal percentage;

        /**
         * 目前只有1个
         **/
        @Expose
        @SerializedName("audienceSegments")
        @Valid
        private List<AudienceSegmentsDTO> audienceSegments;
    }

    @NoArgsConstructor
    @Data
    public static class AudienceSegmentsDTO {
        @Expose
        @SerializedName("audienceId")
        @NotNull
        private String audienceId;

        @Expose
        @SerializedName("audienceSegmentType")
        @EnumValue(enumClass = BiddingAudienceSegmentTypeEnum.class)
        private String audienceSegmentType;
    }

    public void clearUselessData() {
        if (placementBidding != null) {
            CollectionUtil.filter(placementBidding, placementBiddingDTO -> {
                return placementBiddingDTO.getPlacement() != null;
            });
        }
    }
}