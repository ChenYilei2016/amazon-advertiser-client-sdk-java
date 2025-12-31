package io.github.chenyilei2016.amznadclient.kernel.baserequest.token;

import io.github.chenyilei2016.amznadclient.kernel.cache.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

/**
 * 直接使用Credentials的Token提供者
 *
 * <p>此实现允许用户直接提供clientId、clientSecret和refreshToken来获取访问令牌,
 * 而不需要依赖profileId和配置管理器中的映射关系。
 *
 * <p>这种方式适用于以下场景:
 * <ul>
 *   <li>调用不需要profileId的Amazon广告API(如账户管理API)</li>
 *   <li>使用临时或动态的credentials</li>
 *   <li>测试环境中使用特定的账户凭证</li>
 * </ul>
 *
 * <p>此实现对应于SDK中的第二种token获取方式(使用SpecialClientDetail)。
 *
 * <p>使用示例:
 * <pre>{@code
 * TokenProvider provider = DirectCredentialsTokenProvider.builder()
 *     .configManager(configManager)
 *     .clientId("amzn1.application-oa2-client.xxx")
 *     .clientSecret("your-client-secret")
 *     .refreshToken("Atzr|xxx")
 *     .profileId("12345") // 可选,某些API需要
 *     .build();
 *
 * AmznBaseRequest request = AmznBaseRequest.builder()
 *     .tokenProvider(provider)
 *     .url("/v2/profiles")
 *     .build();
 * }</pre>
 *
 * @author chenyilei
 * @date 2025/12/31
 * @see TokenProvider
 * @see AccessTokenMetaRequest
 */
@Builder
@Getter
public class DirectCredentialsTokenProvider implements TokenProvider {

    /**
     * Amazon广告配置管理器,用于调用token刷新API
     */
    private final IAmznAdvConfigManager configManager;

    /**
     * Amazon广告应用的Client ID
     */
    private final String clientId;

    /**
     * Amazon广告应用的Client Secret
     */
    private final String clientSecret;

    /**
     * Amazon广告账户的Refresh Token
     */
    private final String refreshToken;

    public DirectCredentialsTokenProvider(IAmznAdvConfigManager configManager, String clientId, String clientSecret, String refreshToken) {
        this.configManager = configManager;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
    }

    /**
     * 直接使用clientId、clientSecret和refreshToken获取访问令牌
     *
     * <p>内部流程:
     * <ol>
     *   <li>构建AccessTokenRequestMeta对象</li>
     *   <li>调用Amazon token API获取access_token</li>
     *   <li>返回包含access_token和clientId的响应对象</li>
     * </ol>
     *
     * @return Amazon访问令牌响应对象
     * @throws io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException 如果credentials无效或获取token失败
     */
    @Override
    public AmznTokenResponse getAccessToken() {
        AccessTokenMetaRequest meta = AccessTokenMetaRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .refreshToken(refreshToken)
                .build();
        return configManager.getAdvTokenByAccessTokenMetaRequest(meta);
    }

    @Override
    public void customizeHttpHeaders(HttpHeaders httpHeaders) {
//        if (profileId != null) {
//            httpHeaders.add(AmznConstants.HEADER_advertisingApiScope, this.profileId);
//        }
    }

}
