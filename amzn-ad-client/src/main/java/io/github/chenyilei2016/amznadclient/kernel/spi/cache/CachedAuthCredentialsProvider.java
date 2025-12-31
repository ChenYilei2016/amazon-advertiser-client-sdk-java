package io.github.chenyilei2016.amznadclient.kernel.spi.cache;

import com.alicp.jetcache.Cache;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAuthCredentialsProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 带缓存的认证凭证提供者装饰器
 * 
 * <p>使用装饰器模式为原始的IAuthCredentialsProvider添加缓存能力。
 * 
 * <p>缓存策略:
 * <ul>
 *   <li>JVM缓存: 60分钟过期</li>
 *   <li>缓存大小: 1000个条目</li>
 * </ul>
 * 
 * <p>使用示例:
 * <pre>{@code
 * IAuthCredentialsProvider delegate = new MyAuthCredentialsProvider();
 * Cache<String, AmznAdAuthCredentialsResponse> cache = ...;
 * IAuthCredentialsProvider cached = new CachedAuthCredentialsProvider(delegate, cache);
 * 
 * AmznAdAuthCredentialsResponse credentials = cached.getAuthCredentialsByProfileId("12345");
 * }</pre>
 * 
 * @author chenyilei
 * @since 2025/12/31
 * @see IAuthCredentialsProvider
 */
@Slf4j
@AllArgsConstructor
public class CachedAuthCredentialsProvider implements IAuthCredentialsProvider {
    
    /**
     * 被装饰的原始Provider
     */
    private final IAuthCredentialsProvider delegate;
    
    /**
     * JetCache缓存实例
     */
    private final Cache<String, AmznAuthCredentialsResponse> cache;
    
    /**
     * 获取认证凭证,优先从缓存获取
     * 
     * @param profileId Amazon广告账户的profileId
     * @return OAuth认证凭证
     */
    @Override
    public AmznAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId) {
        return cache.computeIfAbsent(profileId, key -> {
            log.debug("认证凭证缓存未命中,调用原始Provider获取: {}", profileId);
            return delegate.getAuthCredentialsByProfileId(key);
        });
    }
}
