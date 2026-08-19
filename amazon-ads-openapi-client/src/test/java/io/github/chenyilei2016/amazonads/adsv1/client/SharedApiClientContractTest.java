package io.github.chenyilei2016.amazonads.adsv1.client;

import io.github.chenyilei2016.amazonads.adsv1.campaigns.api.CampaignsApi;
import io.github.chenyilei2016.amazonads.adsv1.targets.api.TargetsApi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SharedApiClientContractTest {

    @Test
    void shouldAllowCampaignsAndTargetsToShareOneApiClient() {
        ApiClient apiClient = new ApiClient();

        assertDoesNotThrow(() -> new CampaignsApi(apiClient));
        assertDoesNotThrow(() -> new TargetsApi(apiClient));
    }
}
