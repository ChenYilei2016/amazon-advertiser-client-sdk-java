package io.github.chenyilei2016.amznadclient.kernel.config;

import io.github.chenyilei2016.amznadclient.AmznAdClient;
import io.github.chenyilei2016.amznadclient.kernel.manager.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.manager.impl.AmznAdvConfigManagerImpl;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAmznTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IProfileDetailMetaProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultAmznTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultProfileDetailMetaProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Amazon广告客户端自动配置类
 *
 * <p>此配置类负责初始化Amazon广告SDK所需的核心Bean。
 * 所有Bean都使用{@link ConditionalOnMissingBean}注解,允许外部系统通过自定义实现来替换默认实现。
 *
 * <p>可替换的Bean:
 * <ul>
 *   <li>{@link IAmznTokenRequestProvider} - Token刷新请求处理器</li>
 *   <li>{@link IProfileDetailMetaProvider} - Profile详情数据提供者</li>
 *   <li>{@link IAuthCredentialsProvider} - OAuth认证凭证提供者</li>
 *   <li>{@link IAmznAdvConfigManager} - 配置管理器</li>
 *   <li>{@link AmznAdClient} - 广告API客户端</li>
 * </ul>
 *
 * <p>使用示例 - 替换默认实现:
 * <pre>{@code
 * @Configuration
 * public class MyAmznAdConfig {
 *
 *     // 替换Profile数据提供者
 *     @Bean
 *     public IProfileDetailMetaProvider profileDetailMetaProvider() {
 *         return new MyProfileDetailMetaProvider();
 *     }
 *
 *     // 替换认证凭证提供者
 *     @Bean
 *     public IAuthCredentialsProvider authCredentialsProvider() {
 *         return new MyAuthCredentialsProvider();
 *     }
 * }
 * }</pre>
 *
 * @author chenyilei
 * @see IAmznTokenRequestProvider
 * @see IProfileDetailMetaProvider
 * @see IAuthCredentialsProvider
 * @see IAmznAdvConfigManager
 * @see AmznAdClient
 * @since 2025/12/31
 */
@Configuration
public class AmznAdClientConfiguration {

    /**
     * 创建Token请求处理器Bean
     *
     * <p>默认使用{@link DefaultAmznTokenRequestProvider},负责调用Amazon API刷新token。
     *
     * @return Token请求处理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public IAmznTokenRequestProvider amznTokenRequestHandler() {
        return new DefaultAmznTokenRequestProvider();
    }

    /**
     * 创建Profile详情数据提供者Bean
     *
     * <p>默认使用{@link DefaultProfileDetailMetaProvider},会抛出异常提示用户提供自己的实现。
     * <p><strong>外部系统必须提供自己的实现!</strong>
     *
     * @return Profile详情数据提供者实例
     */
    @Bean
    @ConditionalOnMissingBean
    public IProfileDetailMetaProvider profileDetailMetaProvider() {
        return new DefaultProfileDetailMetaProvider();
    }

    /**
     * 创建OAuth认证凭证提供者Bean
     *
     * <p>默认使用{@link DefaultAuthCredentialsProvider},会抛出异常提示用户提供自己的实现。
     * <p><strong>外部系统必须提供自己的实现!</strong>
     *
     * @return OAuth认证凭证提供者实例
     */
    @Bean
    @ConditionalOnMissingBean
    public IAuthCredentialsProvider authCredentialsProvider() {
        return new DefaultAuthCredentialsProvider();
    }

    /**
     * 创建Amazon广告配置管理器Bean
     *
     * <p>默认使用{@link AmznAdvConfigManagerImpl},整合了所有数据提供者。
     *
     * @param amznTokenRequestHandler   Token请求处理器
     * @param profileDetailMetaProvider Profile详情数据提供者
     * @param authCredentialsProvider   OAuth认证凭证提供者
     * @return 配置管理器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public IAmznAdvConfigManager amznAdvConfigManager(
            IAmznTokenRequestProvider amznTokenRequestHandler,
            IProfileDetailMetaProvider profileDetailMetaProvider,
            IAuthCredentialsProvider authCredentialsProvider
    ) {
        return new AmznAdvConfigManagerImpl(
                amznTokenRequestHandler,
                profileDetailMetaProvider,
                authCredentialsProvider
        );
    }

    /**
     * 创建Amazon广告API客户端Bean
     *
     * <p>这是SDK的核心类,用于执行所有Amazon广告API调用。
     *
     * @param amznAdvConfigManager 配置管理器
     * @return Amazon广告API客户端实例
     */
    @Bean
    @ConditionalOnMissingBean
    public AmznAdClient amznAdClient(IAmznAdvConfigManager amznAdvConfigManager) {
        return new AmznAdClient(amznAdvConfigManager);
    }
}
