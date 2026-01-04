package io.github.chenyilei2016.amznadclient.kernel.spi.cache;

import com.alicp.jetcache.Cache;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAmznTokenRequestProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 带缓存的Token请求提供者装饰器
 * 
 * <p>使用装饰器模式为原始的IAmznTokenRequestProvider添加缓存能力。
 * 支持JVM缓存和Redis缓存的多级缓存策略。
 * 
 * <p>缓存策略:
 * <ul>
 *   <li>JVM缓存: 1分钟过期</li>
 *   <li>Redis缓存: 55分钟过期</li>
 *   <li>自动刷新: 50分钟刷新一次</li>
 * </ul>
 * 
 * <p>使用示例:
 * <pre>{@code
 * IAmznTokenRequestProvider delegate = new DefaultAmznTokenRequestProvider();
 * Cache<AccessTokenMetaRequest, AmznTokenResponse> cache = ...;
 * IAmznTokenRequestProvider cached = new CachedTokenRequestProvider(delegate, cache);
 * 
 * AmznTokenResponse token = cached.doRefreshToken(request);
 * }</pre>
 * 
 * @author chenyilei
 * @since 2025/12/31
 * @see IAmznTokenRequestProvider
 */
@Slf4j
@AllArgsConstructor
public class CachedTokenRequestProvider implements IAmznTokenRequestProvider {
    
    /**
     * 被装饰的原始Provider
     */
    private final IAmznTokenRequestProvider delegate;
    
    /**
     * JetCache缓存实例
     */
    private final Cache<AccessTokenMetaRequest, AmznTokenResponse> cache;
    
    /**
     * 刷新Token,优先从缓存获取
     * 
     * <p>执行流程:
     * <ol>
     *   <li>尝试从缓存获取token</li>
     *   <li>如果缓存命中,直接返回</li>
     *   <li>如果缓存未命中,调用delegate获取token</li>
     *   <li>将获取的token放入缓存</li>
     * </ol>
     * 
     * @param request Token请求参数
     * @return Amazon访问令牌响应
     */
    @Override
    public AmznTokenResponse doRefreshToken(AccessTokenMetaRequest request) {
        return cache.get(request);
    }
}
