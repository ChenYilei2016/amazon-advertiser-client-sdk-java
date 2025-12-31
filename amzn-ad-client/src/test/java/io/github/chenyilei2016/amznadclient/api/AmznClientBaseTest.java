package io.github.chenyilei2016.amznadclient.api;

import io.github.chenyilei2016.amznadclient.AmznAdClient;
import io.github.chenyilei2016.amznadclient.SystemPropGet;
import io.github.chenyilei2016.amznadclient.kernel.manager.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.manager.impl.AmznAdvConfigManagerImpl;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultAmznTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultProfileDetailMetaProvider;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Constructor;

/**
 * @author chenyilei
 * @since 2025/12/25 14:51
 */
public class AmznClientBaseTest {

    public static AmznAdClient amznAdClient;

    public static IAmznAdvConfigManager amznAdvConfigManager;

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
        DefaultAmznTokenRequestProvider defaultAmznTokenRequestHandler = new DefaultAmznTokenRequestProvider();
        try {
            defaultAmznTokenRequestHandler.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DefaultProfileDetailMetaProvider defaultProfileDetailMetaProvider = new DefaultProfileDetailMetaProvider() {
            @Override
            public ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId) {
                return super.getProfileDetailMetaByProfileId(profileId);
            }
        };

        DefaultAuthCredentialsProvider defaultAuthCredentialsProvider = new DefaultAuthCredentialsProvider() {
            @Override
            public AmznAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId) {
                AmznAuthCredentialsResponse amznAuthCredentialsResponse = new AmznAuthCredentialsResponse();
                amznAuthCredentialsResponse.setAdvClientId(SystemPropGet.clientId());
                amznAuthCredentialsResponse.setAdvClientSecret(SystemPropGet.clientSecret());
                return amznAuthCredentialsResponse;
            }
        };

        amznAdvConfigManager = new AmznAdvConfigManagerImpl(
                defaultAmznTokenRequestHandler,
                defaultProfileDetailMetaProvider,
                defaultAuthCredentialsProvider
        );

        /**
         * mock
         */
//        Cache<AccessTokenMetaRequest, AmznTokenResponse> tokenCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
//                .limit(100)
//                .expireAfterWrite(1000, TimeUnit.SECONDS) //此值在multi下是会被覆盖的
//                .loader(key -> amznAdvConfigManager.doRefreshToken((AccessTokenMetaRequest) key))
//                .buildCache();
//
//        Cache<String, ProfileDetailMetaResponse> profileCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
//                .limit(100)
//                .expireAfterWrite(1000, TimeUnit.SECONDS) //此值在multi下是会被覆盖的
//                .loader(profileId -> {
//                    final String profileIdStr = (String) profileId;
//                    ProfileDetailMetaResponse config = new ProfileDetailMetaResponse();
//                    //默认的配置 , 要加入properties !!!
//                    config.setProfileId(profileIdStr);
//                    config.setCountryCode("US");
//                    config.setEndpointUrl(AmznRegionEnum.NORTH_AMERICA.getEndpointUrlPrefix());
//                    config.setProfileType(AmznProfileTypeEnum.SC.getCode()); //默认sc
//                    config.setAdvRefreshToken(SystemPropGet.refreshToken());
//                    return config;
//                })
//                .buildCache();

//        AmznAdvTokenCacheManager amznAdvTokenCacheManager = new AmznAdvTokenCacheManager(tokenCache, profileCache);
//        amznAdvConfigManager.setAmznAdvTokenCacheManager(amznAdvTokenCacheManager);
//        amznAdvConfigManager.afterPropertiesSet();
        AmznClientBaseTest.amznAdClient = new AmznAdClient(amznAdvConfigManager);
    }

    protected static <T> T newAmznClient(Class<T> tClass) {
        Constructor constructor = ReflectUtils.getConstructor(tClass, new Class[]{AmznAdClient.class});
        Object o = ReflectUtils.newInstance(constructor, new Object[]{amznAdClient});
        return (T) o;
    }
}
