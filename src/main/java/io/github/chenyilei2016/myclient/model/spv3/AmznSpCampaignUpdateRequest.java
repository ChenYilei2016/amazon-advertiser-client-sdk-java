package io.github.chenyilei2016.myclient.model.spv3;

import cn.hutool.core.collection.CollectionUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.gson.GsonNullableAdapterFactory;
import io.github.chenyilei2016.myclient.kernel.gson.JsonSerializeNullable;
import io.github.chenyilei2016.myclient.kernel.gson.NullableFieldSerialParent;
import io.github.chenyilei2016.myclient.kernel.utils.StrDateUtil;
import io.github.chenyilei2016.myclient.kernel.utils.enums.CommonDatePatternConstant;
import io.github.chenyilei2016.myclient.kernel.utils.enums.CommonDatePatternEnum;
import io.github.chenyilei2016.myclient.kernel.validate.EnumValue;
import io.github.chenyilei2016.myclient.kernel.validate.EqualValidValue;
import io.github.chenyilei2016.myclient.kernel.validate.ExpectNoTrailingWhitespaces;
import io.github.chenyilei2016.myclient.kernel.validate.ValidateBean;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenyilei
 * @date 2023/04/14 13:35
 */
@Data
public class AmznSpCampaignUpdateRequest implements ValidateBean.ExecuteBeforeValidate {

    @NotEmpty
    private String profileId;

    @Valid
    @NotEmpty
    @Size(min = 1, max = 1000)
    @Expose
    @SerializedName("campaigns")
    private List<UpdateDTO> campaigns;

    public boolean existRequest() {
        return CollectionUtil.isNotEmpty(campaigns);
    }

    public List<String> getRequestIds() {
        return campaigns.stream().map(UpdateDTO::getCampaignId).collect(Collectors.toList());
    }

    @Data
    @JsonAdapter(value = GsonNullableAdapterFactory.class)
    @JsonSerializeNullable
    public static class NullableUpdateDTO extends UpdateDTO {

    }

    @NoArgsConstructor
    @Data
    public static class UpdateDTO extends NullableFieldSerialParent {

        @Expose
        @NotEmpty
        @SerializedName("campaignId")
        private String campaignId;

        @Expose
        @SerializedName("portfolioId")
        private String portfolioId;

        @Expose
        @SerializedName("name")
        @Size(min = 1, max = 128)
        @ExpectNoTrailingWhitespaces
        private String name;

        @Expose
        @SerializedName("targetingType")
        @EnumValue(enumClass = AmznSpProductTargetExpressionTypeEnum.AmznSpCampaignTargetTypeEnum.class)
        private String targetingType;

        @Expose
        @SerializedName("state")
        @EnumValue(enumClass = AmznCommonEntityStateEnum.class)
        @EqualValidValue(strValues = {"ENABLED", "PAUSED"})
        private String state;

        @Expose
        @SerializedName("startDate")
        @Pattern(regexp = CommonDatePatternConstant.yyyy_mm_dd)
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
        private AmznSpCampaignBudgetDTO budget;

        @Expose
        @SerializedName("offAmazonSettings")
        @Valid
        private AmznSpOffAmazonSettingsDTO offAmazonSettings;
    }


    @Override
    public void beforeValidate() {
        CollectionUtil.forEach(campaigns, (dto, index) -> {
            dto.setName(StringUtils.trim(dto.getName()));
            StrDateUtil.expected(dto.getStartDate(), CommonDatePatternEnum.yyyy_MM_dd, dto::setStartDate);
            StrDateUtil.expected(dto.getEndDate(), CommonDatePatternEnum.yyyy_MM_dd, dto::setEndDate);
        });
    }
}
