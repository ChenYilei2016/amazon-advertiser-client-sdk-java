package io.github.chenyilei2016.amznadclient.kernel.spi.cache;

import com.alicp.jetcache.Cache;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.spi.IProfileDetailMetaProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 带缓存的Profile详情提供者装饰器
 *
 * <p>使用装饰器模式为原始的IProfileDetailMetaProvider添加缓存能力。
 *
 * <p>缓存策略:
 * <ul>
 *   <li>JVM缓存: 60秒过期</li>
 *   <li>缓存穿透保护: 防止缓存击穿</li>
 * </ul>
 *
 * <p>使用示例:
 * <pre>{@code
 * IProfileDetailMetaProvider delegate = new MyProfileDetailMetaProvider();
 * Cache<String, ProfileDetailMetaResponse> cache = ...;
 * IProfileDetailMetaProvider cached = new CachedProfileDetailMetaProvider(delegate, cache);
 *
 * ProfileDetailMetaResponse profile = cached.getProfileDetailMetaByProfileId("12345");
 * }</pre>
 *
 * @author chenyilei
 * @see IProfileDetailMetaProvider
 * @since 2025/12/31
 */
@Slf4j
@AllArgsConstructor
public class CachedProfileDetailMetaProvider implements IProfileDetailMetaProvider {

    /**
     * 被装饰的原始Provider
     */
    private final IProfileDetailMetaProvider delegate;

    /**
     * JetCache缓存实例
     */
    private final Cache<String, ProfileDetailMetaResponse> cache;

    /**
     * 获取Profile详情,优先从缓存获取
     *
     * @param profileId Amazon广告账户的profileId
     * @return Profile详细信息
     */
    @Override
    public ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId) {
        return cache.get(profileId);
    }
}
