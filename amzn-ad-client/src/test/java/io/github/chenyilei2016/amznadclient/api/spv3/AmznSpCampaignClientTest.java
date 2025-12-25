package io.github.chenyilei2016.amznadclient.api.spv3;

import com.google.common.collect.Lists;
import io.github.chenyilei2016.amznadclient.api.AmznClientBaseTest;
import io.github.chenyilei2016.amznadclient.model.spv3.AmznCommonQueryTermMatchTypeFilter;
import io.github.chenyilei2016.amznadclient.model.spv3.AmznSpCampaignListRequest;
import io.github.chenyilei2016.amznadclient.model.spv3.AmznSpCampaignListResponse;
import org.junit.Test;

/**
 * @author chenyilei
 * @since 2025/12/25 14:51
 */
public class AmznSpCampaignClientTest extends AmznClientBaseTest {
    AmznSpCampaignClient amznSpCampaignClient = newAmznClient(AmznSpCampaignClient.class);


    @Test
    public void list() {
        AmznSpCampaignListRequest amznSpCampaignListRequest = new AmznSpCampaignListRequest();
        amznSpCampaignListRequest.setProfileId("1864675625344235");
//        amznSpCampaignListRequest.setNameFilter(new AmznCommonQueryTermMatchTypeFilter("EXACT_MATCH", Lists.newArrayList("")));
        amznSpCampaignListRequest.setMaxResults(10);
        AmznSpCampaignListResponse list = amznSpCampaignClient.list(amznSpCampaignListRequest);

        System.err.println(list);
    }
}
