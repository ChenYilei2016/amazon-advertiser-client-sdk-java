package io.github.chenyilei2016.amznadclient.kernel.config;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.MultiLevelCacheBuilder;
import com.alicp.jetcache.RefreshPolicy;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.redis.springdata.RedisSpringDataCacheBuilder;
import com.alicp.jetcache.support.Fastjson2KeyConvertor;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAmznTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IProfileDetailMetaProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.cache.CachedAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.cache.CachedProfileDetailMetaProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.cache.CachedTokenRequestProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.concurrent.TimeUnit;

/**
 * Amazon广告客户端缓存配置类
 *
 * <p>此配置类负责创建缓存装饰器Bean,为数据提供者添加缓存能力。
 * 只有在配置了{@code amazon.ad.cache.enabled=true}时才会生效。
 *
 * <p>支持的缓存:
 * <ul>
 *   <li>JVM缓存 - 使用Caffeine,必需</li>
 *   <li>Redis缓存 - 可选,需要RedisConnectionFactory</li>
 * </ul>
 *
 * <p>配置示例:
 * <pre>
 * amazon:
 *   ad:
 *     cache:
 *       enabled: true
 * </pre>
 *
 * @author chenyilei
 * @see AmznAdClientCacheProperties
 * @since 2025/12/31
 */
@Slf4j
@Configuration
@ConditionalOnClass(Cache.class)
@ConditionalOnProperty(name = "aman.ad.client.cache.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AmznAdClientCacheProperties.class)
public class AmznAdClientCacheAutoConfiguration {


    /**
     * 创建带缓存的Token请求提供者
     *
     * <p>使用装饰器模式包装原始Provider,添加缓存层
     * <p>只有在token.enabled=true时才会创建此Bean
     *
     * <p>缓存策略:
     * <ul>
     *   <li>JVM缓存: 1分钟过期,10000条</li>
     *   <li>Redis缓存: 55分钟过期(如果有Redis)</li>
     *   <li>自动刷新: 50分钟刷新一次,8小时无访问后停止刷新</li>
     * </ul>
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "aman.ad.client.cache.token.enabled", havingValue = "true", matchIfMissing = true)
    public IAmznTokenRequestProvider cachedTokenRequestProvider(
            @Qualifier("amznTokenRequestProvider") IAmznTokenRequestProvider delegate,
            AmznAdClientCacheProperties properties,
            @Autowired(required = false) RedisConnectionFactory redisConnectionFactory) {
        AmznAdClientCacheProperties.TokenCacheConfig config = properties.getToken();

        log.info("创建tokenRequestProviderCache 详情缓存,配置: {}", config);

        MultiLevelCacheBuilder.MultiLevelCacheBuilderImpl multiLevelCacheBuilder = MultiLevelCacheBuilder.createMultiLevelCacheBuilder();

        // JVM缓存
        Cache<AccessTokenMetaRequest, AmznTokenResponse> jvmAdvTokenCache =
                CaffeineCacheBuilder.createCaffeineCacheBuilder()
                        .limit(config.getJvmSize())
                        .expireAfterWrite(config.getJvmExpireMinutes(), TimeUnit.MINUTES)
                        .buildCache();
        multiLevelCacheBuilder.addCache(jvmAdvTokenCache);

        // Redis缓存(可选)
        if (redisConnectionFactory != null) {
            log.info("检测到RedisConnectionFactory,启用Redis缓存");
            Cache<AccessTokenMetaRequest, AmznTokenResponse> redisAdvTokenCache = RedisSpringDataCacheBuilder.createBuilder()
                    .connectionFactory(redisConnectionFactory)
                    .expireAfterWrite(config.getRedisExpireMinutes(), TimeUnit.MINUTES)
                    .keyPrefix("amzn:adv:token:")
                    .keyConvertor(Fastjson2KeyConvertor.INSTANCE)
                    .buildCache();
            multiLevelCacheBuilder.addCache(redisAdvTokenCache);
        }

        Cache<AccessTokenMetaRequest, AmznTokenResponse> multiLevelCache = multiLevelCacheBuilder
                .loader(key -> delegate.doRefreshToken((AccessTokenMetaRequest) key))
                .refreshPolicy(RefreshPolicy.newPolicy(config.getRefreshMinutes(), TimeUnit.MINUTES)
                        .stopRefreshAfterLastAccess(config.getStopRefreshAfterHours(), TimeUnit.HOURS))
                .expireAfterWrite(config.getRedisExpireMinutes(), TimeUnit.MINUTES)
                .useExpireOfSubCache(true)
                .cachePenetrateProtect(true)
                .cacheNullValue(true)
                .buildCache();

        log.info("创建带缓存的Token请求提供者");
        return new CachedTokenRequestProvider(delegate, multiLevelCache);
    }

    /**
     * 创建带缓存的Profile详情提供者
     * <p>只有在profile.enabled=true时才会创建此Bean
     *
     * <p>缓存策略:
     * <ul>
     *   <li>JVM缓存: 1分钟过期,10000条</li>
     * </ul>
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "aman.ad.client.cache.profile.enabled", havingValue = "true", matchIfMissing = true)
    public IProfileDetailMetaProvider cachedProfileDetailMetaProvider(
            @Qualifier("amznProfileDetailMetaProvider") IProfileDetailMetaProvider delegate,
            AmznAdClientCacheProperties properties) {

        log.info("创建Profile详情缓存,配置: {}", properties.getProfile());

        AmznAdClientCacheProperties.ProfileCacheConfig config = properties.getProfile();
        Cache<String, ProfileDetailMetaResponse> cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .loader(profileId -> delegate.getProfileDetailMetaByProfileId((String) profileId))
                .limit(config.getJvmSize())
                .expireAfterWrite(config.getJvmExpireSeconds(), TimeUnit.SECONDS)
                .cachePenetrateProtect(true)
                .cacheNullValue(true)
                .buildCache();
        log.info("创建带缓存的Profile详情提供者");
        return new CachedProfileDetailMetaProvider(delegate, cache);
    }

    /**
     * 创建带缓存的认证凭证提供者
     * <p>只有在credentials.enabled=true时才会创建此Bean
     *
     * <p>缓存策略:
     * <ul>
     *   <li>JVM缓存: 60分钟过期,1000条</li>
     *   <li>缓存穿透保护</li>
     * </ul>
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "aman.ad.client.cache.credentials.enabled", havingValue = "true", matchIfMissing = true)
    public IAuthCredentialsProvider cachedAuthCredentialsProvider(@Qualifier("amznAuthCredentialsProvider") IAuthCredentialsProvider delegate,
            AmznAdClientCacheProperties properties) {

        AmznAdClientCacheProperties.CredentialsCacheConfig config = properties.getCredentials();
        Cache<String, AmznAuthCredentialsResponse> authCredentialsProviderCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .loader(profileId -> delegate.getAuthCredentialsByProfileId((String) profileId))
                .limit(config.getJvmSize())
                .expireAfterWrite(config.getJvmExpireMinutes(), TimeUnit.MINUTES)
                .cachePenetrateProtect(true)
                .cacheNullValue(true)
                .buildCache();

        log.info("创建带缓存的认证凭证提供者");
        return new CachedAuthCredentialsProvider(delegate, authCredentialsProviderCache);
    }
}
