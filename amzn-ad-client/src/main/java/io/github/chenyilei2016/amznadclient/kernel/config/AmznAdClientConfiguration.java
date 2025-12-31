package io.github.chenyilei2016.amznadclient.kernel.config;

import io.github.chenyilei2016.amznadclient.kernel.amazon.IAmznTokenRequestHandler;
import io.github.chenyilei2016.amznadclient.kernel.amazon.DefaultAmznTokenRequestHandler;
import io.github.chenyilei2016.amznadclient.kernel.cache.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.cache.impl.AmznAdvConfigManagerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenyilei
 * @since 2025/12/31 13:56
 */
@Configuration
public class AmznAdClientConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public IAmznTokenRequestHandler amznTokenRequestHandler() {
        return new DefaultAmznTokenRequestHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public IAmznAdvConfigManager amznAdvConfigManager(IAmznTokenRequestHandler amznTokenRequestHandler){
        return new AmznAdvConfigManagerImpl(
                amznTokenRequestHandler
        );
    }
}
