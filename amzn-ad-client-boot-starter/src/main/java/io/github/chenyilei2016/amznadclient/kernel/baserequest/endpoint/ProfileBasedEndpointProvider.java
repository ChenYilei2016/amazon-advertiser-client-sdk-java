package io.github.chenyilei2016.amznadclient.kernel.baserequest.endpoint;

import io.github.chenyilei2016.amznadclient.AmznAdClient;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException;
import io.github.chenyilei2016.amznadclient.kernel.manager.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.support.AmznAdClientAware;
import lombok.Getter;

/**
 * 基于ProfileId的Endpoint提供者
 *
 * <p>通过profileId从{@link IAmznAdvConfigManager}中查询对应的endpoint URL。
 * 这是最常用的endpoint获取方式,适用于大多数Amazon广告API调用场景。
 *
 * <p>使用示例:
 * <pre>{@code
 * // 方式1: 手动传入configManager
 * EndpointProvider provider = new ProfileBasedEndpointProvider(configManager, "12345");
 *
 * // 方式2: 通过AmznAdClient.newRequest()自动注入(推荐)
 * AmznBaseRequest request = amznAdClient.newRequest()
 *     .endpointProvider(new ProfileBasedEndpointProvider("12345"))
 *     .url("/sp/campaigns/list")
 *     .build();
 * }</pre>
 *
 * @author chenyilei
 * @date 2025/12/31
 * @see EndpointProvider
 * @see IAmznAdvConfigManager
 */
@Getter
public class ProfileBasedEndpointProvider implements EndpointProvider, AmznAdClientAware {

    /**
     * Amazon广告配置管理器,用于查询profileId对应的配置信息
     */
    private IAmznAdvConfigManager configManager;

    /**
     * Amazon广告账户的profileId
     */
    private final String profileId;

    /**
     * 构造函数 - 仅指定profileId
     * <p>configManager会通过AmznAdClientAware自动注入
     *
     * @param profileId Amazon广告账户的profileId
     */
    public ProfileBasedEndpointProvider(String profileId) {
        this.profileId = profileId;
    }

    /**
     * 构造函数 - 手动指定configManager和profileId
     *
     * @param configManager Amazon广告配置管理器
     * @param profileId Amazon广告账户的profileId
     */
    public ProfileBasedEndpointProvider(IAmznAdvConfigManager configManager, String profileId) {
        this.configManager = configManager;
        this.profileId = profileId;
    }

    @Override
    public void setAmznAdClient(AmznAdClient amznAdClient) {
        if (this.configManager == null) {
            this.configManager = amznAdClient.getAmznAdvConfigManager();
        }
    }

    /**
     * 通过profileId从配置管理器获取endpoint URL
     *
     * @return endpoint URL前缀
     * @throws AmznApiException 如果profileId不存在
     */
    @Override
    public String getEndpointUrlPrefix() {
        if (configManager == null) {
            throw new IllegalStateException("configManager未设置,请使用AmznAdClient.newRequest()创建请求或手动传入configManager");
        }
        ProfileDetailMetaResponse profileDetailMetaResponse = configManager.getProfileDetailMetaByProfileId(profileId);
        if (null == profileDetailMetaResponse) {
            throw AmznApiException.createBizException("不存在的店铺profileId:{}", profileId);
        }
        return profileDetailMetaResponse.getEndpointUrl();
    }
}
