package io.github.chenyilei2016.amznadclient.kernel.manager;

import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznAdAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenyilei
 * @since 2025/12/31 14:00
 */
public interface IAmznAdvConfigManager {


    RestTemplate getApiClient();

    /**
     * 根据profileId 获取token
     * 实际委托给{@link IAmznAdvConfigManager#getAdvTokenByAccessTokenMetaRequest(AccessTokenMetaRequest)} 执行
     */
    AmznTokenResponse getAdvTokenByProfileId(String profileId);

    /**
     * 获取token
     */
    AmznTokenResponse getAdvTokenByAccessTokenMetaRequest(AccessTokenMetaRequest accessTokenMetaRequest);

    /**
     * 根据profileId 获取profile信息
     */
    ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId);

    /**
     * 根据profileId 获取clientId 和 clientSecret 等oauth信息
     */
    AmznAdAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId);

}
