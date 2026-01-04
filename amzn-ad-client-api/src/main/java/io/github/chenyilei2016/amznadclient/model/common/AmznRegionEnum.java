package io.github.chenyilei2016.amznadclient.model.common;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * https://advertising.amazon.com/API/docs/en-us/reference/api-overview#api-endpoints
 *
 * NA -> us-east-1
 * EU -> eu-west-1
 * FE -> us-west-2
 */
@Getter
@AllArgsConstructor
public enum AmznRegionEnum {

    /**
     * 常用 地区 us-east-1 ,eu-west-1, us-west-2
     */
    NORTH_AMERICA("NA", Sets.newHashSet("US", "CA", "MX", "BR"), "https://api.amazon.com/auth/o2/token", "https://advertising-api.amazon.com", "us-east-1"),
    EUROPE("EU", Sets.newHashSet("UK", "FR", "IT", "ES", "DE", "NL", "AE", "PL", "TR", "EG", "SA", "SE", "BE", "IN", "ZA", "GB", "IE"), "https://api.amazon.com/auth/o2/token", "https://advertising-api-eu.amazon.com", "eu-west-1"),
    FAR_EAST("FE", Sets.newHashSet("JP", "AU", "SG"), "https://api.amazon.com/auth/o2/token", "https://advertising-api-fe.amazon.com", "us-west-2"),
    ;

    /**
     * region: North America (NA), Europe (EU), Far East (FE)
     */
    private String region;
    /**
     * marketPlace
     * 国家
     */
    private Set<String> marketplaces;
    /**
     * authorization URL
     */
    private String authUrl;
    /**
     * URL endpoints
     */
    private String endpointUrlPrefix;


    private String regionDetail;

    /**
     * get authUrl via region
     *
     * @return
     */
    public static String getAuthUrl(String region) {
        for (AmznRegionEnum anEnum : AmznRegionEnum.values()) {
            if (anEnum.getRegion().equalsIgnoreCase(region)) {
                return anEnum.getAuthUrl();
            }
        }
        return StringUtils.EMPTY;
    }

    public static String getEndpointUrlPrefix(String region) {
        for (AmznRegionEnum anEnum : AmznRegionEnum.values()) {
            if (anEnum.getRegion().equalsIgnoreCase(region)) {
                return anEnum.getEndpointUrlPrefix();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * get enum via marketplace
     *
     * @return
     */
    public static AmznRegionEnum getViaMarketplace(String marketplace) {
        for (AmznRegionEnum anEnum : AmznRegionEnum.values()) {
            if (anEnum.getMarketplaces().contains(marketplace.toUpperCase())) {
                return anEnum;
            }
        }
        return null;
    }


    public static AmznRegionEnum getFromRegion(String region) {
        for (AmznRegionEnum anEnum : AmznRegionEnum.values()) {
            if (anEnum.getRegion().equalsIgnoreCase(region)) {
                return anEnum;
            }
        }
        return null;
    }

}
