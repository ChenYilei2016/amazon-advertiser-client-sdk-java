package io.github.chenyilei2016.amznadclient.api;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.RefreshPolicy;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import io.github.chenyilei2016.amznadclient.AmznAdClient;
import io.github.chenyilei2016.amznadclient.SystemPropGet;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.manager.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.manager.impl.AmznAdvConfigManagerImpl;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAmznTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.IProfileDetailMetaProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.cache.CachedTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultAmznTokenRequestProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultAuthCredentialsProvider;
import io.github.chenyilei2016.amznadclient.kernel.spi.impl.DefaultProfileDetailMetaProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

/**
 * 本地测试工具类 - 非Spring环境
 * 
 * <p>提供了在非Spring环境下创建AmznAdClient的工具方法,
 * 并配置了JVM缓存以防止重复调用Amazon Token API。
 * 
 * @author chenyilei
 * @since 2025/12/25
 */
public class AmznClientLocalTest {

    public static AmznAdClient amznAdClient;

    public static IAmznAdvConfigManager amznAdvConfigManager;

    static {
        IAmznTokenRequestProvider cachedTokenRequestProvider = mockIAmznTokenRequestProvider();

        // 4. 创建Profile详情提供者(Mock实现)
        IProfileDetailMetaProvider defaultProfileDetailMetaProvider = mockDefaultProfileDetailMetaProvider();

        // 5. 创建认证凭证提供者(Mock实现)
        IAuthCredentialsProvider defaultAuthCredentialsProvider = mockDefaultAuthCredentialsProvider();

        // 6. 创建配置管理器(使用带缓存的Token请求处理器)
        amznAdvConfigManager = new AmznAdvConfigManagerImpl(
                cachedTokenRequestProvider,  // 使用缓存装饰器
                defaultProfileDetailMetaProvider,
                defaultAuthCredentialsProvider
        );

        // 7. 创建AmznAdClient
        AmznClientLocalTest.amznAdClient = new AmznAdClient(amznAdvConfigManager);
        
        System.out.println("AmznClientLocalTest初始化完成,已启用Token缓存");
    }

    @NotNull
    private static DefaultAuthCredentialsProvider mockDefaultAuthCredentialsProvider() {
        return new DefaultAuthCredentialsProvider() {
            @Override
            public AmznAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId) {
                AmznAuthCredentialsResponse amznAuthCredentialsResponse = new AmznAuthCredentialsResponse();
                amznAuthCredentialsResponse.setAdvClientId(SystemPropGet.clientId());
                amznAuthCredentialsResponse.setAdvClientSecret(SystemPropGet.clientSecret());
                return amznAuthCredentialsResponse;
            }
        };
    }

    @NotNull
    private static DefaultProfileDetailMetaProvider mockDefaultProfileDetailMetaProvider() {
        return new DefaultProfileDetailMetaProvider() {
            @Override
            public ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId) {
                ProfileDetailMetaResponse response = new ProfileDetailMetaResponse();
                response.setProfileId(profileId);
                response.setEndpointUrl("https://advertising-api.amazon.com");
                response.setCountryCode("US");
                response.setProfileType("seller");
                response.setAdvRefreshToken(SystemPropGet.refreshToken());
                return response;
            }
        };
    }

    @NotNull
    private static IAmznTokenRequestProvider mockIAmznTokenRequestProvider() {
        // 1. 创建默认的Token请求处理器
        DefaultAmznTokenRequestProvider defaultAmznTokenRequestHandler = new DefaultAmznTokenRequestProvider();
        try {
            defaultAmznTokenRequestHandler.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Cache<AccessTokenMetaRequest, AmznTokenResponse> tokenCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(100)
                .expireAfterWrite(1000, TimeUnit.SECONDS) //此值在multi下是会被覆盖的
                .loader(key -> defaultAmznTokenRequestHandler.doRefreshToken((AccessTokenMetaRequest) key))
                .buildCache();

        // 3. 使用缓存装饰器包装Token请求处理器
        IAmznTokenRequestProvider cachedTokenRequestProvider = new CachedTokenRequestProvider(defaultAmznTokenRequestHandler, tokenCache);
        return cachedTokenRequestProvider;
    }

    /**
     * 创建指定类型的Amazon广告API客户端
     * 
     * @param tClass 客户端类型
     * @param <T> 泛型类型
     * @return 客户端实例
     */
    protected static <T> T newAmznClient(Class<T> tClass) {
        Constructor constructor = ReflectUtils.getConstructor(tClass, new Class[]{AmznAdClient.class});
        Object o = ReflectUtils.newInstance(constructor, new Object[]{amznAdClient});
        return (T) o;
    }
}
