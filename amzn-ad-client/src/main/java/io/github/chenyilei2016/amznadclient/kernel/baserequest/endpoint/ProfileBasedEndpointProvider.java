package io.github.chenyilei2016.amznadclient.kernel.baserequest.endpoint;

import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException;
import io.github.chenyilei2016.amznadclient.kernel.manager.IAmznAdvConfigManager;
import lombok.Getter;

/**
 * 基于ProfileId的Endpoint提供者
 *
 * <p>通过profileId从{@link IAmznAdvConfigManager}中查询对应的endpoint URL。
 * 这是最常用的endpoint获取方式,适用于大多数Amazon广告API调用场景。
 *
 * <p>使用示例:
 * <pre>{@code
 * AmznAdvConfigManager configManager = ...;
 * EndpointProvider provider = new ProfileBasedEndpointProvider(configManager, "12345");
 *
 * AmznBaseRequest request = AmznBaseRequest.builder()
 *     .endpointProvider(provider)
 *     .url("/sp/campaigns/list")
 *     .build();
 * }</pre>
 *
 * @author chenyilei
 * @date 2025/12/31
 * @see EndpointProvider
 */
@Getter
public class ProfileBasedEndpointProvider implements EndpointProvider {

    /**
     * Amazon广告配置管理器,用于查询profileId对应的配置信息
     */
    private final IAmznAdvConfigManager configManager;

    /**
     * Amazon广告账户的profileId
     */
    private final String profileId;

    public ProfileBasedEndpointProvider(IAmznAdvConfigManager configManager, String profileId) {
        this.configManager = configManager;
        this.profileId = profileId;
    }

    /**
     * 通过profileId从配置管理器获取endpoint URL
     *
     * @return endpoint URL前缀
     * @throws AmznApiException 如果profileId不存在
     */
    @Override
    public String getEndpointUrlPrefix() {
        ProfileDetailMetaResponse profileDetailMetaResponse = configManager.getProfileDetailMetaByProfileId(profileId);
        if (null == profileDetailMetaResponse) {
            throw AmznApiException.createBizException("不存在的店铺profileId:{}", profileId);
        }
        return profileDetailMetaResponse.getEndpointUrl();
    }
}
