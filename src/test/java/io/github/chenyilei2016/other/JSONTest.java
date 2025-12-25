package io.github.chenyilei2016.other;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import io.github.chenyilei2016.myclient.kernel.gson.GsonUtil;
import io.github.chenyilei2016.myclient.model.spv3.AmznSpCampaignCreateRequest;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenyilei
 * @since 2025/12/25 13:47
 */
public class JSONTest {

    @Test
    public void testParseObject() {
        String testRequestJson =
                "{\n" +
                        "  \"profileId\": \"profileId_c13c8ab0015b\",\n" +
                        "  \"campaigns\": [\n" +
                        "    {\n" +
                        "      \"portfolioId\": \"portfolioId_af02fc777d75\",\n" +
                        "      \"name\": \"name_521071580a99\",\n" +
                        "      \"targetingType\": \"targetingType_bfcd8866d77a\",\n" +
                        "      \"state\": \"state_8680e75a989f\",\n" +
                        "      \"startDate\": \"startDate_cc222c424339\",\n" +
                        "      \"endDate\": \"endDate_00bb6f9035ce\",\n" +
                        "      \"dynamicBidding\": {\n" +
                        "        \"placementBidding\": [\n" +
                        "          {\n" +
                        "            \"percentage\": 1.50,\n" +
                        "            \"placement\": \"placement_d300fd00a331\"\n" +
                        "          }\n" +
                        "        ],\n" +
                        "        \"strategy\": \"strategy_8c99f5e8468c\",\n" +
                        "        \"shopperCohortBidding\": [\n" +
                        "          {\n" +
                        "            \"shopperCohortType\": \"shopperCohortType_123f442c58ce\",\n" +
                        "            \"percentage\": 0.00,\n" +
                        "            \"audienceSegments\": [\n" +
                        "              {\n" +
                        "                \"audienceId\": \"audienceId_1a3fa2f47b4e\",\n" +
                        "                \"audienceSegmentType\": \"audienceSegmentType_a0119d48c377\"\n" +
                        "              }\n" +
                        "            ]\n" +
                        "          }\n" +
                        "        ]\n" +
                        "      },\n" +
                        "      \"budget\": {\n" +
                        "        \"budgetType\": \"budgetType_873586f88cc0\",\n" +
                        "        \"budget\": 1.00,\n" +
                        "        \"effectiveBudget\": 0.00\n" +
                        "      },\n" +
                        "      \"offAmazonSettings\": {\n" +
                        "        \"offAmazonBudgetControlStrategy\": \"offAmazonBudgetControlStrategy_fba069e4a6ef\"\n" +
                        "      },\n" +
                        "      \"siteRestrictions\": [\n" +
                        "        \"siteRestrictions_b4e7e55daea4\"\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
        AmznSpCampaignCreateRequest amznSpCampaignCreateRequest = GsonUtil.parseObject(testRequestJson, AmznSpCampaignCreateRequest.class);
        Assert.assertEquals(amznSpCampaignCreateRequest.getCampaigns().get(0).getTargetingType(), "targetingType_bfcd8866d77a");
        Assert.assertEquals(amznSpCampaignCreateRequest.getCampaigns().get(0).getDynamicBidding().getPlacementBidding().get(0).getPercentage(), new BigDecimal("1.50"));


        String jsonString = GsonUtil.toJsonString(amznSpCampaignCreateRequest);

        amznSpCampaignCreateRequest = GsonUtil.parseObject(jsonString, new TypeToken<AmznSpCampaignCreateRequest>() {
        });
        Assert.assertEquals(amznSpCampaignCreateRequest.getCampaigns().get(0).getTargetingType(), "targetingType_bfcd8866d77a");
        Assert.assertEquals(amznSpCampaignCreateRequest.getCampaigns().get(0).getDynamicBidding().getPlacementBidding().get(0).getPercentage(), new BigDecimal("1.50"));
    }

    @Test
    public void testMap() {
        HashMap<String, String> m = Maps.newHashMap();
        m.put("1", "22");
        m.put("3", "777");

        String jsonString = GsonUtil.toJsonString(m);

        Map<String, String> stringStringMap = GsonUtil.parseObject(jsonString, new TypeToken<Map<String, String>>() {
        });
        System.err.println(stringStringMap);
        Assert.assertEquals(stringStringMap.get("1"), "22");

    }
}
