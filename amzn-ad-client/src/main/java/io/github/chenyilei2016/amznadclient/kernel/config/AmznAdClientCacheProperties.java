package io.github.chenyilei2016.amznadclient.kernel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Amazon广告客户端缓存配置属性
 * 
 * <p>用于配置SDK的缓存行为,包括是否启用缓存、缓存大小、过期时间等。
 * 
 * <p>配置示例:
 * <pre>
 * amazon:
 *   ad:
 *     cache:
 *       enabled: true
 *       token:
 *         jvm-size: 10000
 *         jvm-expire-minutes: 1
 *         redis-expire-minutes: 55
 *         refresh-minutes: 50
 *       profile:
 *         jvm-size: 10000
 *         jvm-expire-seconds: 60
 *       credentials:
 *         jvm-size: 1000
 *         jvm-expire-minutes: 60
 * </pre>
 * 
 * @author chenyilei
 * @since 2025/12/31
 */
@Data
@ConfigurationProperties(prefix = "amazon.ad.cache")
public class AmznAdClientCacheProperties {
    
    /**
     * 是否启用缓存,默认false
     */
    private boolean enabled = false;
    
    /**
     * Token缓存配置
     */
    private TokenCacheConfig token = new TokenCacheConfig();
    
    /**
     * Profile详情缓存配置
     */
    private ProfileCacheConfig profile = new ProfileCacheConfig();
    
    /**
     * 认证凭证缓存配置
     */
    private CredentialsCacheConfig credentials = new CredentialsCacheConfig();
    
    /**
     * Token缓存配置
     */
    @Data
    public static class TokenCacheConfig {
        /**
         * JVM缓存大小
         */
        private int jvmSize = 10000;
        
        /**
         * JVM缓存过期时间(分钟)
         */
        private int jvmExpireMinutes = 1;
        
        /**
         * Redis缓存过期时间(分钟)
         */
        private int redisExpireMinutes = 55;
        
        /**
         * 自动刷新间隔(分钟)
         */
        private int refreshMinutes = 50;
        
        /**
         * 停止刷新的空闲时间(小时)
         */
        private int stopRefreshAfterHours = 8;
    }
    
    /**
     * Profile详情缓存配置
     */
    @Data
    public static class ProfileCacheConfig {
        /**
         * JVM缓存大小
         */
        private int jvmSize = 10000;
        
        /**
         * JVM缓存过期时间(秒)
         */
        private int jvmExpireSeconds = 60;
    }
    
    /**
     * 认证凭证缓存配置
     */
    @Data
    public static class CredentialsCacheConfig {
        /**
         * JVM缓存大小
         */
        private int jvmSize = 1000;
        
        /**
         * JVM缓存过期时间(分钟)
         */
        private int jvmExpireMinutes = 60;
    }
}
