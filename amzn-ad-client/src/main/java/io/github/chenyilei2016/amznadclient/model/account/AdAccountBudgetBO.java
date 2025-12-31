package io.github.chenyilei2016.amznadclient.model.account;

import com.zbycorp.fenghuo.domain.common.amazonconnect.kernel.enums.AmznProfileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author SunYingLing
 * @description 获取平台店铺信息
 * @create 2023/3/3 10:52 上午
 * @formatter:off
 * {
 *         "profileId": 2039815907868317,
 *         "countryCode": "US",
 *         "currencyCode": "USD",
 *         "timezone": "America/Los_Angeles",
 *         "accountInfo": {
 *             "marketplaceStringId": "ATVPDKIKX0DER",
 *             "id": "ENTITY2XL7W6G9YD7UO",
 *             "type": "vendor",
 *             "name": "Sportneer",
 *             "validPaymentMethod": false
 *         }
 *     }
 * @formatter:on
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AdAccountBudgetBO {

    /**
     * 店铺ID
     */
    private String profileId;

    /**
     * 国家code
     */
    private String countryCode;

    /**
     * 当前code
     */
    private String currencyCode;

    /**
     * 广告预算
     */
    private BigDecimal dailyBudget;

    /**
     * 当前时间
     */
    private String timezone;

    /**
     * 账户信息
     */
    private AccountInfo accountInfo;

    @Data
    public static class AccountInfo {

        private String marketplaceStringId;

        private String id;

        private String type;

        private String name;

        private String subType;

        private Boolean validPaymentMethod;
    }

    public String getAccountInfoId() {
        if (accountInfo == null) {
            return null;
        }
        return accountInfo.getId();
    }

    public AmznProfileTypeEnum convertAccountInfoType() {
        if (accountInfo == null) {
            return null;
        }
        String type = accountInfo.getType();

        if ("vendor".equalsIgnoreCase(type)) {
            return AmznProfileTypeEnum.VC;
        } else if ("seller".equalsIgnoreCase(type)) {
            return AmznProfileTypeEnum.SC;
        } else if ("agency".equalsIgnoreCase(type)) {
            return AmznProfileTypeEnum.AGENCY;
        }
        return null;
    }

}
