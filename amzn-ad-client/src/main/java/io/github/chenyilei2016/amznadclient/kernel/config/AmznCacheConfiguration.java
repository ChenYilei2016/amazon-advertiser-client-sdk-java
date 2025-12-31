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
@ConditionalOnProperty(name = "amazon.ad.cache.enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(AmznAdClientCacheProperties.class)
public class AmznCacheConfiguration {

    /**
     * 创建Token请求缓存
     *
     * <p>缓存策略:
     * <ul>
     *   <li>JVM缓存: 1分钟过期,10000条</li>
     *   <li>Redis缓存: 55分钟过期(如果有Redis)</li>
     *   <li>自动刷新: 50分钟刷新一次,8小时无访问后停止刷新</li>
     * </ul>
     */
    @Bean
    public Cache<AccessTokenMetaRequest, AmznTokenResponse> tokenRequestProviderCache(
            AmznAdClientCacheProperties properties,
            @Autowired(required = false) RedisConnectionFactory redisConnectionFactory) {

        AmznAdClientCacheProperties.TokenCacheConfig config = properties.getToken();

        log.info("创建tokenRequestProviderCache 详情缓存,配置: {}", config);

        MultiLevelCacheBuilder.MultiLevelCacheBuilderImpl builder = MultiLevelCacheBuilder.createMultiLevelCacheBuilder();

        // JVM缓存
        Cache<AccessTokenMetaRequest, AmznTokenResponse> jvmCache =
                CaffeineCacheBuilder.createCaffeineCacheBuilder()
                        .limit(config.getJvmSize())
                        .expireAfterWrite(config.getJvmExpireMinutes(), TimeUnit.MINUTES)
                        .buildCache();
        builder.addCache(jvmCache);

        // Redis缓存(可选)
        if (redisConnectionFactory != null) {
            log.info("检测到RedisConnectionFactory,启用Redis缓存");
            Cache<AccessTokenMetaRequest, AmznTokenResponse> redisCache = RedisSpringDataCacheBuilder.createBuilder()
                    .connectionFactory(redisConnectionFactory)
                    .expireAfterWrite(config.getRedisExpireMinutes(), TimeUnit.MINUTES)
                    .keyPrefix("amzn:adv:token:")
                    .keyConvertor(Fastjson2KeyConvertor.INSTANCE)
                    .buildCache();
            builder.addCache(redisCache);
        }

        return builder.refreshPolicy(RefreshPolicy.newPolicy(config.getRefreshMinutes(), TimeUnit.MINUTES)
                        .stopRefreshAfterLastAccess(config.getStopRefreshAfterHours(), TimeUnit.HOURS))
                .expireAfterWrite(config.getRedisExpireMinutes(), TimeUnit.MINUTES)
                .useExpireOfSubCache(true)
                .cachePenetrateProtect(true)
                .buildCache();
    }

    /**
     * 创建Profile详情缓存
     *
     * <p>缓存策略:
     * <ul>
     *   <li>JVM缓存: 60秒过期,10000条</li>
     *   <li>缓存穿透保护</li>
     * </ul>
     */
    @Bean
    public Cache<String, ProfileDetailMetaResponse> profileDetailMetaProviderCache(
            AmznAdClientCacheProperties properties) {

        log.info("创建Profile详情缓存,配置: {}", properties.getProfile());

        AmznAdClientCacheProperties.ProfileCacheConfig config = properties.getProfile();
        return CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(config.getJvmSize())
                .expireAfterWrite(config.getJvmExpireSeconds(), TimeUnit.SECONDS)
                .cachePenetrateProtect(true)
                .buildCache();
    }

    /**
     * 创建认证凭证缓存
     *
     * <p>缓存策略:
     * <ul>
     *   <li>JVM缓存: 60分钟过期,1000条</li>
     *   <li>缓存穿透保护</li>
     * </ul>
     */
    @Bean
    public Cache<String, AmznAuthCredentialsResponse> authCredentialsProviderCache(
            AmznAdClientCacheProperties properties) {

        log.info("创建认证凭证缓存,配置: {}", properties.getCredentials());

        AmznAdClientCacheProperties.CredentialsCacheConfig config = properties.getCredentials();
        return CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(config.getJvmSize())
                .expireAfterWrite(config.getJvmExpireMinutes(), TimeUnit.MINUTES)
                .cachePenetrateProtect(true)
                .buildCache();
    }

    /**
     * 创建带缓存的Token请求提供者
     *
     * <p>使用装饰器模式包装原始Provider,添加缓存层
     */
    @Bean
    @Primary
    public IAmznTokenRequestProvider cachedTokenRequestProvider(
            @Qualifier("amznTokenRequestProvider") IAmznTokenRequestProvider delegate,
            Cache<AccessTokenMetaRequest, AmznTokenResponse> tokenRequestProviderCache) {
        log.info("创建带缓存的Token请求提供者");
        return new CachedTokenRequestProvider(delegate, tokenRequestProviderCache);
    }

    /**
     * 创建带缓存的Profile详情提供者
     */
    @Bean
    @Primary
    public IProfileDetailMetaProvider cachedProfileDetailMetaProvider(
            @Qualifier("profileDetailMetaProvider") IProfileDetailMetaProvider delegate,
            Cache<String, ProfileDetailMetaResponse> profileDetailMetaProviderCache) {
        log.info("创建带缓存的Profile详情提供者");
        return new CachedProfileDetailMetaProvider(delegate, profileDetailMetaProviderCache);
    }

    /**
     * 创建带缓存的认证凭证提供者
     */
    @Bean
    @Primary
    public IAuthCredentialsProvider cachedAuthCredentialsProvider(
            @Qualifier("authCredentialsProvider") IAuthCredentialsProvider delegate,
            Cache<String, AmznAuthCredentialsResponse> authCredentialsProviderCache) {
        log.info("创建带缓存的认证凭证提供者");
        return new CachedAuthCredentialsProvider(delegate, authCredentialsProviderCache);
    }
}
