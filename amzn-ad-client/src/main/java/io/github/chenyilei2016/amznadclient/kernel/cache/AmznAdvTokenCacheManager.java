package io.github.chenyilei2016.amznadclient.kernel.cache;

import com.alicp.jetcache.Cache;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenRequestMeta;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMeta;

/**
 * @author chenyilei
 * @date 2023/09/11 13:56
 * token 多级缓存
 * @see AmznMultiLevelTokenCacheConfig
 * @see AmznTokenResponse
 */
public class AmznAdvTokenCacheManager {

    private final Cache<String, ProfileDetailMeta> profileDetailCache;

    private final Cache<AccessTokenRequestMeta, AmznTokenResponse> multiLevelTokenCache;


    public AmznAdvTokenCacheManager(Cache<AccessTokenRequestMeta, AmznTokenResponse> multiLevelTokenCache,
                                    Cache<String, ProfileDetailMeta> profileDetailCache) {
        this.multiLevelTokenCache = multiLevelTokenCache;
        this.profileDetailCache = profileDetailCache;
    }

    public ProfileDetailMeta getProfileDetailMeta(String profileId) {
        return profileDetailCache.get(profileId);
    }

    /**
     * todo: cyl
     *
     * @return
     */
    public AmznTokenResponse getAdvTokenResponse(String profileId) {
        ProfileDetailMeta profileDetailMeta = getProfileDetailMeta(profileId);
        if (profileDetailMeta == null) {
            return null;
        }
        AccessTokenRequestMeta accessTokenRequestMeta = new AccessTokenRequestMeta();
//        accessTokenRequestMeta.setClientId(amazonAccountConfigBO.getAdvClientId());
//        accessTokenRequestMeta.setClientSecret(amazonAccountConfigBO.getAdvClientSecret());
//        accessTokenRequestMeta.setRefreshToken(profileDetailMeta.getAdvRefreshToken());
        return getAdvToken(accessTokenRequestMeta);
    }

    protected AmznTokenResponse getAdvToken(AccessTokenRequestMeta key) {
        return multiLevelTokenCache.get(key); // . 目前写3300秒, token刚获取到的应该是3600秒过期
    }

    public void shutdown() {
        this.profileDetailCache.close();
        this.multiLevelTokenCache.close();
    }
}
