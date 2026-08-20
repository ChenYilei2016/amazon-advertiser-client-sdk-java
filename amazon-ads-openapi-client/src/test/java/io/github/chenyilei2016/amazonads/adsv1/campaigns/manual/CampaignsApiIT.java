package io.github.chenyilei2016.amazonads.adsv1.campaigns.manual;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import io.github.chenyilei2016.amazonads.adsv1.campaigns.api.CampaignsApi;
import io.github.chenyilei2016.amazonads.adsv1.campaigns.model.*;
import io.github.chenyilei2016.amazonads.adsv1.campaigns.model.AdProduct;
import io.github.chenyilei2016.amazonads.adsv1.manual.AmazonAdsManualTestSupport;
import io.github.chenyilei2016.amazonads.adsv1.targets.api.TargetsApi;
import io.github.chenyilei2016.amazonads.adsv1.targets.model.*;
import io.github.chenyilei2016.amazonads.client.ApiException;
import io.github.chenyilei2016.amazonads.client.ApiResponse;
import io.github.chenyilei2016.amazonads.validate.ValidateBean;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CampaignsApiIT {

    /**
     * @formatter:off
     *
     * {
     * 	"data":{
     * 		"campaigns":[
     * 			{
     * 				"adProduct":"SPONSORED_PRODUCTS",
     * 				"adomains":[
     *
     * 				],
     * 				"autoCreationSettings":{
     * 					"autoCreateTargets":true
     * 				},
     * 				"autoScaleGlobalCampaign":"MANUAL",
     * 				"budgets":[
     * 					{
     * 						"budgetType":"MONETARY",
     * 						"budgetValue":{
     * 							"monetaryBudgetValue":{
     * 								"marketplaceSettings":[
     *
     * 								],
     * 								"monetaryBudget":{
     * 									"currencyCode":"USD",
     * 									"value":1.11
     * 								}
     * 							}
     * 						},
     * 						"recurrenceTimePeriod":"DAILY"
     * 					}
     * 				],
     * 				"campaignId":"67198460319365",
     * 				"countries":[
     * 					"US"
     * 				],
     * 				"creationDateTime":"2022-08-18T01:58:53.070Z",
     * 				"eligibleAutomatedTargetingTactics":[
     *
     * 				],
     * 				"endDateTime":"2023-01-01T07:59:59Z",
     * 				"fees":[
     *
     * 				],
     * 				"flights":[
     *
     * 				],
     * 				"frequencies":[
     *
     * 				],
     * 				"ineligibleAutomatedTargetingTactics":[
     *
     * 				],
     * 				"lastUpdatedDateTime":"2022-12-02T08:00:09.550Z",
     * 				"marketplaceConfigurations":[
     *
     * 				],
     * 				"marketplaceScope":"SINGLE_MARKETPLACE",
     * 				"marketplaces":[
     * 					"US"
     * 				],
     * 				"name":"接口自动化-自动广告（勿动）",
     * 				"optimizations":{
     * 					"bidSettings":{
     * 						"bidAdjustments":{
     * 							"audienceBidAdjustments":[
     *
     * 							],
     * 							"creativeBidAdjustments":[
     *
     * 							],
     * 							"placementBidAdjustments":[
     * 								{
     * 									"percentage":0,
     * 									"placement":"TOP_OF_SEARCH"
     * 								},
     * 								{
     * 									"percentage":0,
     * 									"placement":"PRODUCT_PAGE"
     * 								}
     * 							],
     * 							"shopperSegmentBidAdjustments":[
     *
     * 							]
     * 						},
     * 						"bidStrategy":"SALES_UP_AND_DOWN"
     * 					},
     * 					"primaryInventoryTypes":[
     *
     * 					]
     * 				},
     * 				"portfolioId":"69812030691501",
     * 				"siteRestrictions":[
     *
     * 				],
     * 				"startDateTime":"2022-10-27T07:00:00Z",
     * 				"state":"PAUSED",
     * 				"status":{
     * 					"deliveryReasons":[
     * 						"CAMPAIGN_END_DATE_REACHED",
     * 						"CAMPAIGN_PAUSED",
     * 						"ADVERTISER_PAYMENT_FAILURE"
     * 					],
     * 					"deliveryStatus":"NOT_DELIVERING",
     * 					"marketplaceSettings":[
     *
     * 					]
     * 				},
     * 				"tags":[
     *
     * 				]
     * 			},
     * 			{
     * 				"adProduct":"SPONSORED_PRODUCTS",
     * 				"adomains":[
     *
     * 				],
     * 				"autoCreationSettings":{
     * 					"autoCreateTargets":false
     * 				},
     * 				"autoScaleGlobalCampaign":"MANUAL",
     * 				"budgets":[
     * 					{
     * 						"budgetType":"MONETARY",
     * 						"budgetValue":{
     * 							"monetaryBudgetValue":{
     * 								"marketplaceSettings":[
     *
     * 								],
     * 								"monetaryBudget":{
     * 									"currencyCode":"USD",
     * 									"value":1.1
     * 								}
     * 							}
     * 						},
     * 						"recurrenceTimePeriod":"DAILY"
     * 					}
     * 				],
     * 				"campaignId":"29360028274381",
     * 				"countries":[
     * 					"US"
     * 				],
     * 				"creationDateTime":"2022-08-25T01:33:44.365Z",
     * 				"eligibleAutomatedTargetingTactics":[
     *
     * 				],
     * 				"endDateTime":"2024-01-02T07:59:59Z",
     * 				"fees":[
     *
     * 				],
     * 				"flights":[
     *
     * 				],
     * 				"frequencies":[
     *
     * 				],
     * 				"ineligibleAutomatedTargetingTactics":[
     *
     * 				],
     * 				"lastUpdatedDateTime":"2023-12-13T06:41:03.505Z",
     * 				"marketplaceConfigurations":[
     *
     * 				],
     * 				"marketplaceScope":"SINGLE_MARKETPLACE",
     * 				"marketplaces":[
     * 					"US"
     * 				],
     * 				"name":"python测试广告（修改名称）",
     * 				"optimizations":{
     * 					"bidSettings":{
     * 						"bidAdjustments":{
     * 							"audienceBidAdjustments":[
     *
     * 							],
     * 							"creativeBidAdjustments":[
     *
     * 							],
     * 							"placementBidAdjustments":[
     * 								{
     * 									"percentage":38,
     * 									"placement":"TOP_OF_SEARCH"
     * 								},
     * 								{
     * 									"percentage":4,
     * 									"placement":"PRODUCT_PAGE"
     * 								}
     * 							],
     * 							"shopperSegmentBidAdjustments":[
     *
     * 							]
     * 						},
     * 						"bidStrategy":"MANUAL"
     * 					},
     * 					"primaryInventoryTypes":[
     *
     * 					]
     * 				},
     * 				"siteRestrictions":[
     *
     * 				],
     * 				"startDateTime":"2022-08-24T07:00:00Z",
     * 				"state":"PAUSED",
     * 				"status":{
     * 					"deliveryReasons":[
     * 						"CAMPAIGN_END_DATE_REACHED",
     * 						"CAMPAIGN_PAUSED",
     * 						"ADVERTISER_PAYMENT_FAILURE"
     * 					],
     * 					"deliveryStatus":"NOT_DELIVERING",
     * 					"marketplaceSettings":[
     *
     * 					]
     * 				},
     * 				"tags":[
     *
     * 				]
     * 			}
     * 		],
     * 		"nextToken":"A012368125WKQO7M8II8Z"
     * 	}
     * 	"statusCode":200
     * }
     * @formatter:on
     */
    @Test
    void query() throws ApiException {
        AmazonAdsManualTestSupport.Context context = AmazonAdsManualTestSupport.requireContext();

        QueryCampaignRequest queryCampaignRequest = new QueryCampaignRequest();
        queryCampaignRequest.maxResults(1);
        queryCampaignRequest.setAdProductFilter(new CampaignAdProductFilter().addIncludeItem(AdProduct.SPONSORED_PRODUCTS));
        CampaignsApi.APIQueryCampaignRequest r = CampaignsApi.APIQueryCampaignRequest.newBuilder()
                .amazonAdsClientId(context.clientId())
                .amazonAdvertisingAPIScope(context.requireProfileId())
                .queryCampaignRequest(queryCampaignRequest)
                .build();

        ValidateBean.validateThrow(queryCampaignRequest);

        ApiResponse<CampaignSuccessResponse> campaignWithHttpInfo =
                new CampaignsApi(context.apiClient()).queryCampaignWithHttpInfo(r);

        System.err.println(JSON.toJSONString(campaignWithHttpInfo, JSONWriter.Feature.PrettyFormat));
    }
}
