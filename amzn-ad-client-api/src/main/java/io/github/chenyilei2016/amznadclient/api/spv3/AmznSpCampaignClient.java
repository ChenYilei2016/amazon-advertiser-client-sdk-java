package io.github.chenyilei2016.amznadclient.api.spv3;

import io.github.chenyilei2016.amznadclient.AmznAdClient;
import io.github.chenyilei2016.amznadclient.AmznBaseRequest;
import io.github.chenyilei2016.amznadclient.kernel.advice.AmznClientCrudTypeEnum;
import io.github.chenyilei2016.amznadclient.kernel.baserequest.endpoint.ProfileBasedEndpointProvider;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznHeaderEnum;
import io.github.chenyilei2016.amznadclient.kernel.baserequest.token.ProfileBasedTokenProvider;
import io.github.chenyilei2016.amznadclient.kernel.validate.ValidateBean;
import io.github.chenyilei2016.amznadclient.model.spv3.*;
import org.springframework.stereotype.Component;

/**
 * @author chenyilei
 * @date 2023/04/14 16:39
 */
@Component
public class AmznSpCampaignClient {
    private final AmznAdClient amznAdClient;

    public AmznSpCampaignClient(AmznAdClient amznAdClient) {
        this.amznAdClient = amznAdClient;
    }

    /**
     * <a href="https://advertising.amazon.com/API/docs/en-us/sponsored-products/3-0/openapi/prod#/Campaigns">...</a>
     */
    public AmznSpCampaignListResponse list(AmznSpCampaignListRequest amznSpCampaignListRequest) {
        ValidateBean.validateThrow(amznSpCampaignListRequest);

        AmznBaseRequest amznBaseRequest = amznAdClient.newRequest()
                .tokenProvider(new ProfileBasedTokenProvider(amznSpCampaignListRequest.getProfileId()))
                .endpointProvider(new ProfileBasedEndpointProvider(amznSpCampaignListRequest.getProfileId()))
                .url("/sp/campaigns/list")
                .mediaType("application/vnd.spCampaign.v3+json")
                .headerValue(AmznHeaderEnum.PREFER_RETURN_REPRESENTATION.getKey(), AmznHeaderEnum.PREFER_RETURN_REPRESENTATION.getValue())
                .jsonBodyObject(amznSpCampaignListRequest)
                .crudTypeEnum(AmznClientCrudTypeEnum.QUERY);

        return amznAdClient.getResultGson().fromJson(amznAdClient.httpPost(amznBaseRequest), AmznSpCampaignListResponse.class);
    }


    public AmznSpCampaignChangeResponse update(AmznSpCampaignUpdateRequest amznSpCampaignUpdateRequest) {
        ValidateBean.validateThrow(amznSpCampaignUpdateRequest);
        AmznBaseRequest amznBaseRequest = amznAdClient.newRequest()
                .tokenProvider(new ProfileBasedTokenProvider(amznSpCampaignUpdateRequest.getProfileId()))
                .endpointProvider(new ProfileBasedEndpointProvider(amznSpCampaignUpdateRequest.getProfileId()))
                .url("/sp/campaigns")
                .mediaType("application/vnd.spCampaign.v3+json")
                .headerValue(AmznHeaderEnum.PREFER_RETURN_REPRESENTATION.getKey(), AmznHeaderEnum.PREFER_RETURN_REPRESENTATION.getValue())
                .jsonBodyObject(amznSpCampaignUpdateRequest)
                .crudTypeEnum(AmznClientCrudTypeEnum.UPDATE);

        return amznAdClient.beforeReturnResult(AmznClientCrudTypeEnum.UPDATE, amznAdClient.getResultGson().fromJson(amznAdClient.httpPut(amznBaseRequest), AmznSpCampaignChangeResponse.class));
    }


    public AmznSpCampaignChangeResponse create(AmznSpCampaignCreateRequest amznSpCampaignCreateRequest) {
        ValidateBean.validateThrow(amznSpCampaignCreateRequest);
        AmznBaseRequest amznBaseRequest = amznAdClient.newRequest()
                .tokenProvider(new ProfileBasedTokenProvider(amznAdClient.getAmznAdvConfigManager(), amznSpCampaignCreateRequest.getProfileId()))
                .endpointProvider(new ProfileBasedEndpointProvider(amznAdClient.getAmznAdvConfigManager(), amznSpCampaignCreateRequest.getProfileId()))
                .url("/sp/campaigns")
                .mediaType("application/vnd.spCampaign.v3+json")
                .headerValue(AmznHeaderEnum.PREFER_RETURN_REPRESENTATION.getKey(), AmznHeaderEnum.PREFER_RETURN_REPRESENTATION.getValue())
                .jsonBodyObject(amznSpCampaignCreateRequest)
                .resultLogPrintLengthNoLimit()
                .crudTypeEnum(AmznClientCrudTypeEnum.CREATE);

        AmznSpCampaignChangeResponse amznSpCampaignChangeResponse = amznAdClient.getResultGson().fromJson(amznAdClient.httpPost(amznBaseRequest), AmznSpCampaignChangeResponse.class);
        return amznAdClient.beforeReturnResult(AmznClientCrudTypeEnum.CREATE, amznSpCampaignChangeResponse);
    }


    public AmznSpCampaignChangeResponse delete(AmznSpCampaignDeleteRequest amznSpCampaignDeleteRequest) {
        ValidateBean.validateThrow(amznSpCampaignDeleteRequest);
        AmznBaseRequest amznBaseRequest = amznAdClient.newRequest()
                .tokenProvider(new ProfileBasedTokenProvider(amznAdClient.getAmznAdvConfigManager(), amznSpCampaignDeleteRequest.getProfileId()))
                .endpointProvider(new ProfileBasedEndpointProvider(amznAdClient.getAmznAdvConfigManager(), amznSpCampaignDeleteRequest.getProfileId()))
                .url("/sp/campaigns/delete")
                .mediaType("application/vnd.spCampaign.v3+json")
                .jsonBodyObject(amznSpCampaignDeleteRequest)
                .crudTypeEnum(AmznClientCrudTypeEnum.DELETE);

        AmznSpCampaignChangeResponse amznSpCampaignChangeResponse = amznAdClient.getResultGson().fromJson(amznAdClient.httpPost(amznBaseRequest), AmznSpCampaignChangeResponse.class);
        return amznAdClient.beforeReturnResult(AmznClientCrudTypeEnum.DELETE, amznSpCampaignChangeResponse);
    }

}
