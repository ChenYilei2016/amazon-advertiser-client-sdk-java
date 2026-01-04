package io.github.chenyilei2016.amznadclient.model.spv3;

import cn.hutool.core.collection.CollectionUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.amznadclient.kernel.utils.StrDateUtil;
import io.github.chenyilei2016.amznadclient.kernel.utils.enums.CommonDatePatternConstant;
import io.github.chenyilei2016.amznadclient.kernel.utils.enums.CommonDatePatternEnum;
import io.github.chenyilei2016.amznadclient.kernel.validate.EnumValue;
import io.github.chenyilei2016.amznadclient.kernel.validate.EqualValidValue;
import io.github.chenyilei2016.amznadclient.kernel.validate.ExpectNoTrailingWhitespaces;
import io.github.chenyilei2016.amznadclient.kernel.validate.ValidateBean;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author chenyilei
 * @date 2023/04/14 13:35
 */
@Data
public class AmznSpCampaignCreateRequest implements ValidateBean.ExecuteBeforeValidate {

    @NotEmpty
    private String profileId;

    @Valid
    @NotEmpty
    @Size(min = 1, max = 1000)
    @Expose
    @SerializedName("campaigns")
    private List<CreateDTO> campaigns;

    @NoArgsConstructor
    @Data
    public static class CreateDTO {

        @Expose
        @SerializedName("portfolioId")
        private String portfolioId;

        @Expose
        @SerializedName("name")
        @Size(min = 1, max = 128)
        @NotEmpty
        @ExpectNoTrailingWhitespaces
        private String name;

        @Expose
        @SerializedName("targetingType")
        @EnumValue(enumClass = AmznSpProductTargetExpressionTypeEnum.AmznSpCampaignTargetTypeEnum.class)
        @NotNull
        private String targetingType;

        @Expose
        @SerializedName("state")
        @EnumValue(enumClass = AmznCommonEntityStateEnum.class)
        @EqualValidValue(strValues = {"ENABLED", "PAUSED"})
        private String state = AmznCommonEntityStateEnum.ENABLED.getCode();

        @Expose
        @SerializedName("startDate")
        @Pattern(regexp = CommonDatePatternConstant.yyyy_mm_dd)
        @NotNull
        private String startDate;

        @Expose
        @SerializedName("endDate")
        @Pattern(regexp = CommonDatePatternConstant.yyyy_mm_dd)
        private String endDate;

        @Expose
        @SerializedName("dynamicBidding")
        @Valid
        private AmznSpCampaignDynamicBiddingDTO dynamicBidding;


        @Expose
        @SerializedName("budget")
        @Valid
        @NotNull
        private AmznSpCampaignBudgetDTO budget;

        @Expose
        @SerializedName("offAmazonSettings")
        @Valid
        private AmznSpOffAmazonSettingsDTO offAmazonSettings;

        @Expose
        @SerializedName("siteRestrictions")
        @Valid
        private List<String> siteRestrictions;
    }

    public boolean existRequest() {
        return CollectionUtil.isNotEmpty(campaigns);
    }


    @Override
    public void beforeValidate() {
        CollectionUtil.forEach(campaigns, (dto, index) -> {
            StrDateUtil.expected(dto.getStartDate(), CommonDatePatternEnum.yyyy_MM_dd, dto::setStartDate);
            StrDateUtil.expected(dto.getEndDate(), CommonDatePatternEnum.yyyy_MM_dd, dto::setEndDate);
        });
    }

}
