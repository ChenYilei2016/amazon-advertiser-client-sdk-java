package io.github.chenyilei2016.amznadclient.kernel.manager.impl;

import io.github.chenyilei2016.amznadclient.kernel.spi.IAmznTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IProfileDetailMetaProvider;
import io.github.chenyilei2016.amznadclient.kernel.manager.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.utils.RestTemplateUtil;
import io.github.chenyilei2016.amznadclient.kernel.utils.TrustSSLConstant;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Amazon广告配置管理器实现类
 * 
 * <p>此类负责管理Amazon广告API的配置和token获取逻辑。
 * 它依赖于三个可插拔的接口:
 * <ul>
 *   <li>{@link IProfileDetailMetaProvider} - 提供Profile详情数据</li>
 *   <li>{@link IAuthCredentialsProvider} - 提供OAuth认证凭证</li>
 *   <li>{@link IAmznTokenRequestProvider} - 处理token刷新请求</li>
 * </ul>
 * 
 * <p>外部系统可以通过实现这些接口并注入到Spring容器中来自定义数据获取逻辑。
 * 
 * @author chenyilei
 * @since 2025/12/31
 * @see IAmznAdvConfigManager
 * @see IProfileDetailMetaProvider
 * @see IAuthCredentialsProvider
 * @see IAmznTokenRequestProvider
 */
public class AmznAdvConfigManagerImpl implements IAmznAdvConfigManager {

    /**
     * Token请求处理器,负责调用Amazon API刷新token
     */
    private final IAmznTokenRequestProvider amznTokenRequestHandler;
    
    /**
     * Profile详情数据提供者,负责获取Profile相关信息
     */
    private final IProfileDetailMetaProvider profileDetailMetaProvider;
    
    /**
     * 认证凭证提供者,负责获取OAuth clientId和clientSecret
     */
    private final IAuthCredentialsProvider authCredentialsProvider;

    /**
     * API调用的RestTemplate客户端
     */
    @Getter
    protected RestTemplate apiClient;

    /**
     * 构造函数
     * 
     * @param amznTokenRequestHandler Token请求处理器
     * @param profileDetailMetaProvider Profile详情数据提供者
     * @param authCredentialsProvider 认证凭证提供者
     */
    public AmznAdvConfigManagerImpl(
            IAmznTokenRequestProvider amznTokenRequestHandler,
            IProfileDetailMetaProvider profileDetailMetaProvider,
            IAuthCredentialsProvider authCredentialsProvider
    ) {
        this.amznTokenRequestHandler = amznTokenRequestHandler;
        this.profileDetailMetaProvider = profileDetailMetaProvider;
        this.authCredentialsProvider = authCredentialsProvider;
        
        // 初始化API客户端
        this.apiClient = new RestTemplate(new OkHttp3ClientHttpRequestFactory(new OkHttpClient().newBuilder()
                .connectionPool(RestTemplateUtil.pool())
                .connectTimeout(15 * 1000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .readTimeout(60 * 1000L, TimeUnit.MILLISECONDS)
                .writeTimeout(60 * 1000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(TrustSSLConstant.getTrustSSLContext().getSocketFactory(), TrustSSLConstant.getTrustX509TrustManager())
                .build())
        );
        RestTemplateUtil.converterUtf8(this.apiClient);
    }

    /**
     * 根据profileId获取Amazon广告访问令牌
     * 
     * <p>内部流程:
     * <ol>
     *   <li>通过{@link IAuthCredentialsProvider}获取OAuth认证凭证(clientId、clientSecret)</li>
     *   <li>通过{@link IProfileDetailMetaProvider}获取Profile详情(refreshToken等)</li>
     *   <li>构建{@link AccessTokenMetaRequest}对象</li>
     *   <li>调用{@link #getAdvTokenByAccessTokenMetaRequest}获取access token</li>
     * </ol>
     * 
     * @param profileId Amazon广告账户的profileId
     * @return Amazon访问令牌响应对象
     */
    @Override
    public AmznTokenResponse getAdvTokenByProfileId(String profileId) {
        // 1. 获取OAuth认证凭证
        AmznAuthCredentialsResponse authCredentials = this.getAuthCredentialsByProfileId(profileId);
        
        // 2. 获取Profile详情(包含refreshToken)
        ProfileDetailMetaResponse profileDetailMetaResponse = this.getProfileDetailMetaByProfileId(profileId);

        // 3. 构建AccessTokenMetaRequest
        AccessTokenMetaRequest accessTokenMetaRequest = new AccessTokenMetaRequest();
        accessTokenMetaRequest.setGrantType(authCredentials.getGrantType());
        accessTokenMetaRequest.setClientId(authCredentials.getAdvClientId());
        accessTokenMetaRequest.setClientSecret(authCredentials.getAdvClientSecret());
        accessTokenMetaRequest.setRefreshToken(profileDetailMetaResponse.getAdvRefreshToken());
        
        // 4. 获取access token
        return this.getAdvTokenByAccessTokenMetaRequest(accessTokenMetaRequest);
    }

    /**
     * 根据AccessTokenMetaRequest获取Amazon广告访问令牌
     * 
     * <p>此方法委托给{@link IAmznTokenRequestProvider}来执行实际的token刷新请求。
     * 
     * @param accessTokenMetaRequest 包含clientId、clientSecret、refreshToken等信息的请求对象
     * @return Amazon访问令牌响应对象
     */
    @Override
    public AmznTokenResponse getAdvTokenByAccessTokenMetaRequest(AccessTokenMetaRequest accessTokenMetaRequest) {
        return amznTokenRequestHandler.doRefreshToken(accessTokenMetaRequest);
    }

    /**
     * 根据profileId获取Profile详细信息
     * 
     * <p>此方法委托给{@link IProfileDetailMetaProvider}来获取Profile数据。
     * 
     * @param profileId Amazon广告账户的profileId
     * @return Profile详细信息
     */
    @Override
    public ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId) {
        return profileDetailMetaProvider.getProfileDetailMetaByProfileId(profileId);
    }

    /**
     * 根据profileId获取OAuth认证凭证
     * 
     * <p>此方法委托给{@link IAuthCredentialsProvider}来获取认证凭证。
     * 
     * @param profileId Amazon广告账户的profileId
     * @return OAuth认证凭证
     */
    @Override
    public AmznAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId) {
        return authCredentialsProvider.getAuthCredentialsByProfileId(profileId);
    }
}
