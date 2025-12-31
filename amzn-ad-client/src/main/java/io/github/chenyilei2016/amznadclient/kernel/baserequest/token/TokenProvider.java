package io.github.chenyilei2016.amznadclient.kernel.baserequest.token;

import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import org.springframework.http.HttpHeaders;

/**
 * Token提供者接口 - 策略模式
 * 
 * <p>定义了获取Amazon广告API访问令牌的统一接口。不同的实现类可以采用不同的策略来获取token:
 * <ul>
 *   <li>基于profileId从配置管理器获取</li>
 *   <li>直接使用clientId/clientSecret/refreshToken获取</li>
 *   <li>用户自定义的token获取逻辑</li>
 * </ul>
 * 
 * <p>使用示例:
 * <pre>{@code
 * // 使用profileId
 * TokenProvider provider = new ProfileBasedTokenProvider(configManager, "12345");
 * 
 * // 使用直接credentials
 * TokenProvider provider = DirectCredentialsTokenProvider.builder()
 *     .configManager(configManager)
 *     .clientId("xxx")
 *     .clientSecret("yyy")
 *     .refreshToken("zzz")
 *     .build();
 *     
 * // 自定义token获取
 * TokenProvider provider = new CustomTokenProvider(() -> myCustomTokenLogic(), null);
 * }</pre>
 * 
 * @author chenyilei
 * @date 2025/12/31
 * @see ProfileBasedTokenProvider
 * @see DirectCredentialsTokenProvider
 */
public interface TokenProvider {
    
    /**
     * 获取Amazon广告API访问令牌
     * 
     * <p>此方法负责获取有效的访问令牌。实现类应该处理token的刷新、缓存等逻辑。
     * 
     * @return Amazon访问令牌响应对象,包含access_token和clientId等信息
     * @throws io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException 
     *         如果获取token失败
     */
    AmznTokenResponse getAccessToken();



    void customizeHttpHeaders(HttpHeaders httpHeaders);
}
