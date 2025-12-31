package io.github.chenyilei2016.amznadclient.kernel.spi.impl;

import io.github.chenyilei2016.amznadclient.kernel.core.AmznAdAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAuthCredentialsProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * Amazon广告认证凭证提供者默认实现
 * 
 * <p>这是一个空实现,抛出异常提示用户需要提供自己的实现。
 * 外部系统应该通过Spring依赖注入替换此默认实现。
 * 
 * <p>使用示例:
 * <pre>{@code
 * @Component
 * public class MyAuthCredentialsProvider implements IAuthCredentialsProvider {
 *     
 *     @Value("${amazon.ad.client-id}")
 *     private String clientId;
 *     
 *     @Value("${amazon.ad.client-secret}")
 *     private String clientSecret;
 *     
 *     @Override
 *     public AmznAdAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId) {
 *         // 可以根据profileId返回不同的credentials,或者返回统一的credentials
 *         AmznAdAuthCredentialsResponse response = new AmznAdAuthCredentialsResponse();
 *         response.setAdvClientId(clientId);
 *         response.setAdvClientSecret(clientSecret);
 *         return response;
 *     }
 * }
 * }</pre>
 * 
 * @author chenyilei
 * @since 2025/12/31
 * @see IAuthCredentialsProvider
 */
@Slf4j
public class DefaultAuthCredentialsProvider implements IAuthCredentialsProvider {
    
    @Override
    public AmznAdAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId) {
        log.error("使用了默认的AuthCredentialsProvider实现,profileId: {}", profileId);
        throw AmznApiException.createBizException(
            "未提供AuthCredentialsProvider实现! 请实现IAuthCredentialsProvider接口并注入到Spring容器中。profileId: {}", 
            profileId
        );
    }
}
