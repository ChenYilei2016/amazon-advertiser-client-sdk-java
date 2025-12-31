package io.github.chenyilei2016.amznadclient.api;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import io.github.chenyilei2016.amznadclient.AmznAdClient;
import io.github.chenyilei2016.amznadclient.SystemPropGet;
import io.github.chenyilei2016.amznadclient.kernel.cache.AmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.cache.AmznAdvTokenCacheManager;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenRequestMeta;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMeta;
import io.github.chenyilei2016.amznadclient.model.account.AmznProfileTypeEnum;
import io.github.chenyilei2016.amznadclient.model.common.AmznRegionEnum;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenyilei
 * @since 2025/12/25 14:51
 */
public class AmznClientBaseTest {

    public static AmznAdClient amznAdClient;

    public static AmznAdvConfigManager amznAdvConfigManager;

    static {
//        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
//        annotationConfigApplicationContext.register(PropertyPlaceholderAutoConfiguration.class, ConfigurationPropertiesAutoConfiguration.class);
//        Properties properties = new Properties();
//        try {
//            new PathMatchingResourcePatternResolver()
//            DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader(ClassLoader.getSystemClassLoader());
//            properties.load(defaultResourceLoader.getResource("classpath:application.properties").getInputStream());
//            properties.load(defaultResourceLoader.getResource("classpath:application-unit.properties").getInputStream());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//            ConfigurableEnvironment environment = annotationConfigApplicationContext.getEnvironment();
//            environment.getPropertySources().addFirst(new PropertiesPropertySource("test_", properties));
//            annotationConfigApplicationContext.refresh();
//        ConfigurationPropertiesBindingPostProcessor bindingPostProcessor = annotationConfigApplicationContext.getBean(ConfigurationPropertiesBindingPostProcessor.class);
//        bindingPostProcessor.postProcessBeforeInitialization(amazonConfigProperties, "config_");
    }

    static {
        amznAdvConfigManager = new AmznAdvConfigManager();

        /**
         * mock
         */
        Cache<AccessTokenRequestMeta, AmznTokenResponse> tokenCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(1000, TimeUnit.SECONDS) //此值在multi下是会被覆盖的
                .loader(key -> amznAdvConfigManager.doRefreshToken((AccessTokenRequestMeta) key))
                .buildCache();

        Cache<String, ProfileDetailMeta> profileCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(1000, TimeUnit.SECONDS) //此值在multi下是会被覆盖的
                .loader(profileId -> {
                    final String profileIdStr = (String) profileId;
                    ProfileDetailMeta config = new ProfileDetailMeta();
                    //默认的配置 , 要加入properties !!!
                    config.setProfileId(profileIdStr);
                    config.setCountryCode("US");
                    config.setEndpointUrl(AmznRegionEnum.NORTH_AMERICA.getEndpointUrlPrefix());
                    config.setProfileType(AmznProfileTypeEnum.SC.getCode()); //默认sc
                    config.setAdvRefreshToken(SystemPropGet.refreshToken());
                    return config;
                })
                .buildCache();

        AmznAdvTokenCacheManager amznAdvTokenCacheManager = new AmznAdvTokenCacheManager(tokenCache, profileCache);
        amznAdvConfigManager.setAmznAdvTokenCacheManager(amznAdvTokenCacheManager);
        amznAdvConfigManager.afterPropertiesSet();
        AmznClientBaseTest.amznAdClient = new AmznAdClient(amznAdvConfigManager);
    }

    protected static <T> T newAmznClient(Class<T> tClass) {
        Constructor constructor = ReflectUtils.getConstructor(tClass, new Class[]{AmznAdClient.class});
        Object o = ReflectUtils.newInstance(constructor, new Object[]{amznAdClient});
        return (T) o;
    }
}
