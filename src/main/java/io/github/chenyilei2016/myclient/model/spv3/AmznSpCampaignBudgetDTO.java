package io.github.chenyilei2016.myclient.model.spv3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.validate.EnumValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * @author chenyilei
 * @date 2023/04/17 15:50
 */
@NoArgsConstructor
@Data
public class AmznSpCampaignBudgetDTO {

    public AmznSpCampaignBudgetDTO(BigDecimal budget) {
        this.budget = budget;
    }

    public AmznSpCampaignBudgetDTO(String budgetType, BigDecimal budget) {
        this.budgetType = budgetType;
        this.budget = budget;
    }

    @SerializedName("budgetType")
    @EnumValue(enumClass = AmznCommonCampaignBudgetTypeEnum.class)
    @Expose
    private String budgetType = AmznCommonCampaignBudgetTypeEnum.DAILY.getCode();

    @Expose
    @SerializedName("budget")
    @DecimalMin("1.0")
    private BigDecimal budget;

    @Expose
    @SerializedName("effectiveBudget")
    private BigDecimal effectiveBudget;
}
