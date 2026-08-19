package io.github.chenyilei2016.amazonads.adsv1.campaigns.api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CampaignsApiContractTest {

    @Test
    void shouldExposeOnlyTheFourCampaignManagementOperations() {
        Set<String> operationNames = Arrays.stream(CampaignsApi.class.getDeclaredMethods())
                .map(method -> method.getName())
                .filter(name -> Set.of("createCampaign", "deleteCampaign", "queryCampaign", "updateCampaign").contains(name))
                .collect(Collectors.toSet());

        assertEquals(Set.of("createCampaign", "deleteCampaign", "queryCampaign", "updateCampaign"), operationNames);
    }
}
