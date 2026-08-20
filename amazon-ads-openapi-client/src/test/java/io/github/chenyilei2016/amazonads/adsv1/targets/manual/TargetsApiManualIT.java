package io.github.chenyilei2016.amazonads.adsv1.targets.manual;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import io.github.chenyilei2016.amazonads.client.ApiException;
import io.github.chenyilei2016.amazonads.adsv1.manual.AmazonAdsManualTestSupport;
import io.github.chenyilei2016.amazonads.adsv1.manual.AmazonAdsManualTestSupport.Context;
import io.github.chenyilei2016.amazonads.adsv1.targets.api.TargetsApi;
import io.github.chenyilei2016.amazonads.adsv1.targets.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TargetsApiManualIT {

    /**
     * @formatter:off
     *
     * {
     * 	"nextToken":"A0535685QWTNJ145FN2B",
     * 	"targets":[
     * 		{
     * 			"adGroupId":"452546085839232",
     * 			"adProduct":"SPONSORED_PRODUCTS",
     * 			"bid":{
     * 				"bid":0.02,
     * 				"currencyCode":"USD",
     * 				"marketplaceSettings":[
     *
     * 				]
     * 			},
     * 			"campaignId":"471609048860416",
     * 			"creationDateTime":"2026-04-15T02:33:24.784Z",
     * 			"lastUpdatedDateTime":"2026-04-15T02:33:24.784Z",
     * 			"marketplaceConfigurations":[
     *
     * 			],
     * 			"marketplaceScope":"SINGLE_MARKETPLACE",
     * 			"marketplaces":[
     * 				"US"
     * 			],
     * 			"negative":false,
     * 			"state":"ENABLED",
     * 			"status":{
     * 				"deliveryReasons":[
     * 					"ADVERTISER_PAYMENT_FAILURE"
     * 				],
     * 				"deliveryStatus":"NOT_DELIVERING",
     * 				"marketplaceSettings":[
     *
     * 				]
     * 			},
     * 			"tags":[
     *
     * 			],
     * 			"targetDetails":{
     * 				"productTarget":{
     * 					"productTarget":null
     * 				}
     * 			},
     * 			"targetId":"213055688589673",
     * 			"targetLevel":"AD_GROUP",
     * 			"targetType":"PRODUCT"
     * 		}
     * 	]
     * }
     * @formatter:on
     */
    @Test
    void shouldQueryTargetsFromAmazonAds() throws ApiException {
        Context context = AmazonAdsManualTestSupport.requireContext();

        QueryTargetRequest queryTargetRequest = new QueryTargetRequest()
                .adProductFilter(new TargetAdProductFilter().addIncludeItem(AdProduct.SPONSORED_PRODUCTS))
                .targetIdFilter(new TargetTargetIdFilter().addIncludeItem("213055688589673"))
                .maxResults(1);
        TargetsApi.APIQueryTargetRequest request = TargetsApi.APIQueryTargetRequest.newBuilder()
                .amazonAdsClientId(context.clientId())
                .amazonAdsAccountId(context.accountId())
                .amazonAdvertisingAPIScope(context.requireProfileId())
                .queryTargetRequest(queryTargetRequest)
                .build();

        TargetSuccessResponse response = new TargetsApi(context.apiClient()).queryTarget(request);

        assertNotNull(response, "Amazon Ads 查询成功时必须返回响应体。");
        System.out.printf("Targets 查询成功：totalResults=%s，返回条数=%d，nextToken=%s%n%s%n",
                response.getTotalResults(),
                response.getTargets() == null ? 0 : response.getTargets().size(),
                response.getNextToken(),
                JSON.toJSONString(response, JSONWriter.Feature.PrettyFormat));
    }

}
