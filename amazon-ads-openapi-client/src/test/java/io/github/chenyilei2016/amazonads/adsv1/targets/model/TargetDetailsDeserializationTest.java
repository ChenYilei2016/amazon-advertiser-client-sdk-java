package io.github.chenyilei2016.amazonads.adsv1.targets.model;

import io.github.chenyilei2016.amazonads.client.ApiClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TargetDetailsDeserializationTest {

    @Test
    void shouldDeserializeProductTargetWithoutAnAdditionalWrapper() throws Exception {
        TargetSuccessResponse response = new ApiClient().getObjectMapper().readValue("""
                {
                  "targets": [
                    {
                      "adGroupId": "452546085839232",
                      "adProduct": "SPONSORED_PRODUCTS",
                      "bid": {
                        "bid": 0.02,
                        "currencyCode": "USD"
                      },
                      "campaignId": "471609048860416",
                      "creationDateTime": "2026-04-15T02:33:24.784Z",
                      "lastUpdatedDateTime": "2026-04-15T02:33:24.784Z",
                      "marketplaceScope": "SINGLE_MARKETPLACE",
                      "marketplaces": ["US"],
                      "negative": false,
                      "state": "ENABLED",
                      "status": {
                        "deliveryReasons": ["ADVERTISER_PAYMENT_FAILURE"],
                        "deliveryStatus": "NOT_DELIVERING"
                      },
                      "tags": [],
                      "targetId": "213055688589673",
                      "targetLevel": "AD_GROUP",
                      "targetType": "PRODUCT",
                      "targetDetails": {
                        "productTarget": {
                          "matchType": "PRODUCT_SIMILAR",
                          "product": {
                            "productId": "B0001AVZAS"
                          },
                          "productIdType": "ASIN"
                        }
                      }
                    }
                  ]
                }
                """, TargetSuccessResponse.class);

        Target target = response.getTargets().get(0);

        assertNotNull(target.getTargetDetails());
        assertEquals(TargetType.PRODUCT, target.getTargetType());
        assertNotNull(target.getTargetDetails().getProductTarget());
        assertEquals(ProductMatchType.PRODUCT_SIMILAR, target.getTargetDetails().getProductTarget().getMatchType());
        assertEquals("B0001AVZAS", target.getTargetDetails().getProductTarget().getProduct().getProductId());
    }
}
