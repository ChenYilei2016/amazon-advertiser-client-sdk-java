package io.github.chenyilei2016.amznadclient.kernel.baserequest.token;

import io.github.chenyilei2016.amznadclient.kernel.cache.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznConstants;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

/**
 * 基于ProfileId的Token提供者
 *
 * <p>这是最常用的token获取方式,通过profileId从{@link AmznAdvConfigManager}中查询
 * 相关的配置信息(如endpoint、refreshToken、accountType等),然后获取访问令牌。
 *
 * <p>此实现对应于SDK中的第一种token获取方式,适用于大多数Amazon广告API调用场景。
 *
 * <p>使用示例:
 * <pre>{@code
 * AmznAdvConfigManager configManager = ...;
 * TokenProvider provider = new ProfileBasedTokenProvider(configManager, "12345");
 *
 * AmznBaseRequest request = AmznBaseRequest.builder()
 *     .tokenProvider(provider)
 *     .url("/sp/campaigns/list")
 *     .build();
 * }</pre>
 *
 * @author chenyilei
 * @date 2025/12/31
 * @see TokenProvider
 * @see AmznAdvConfigManager
 */
@Getter
public class ProfileBasedTokenProvider implements TokenProvider {

    /**
     * Amazon广告配置管理器,用于查询profileId对应的配置信息
     */
    private final IAmznAdvConfigManager configManager;

    /**
     * Amazon广告账户的profileId
     */
    private final String profileId;


    public ProfileBasedTokenProvider(IAmznAdvConfigManager configManager, String profileId) {
        this.configManager = configManager;
        this.profileId = profileId;
    }

    /**
     * 通过profileId从配置管理器获取访问令牌
     *
     * <p>内部流程:
     * <ol>
     *   <li>通过profileId查询ProfileDetailMeta(包含endpoint、refreshToken等)</li>
     *   <li>根据accountType获取对应的clientId和clientSecret</li>
     *   <li>调用Amazon token API获取access_token</li>
     *   <li>返回包含access_token和clientId的响应对象</li>
     * </ol>
     *
     * @return Amazon访问令牌响应对象
     * @throws io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException 如果profileId不存在或获取token失败
     */
    @Override
    public AmznTokenResponse getAccessToken() {
        return configManager.getAdvTokenByProfileId(profileId);
    }

    @Override
    public void customizeHttpHeaders(HttpHeaders httpHeaders) {
        if (profileId != null) {
            httpHeaders.add(AmznConstants.HEADER_advertisingApiScope, this.profileId);
        }
    }

}
