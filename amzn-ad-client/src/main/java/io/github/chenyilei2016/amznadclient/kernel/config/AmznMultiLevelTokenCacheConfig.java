//package io.github.chenyilei2016.myclient.kernel.config;
//
//import com.alicp.jetcache.Cache;
//import com.alicp.jetcache.MultiLevelCacheBuilder;
//import com.alicp.jetcache.RefreshPolicy;
//import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
//import com.alicp.jetcache.redis.springdata.RedisSpringDataCacheBuilder;
//import com.alicp.jetcache.support.Fastjson2KeyConvertor;
//import io.github.chenyilei2016.myclient.kernel.cache.AmznAdvConfigManager;
//import io.github.chenyilei2016.myclient.kernel.cache.AmznAdvTokenCacheManager;
//import io.github.chenyilei2016.myclient.kernel.core.AccessTokenRequestMeta;
//import io.github.chenyilei2016.myclient.kernel.core.AmznTokenResponse;
//import io.github.chenyilei2016.myclient.kernel.core.ProfileDetailMeta;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//import javax.annotation.Resource;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author chenyilei
// * @date 2023/09/11 11:37
// */
//@Configuration
//public class AmznMultiLevelTokenCacheConfig {
//    /**
//     * todo: cyl
//     */
//
//    @Resource
//    @Lazy
//    private SiteShopDomainService siteShopDomainService;
//
//
//    /**
//     * 优先jvm缓存
//     * 然后 redis缓存
//     */
//    @Bean
//    public AmznAdvConfigManager amznConfigManager(@Autowired(required = false) RedisConnectionFactory redisConnectionFactory,
//                                                  AmazonConfigProperties amznConfigProperties) {
//        AmznAdvConfigManager amznAdvConfigManager = new AmznAdvConfigManager();
//
//        MultiLevelCacheBuilder.MultiLevelCacheBuilderImpl multiLevelCacheBuilder = MultiLevelCacheBuilder.createMultiLevelCacheBuilder();
//        Cache<AccessTokenRequestMeta, AmznTokenResponse> jvmAdvTokenCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
//                .limit(10000)
//                .expireAfterWrite(1, TimeUnit.MINUTES) //此值在multi下是会被覆盖的
//                .buildCache();
//        multiLevelCacheBuilder.addCache(jvmAdvTokenCache);
//
//        if (redisConnectionFactory != null) {
//            Cache<AccessTokenRequestMeta, AmznTokenResponse> redisAdvTokenCache = RedisSpringDataCacheBuilder.createBuilder()
//                    .connectionFactory(redisConnectionFactory)
//                    .expireAfterWrite(55, TimeUnit.MINUTES)
//                    .keyPrefix(RedisKeyCenter.amznProfileAdvAccessTokenPrefix)
//                    .keyConvertor(Fastjson2KeyConvertor.INSTANCE) //ha 引入了fastjson2 可以共存
//                    .buildCache();
//            multiLevelCacheBuilder.addCache(redisAdvTokenCache);
//        }
//
//        Cache<AccessTokenRequestMeta, AmznTokenResponse> multiLevelCache = multiLevelCacheBuilder
//                .loader(key -> amznAdvConfigManager.doRefreshToken((AccessTokenRequestMeta) key))
//                .refreshPolicy(RefreshPolicy.newPolicy(50, TimeUnit.MINUTES).stopRefreshAfterLastAccess(8, TimeUnit.HOURS)) //50分钟自动刷新
//                .expireAfterWrite(55, TimeUnit.MINUTES)
//                .useExpireOfSubCache(true)
//                .cachePenetrateProtect(true)
//                .buildCache();
//
//        Cache<String, ProfileDetailMeta> jvmProfileDetailCache = CaffeineCacheBuilder.createCaffeineCacheBuilder()
//                .limit(10000)
//                .expireAfterWrite(60, TimeUnit.SECONDS)
//                .loader(profileId -> {
//                    SiteShopEntity siteShop = siteShopDomainService.getBasicSitShopByProfileIdCacheable((String) profileId, true);
//                    ProfileDetailMeta profileDetailMeta = new ProfileDetailMeta();
//                    profileDetailMeta.setTenantId(siteShop.getTenantId());
//                    profileDetailMeta.setProfileId(siteShop.getProfileId());
//                    profileDetailMeta.setAdvRefreshToken(siteShop.getAdvRefreshToken());
//                    profileDetailMeta.setCountryCode(siteShop.getCountryCode());
//                    profileDetailMeta.setProfileType(siteShop.getType());
//                    profileDetailMeta.setAmazonAdvAccountType(siteShop.getAdvApiAmazonAccountType());
//                    String countryCode = siteShop.getCountryCode();
//                    AmznRegionEnum viaMarketplace = AmznRegionEnum.getViaMarketplace(countryCode);
//                    if (null == viaMarketplace) {
//                        throw CommonException.createBizException("profileId:{}, 未知的国家码:{}", profileId, countryCode);
//                    }
//                    profileDetailMeta.setEndpointUrl(viaMarketplace.getEndpointUrlPrefix());
//                    return profileDetailMeta;
//                })
//                .cachePenetrateProtect(true)
//                .buildCache();
//        AmznAdvTokenCacheManager amznAdvTokenCacheManager = new AmznAdvTokenCacheManager(multiLevelCache, jvmProfileDetailCache, amznConfigProperties);
//        amznAdvConfigManager.setAmznAdvTokenCacheManager(amznAdvTokenCacheManager);
//        return amznAdvConfigManager;
//    }
//}
