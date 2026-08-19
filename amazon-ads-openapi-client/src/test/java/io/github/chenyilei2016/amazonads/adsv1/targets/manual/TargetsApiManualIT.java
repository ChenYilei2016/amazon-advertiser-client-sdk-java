package io.github.chenyilei2016.amazonads.adsv1.targets.manual;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import io.github.chenyilei2016.amazonads.adsv1.client.ApiException;
import io.github.chenyilei2016.amazonads.adsv1.manual.AmazonAdsManualTestSupport;
import io.github.chenyilei2016.amazonads.adsv1.manual.AmazonAdsManualTestSupport.Context;
import io.github.chenyilei2016.amazonads.adsv1.targets.api.TargetsApi;
import io.github.chenyilei2016.amazonads.adsv1.targets.model.AdProduct;
import io.github.chenyilei2016.amazonads.adsv1.targets.model.QueryTargetRequest;
import io.github.chenyilei2016.amazonads.adsv1.targets.model.TargetAdProductFilter;
import io.github.chenyilei2016.amazonads.adsv1.targets.model.TargetSuccessResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TargetsApiManualIT {

    @Test
    void shouldQueryTargetsFromAmazonAds() throws ApiException {
        Context context = AmazonAdsManualTestSupport.requireContext();

        QueryTargetRequest queryTargetRequest = new QueryTargetRequest()
                .adProductFilter(new TargetAdProductFilter().addIncludeItem(AdProduct.SPONSORED_PRODUCTS))
                .maxResults(10);
        TargetsApi.APIQueryTargetRequest request = TargetsApi.APIQueryTargetRequest.newBuilder()
                .amazonAdsClientId(context.clientId())
                .amazonAdsAccountId(context.accountId())
                .amazonAdvertisingAPIScope(context.requireProfileId())
                .queryTargetRequest(queryTargetRequest)
                .build();

        TargetSuccessResponse response = new TargetsApi(context.apiClient()).queryTarget(request);

        System.err.println(JSON.toJSONString(response, JSONWriter.Feature.PrettyFormat));
        assertNotNull(response, "Amazon Ads 查询成功时必须返回响应体。");
        System.out.printf("Targets 查询成功：totalResults=%s，返回条数=%d，nextToken=%s%n%s%n",
                response.getTotalResults(),
                response.getTargets() == null ? 0 : response.getTargets().size(),
                response.getNextToken(),
                response);
    }

}
