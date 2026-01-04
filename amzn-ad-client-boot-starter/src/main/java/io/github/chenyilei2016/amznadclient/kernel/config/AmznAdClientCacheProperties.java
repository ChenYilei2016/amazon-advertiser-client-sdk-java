package io.github.chenyilei2016.amznadclient.kernel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Amazon广告客户端缓存配置属性
 * 
 * <p>用于配置SDK的缓存行为,包括是否启用缓存、缓存大小、过期时间等。
 * 每个缓存都可以独立启用或禁用。
 * 
 * <p>配置示例:
 * <pre>
 * amazon:
 *   ad:
 *     cache:
 *       enabled: true  # 全局开关
 *       token:
 *         enabled: true  # Token缓存独立开关
 *         jvm-size: 10000
 *         jvm-expire-minutes: 1
 *         redis-expire-minutes: 55
 *         refresh-minutes: 50
 *       profile:
 *         enabled: true  # Profile缓存独立开关
 *         jvm-size: 10000
 *         jvm-expire-seconds: 60
 *       credentials:
 *         enabled: false  # 可以单独禁用某个缓存
 *         jvm-size: 1000
 *         jvm-expire-minutes: 60
 * </pre>
 * 
 * @author chenyilei
 * @since 2025/12/31
 */
@Data
@ConfigurationProperties(prefix = "aman.ad.client.cache")
public class AmznAdClientCacheProperties {
    
    /**
     * 全局缓存开关,默认false
     * <p>如果为false,所有缓存都不会启用,即使单个缓存的enabled为true
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
         * 是否启用Token缓存,默认true
         * <p>只有在全局enabled=true时才生效
         */
        private boolean enabled = true;
        
        /**
         * JVM缓存大小
         */
        private int jvmSize = 15000;
        
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
         * 是否启用Profile缓存,默认true
         * <p>只有在全局enabled=true时才生效
         */
        private boolean enabled = true;
        
        /**
         * JVM缓存大小
         */
        private int jvmSize = 15000;
        
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
         * 是否启用认证凭证缓存,默认true
         * <p>只有在全局enabled=true时才生效
         */
        private boolean enabled = true;
        
        /**
         * JVM缓存大小
         */
        private int jvmSize = 15000;
        
        /**
         * JVM缓存过期时间(分钟)
         */
        private int jvmExpireMinutes = 60;
    }
}
