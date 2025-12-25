package io.github.chenyilei2016.amznadclient.model.spv3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.amznadclient.kernel.validate.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 2025/5/20 16:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AmznSpOffAmazonSettingsDTO {

    /**
     * 在亚马逊站外投放的广告的设置：offAmazonBudgetControlStrategy；单选
     *  MAXIMIZE_REACH: 尽量扩大触达：使用投放设置优先考虑扩大触达。此设置可能会增加亚马逊站外的展示量和销售机会。
     *  MINIMIZE_SPEND: 尽量减少支出：优化广告投放以尽量减少支出。此设置可能会减少亚马逊站外的展示量，但有助于控制支出。
     */
    @Expose
    @SerializedName("offAmazonBudgetControlStrategy")
    @EnumValue(enumClass = AmznSpOffAmazonBudgetControlStrategyEnum.class)
    public String offAmazonBudgetControlStrategy;
}
