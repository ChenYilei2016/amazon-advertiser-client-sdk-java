package io.github.chenyilei2016.amznadclient.kernel.core;

/**
 * @author chenyilei
 * @date 2022/08/09 11:40
 */
public interface AmznConstants {

    /**
     * 请求传入 token
     */
    String HEADER_authorization = "Authorization";

    /**
     * token前缀
     */
    String HEADER_authorizationPrefix = "bearer ";

    /**
     * 请求传入clientId
     */
    String HEADER_clientId = "Amazon-Advertising-API-ClientId";


    String HEADER_marketplaceId = "Amazon-Advertising-API-MarketplaceId";

    String HEADER_instanceId = "Amazon-Marketing-Cloud-Audience-InstanceId";

    String HEADER_advertiserId = "Amazon-Advertising-API-AdvertiserId";

    String HEADER_entityId = "Amazon-Advertising-API-EntityId";


    /**
     * 一般使用 profileId
     */
    String HEADER_advertisingApiScope = "Amazon-Advertising-API-Scope";

    //////////////////////////// ↑ 请求传入

    /**
     * 返回携带
     */
    String HEADER_requestId = "x-amzn-RequestId";


}
