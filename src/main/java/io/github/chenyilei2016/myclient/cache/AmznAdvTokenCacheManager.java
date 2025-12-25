package io.github.chenyilei2016.myclient.cache;

import com.alicp.jetcache.Cache;
import com.zbycorp.fenghuo.domain.common.amazonconnect.baseserviceV2.config.AmznMultiLevelTokenCacheConfig;
import com.zbycorp.fenghuo.domain.common.amazonconnect.kernel.dto.AccessTokenRequestMeta;
import com.zbycorp.fenghuo.domain.common.amazonconnect.kernel.dto.AmznTokenResponse;
import com.zbycorp.fenghuo.domain.common.amazonconnect.kernel.dto.ProfileDetailMeta;
import com.zbycorp.fenghuo.domain.common.rpc.nacos.AmazonConfigProperties;
import com.zbycorp.fenghuo.domain.common.rpc.nacos.bo.AmazonAccountConfigBO;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

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

    @Getter
    private final Map<String, AmazonAccountConfigBO> amazonAccountMap;

    public AmznAdvTokenCacheManager(Cache<AccessTokenRequestMeta, AmznTokenResponse> multiLevelTokenCache,
                                    Cache<String, ProfileDetailMeta> profileDetailCache,
                                    AmazonConfigProperties amznConfigProperties) {
        this.multiLevelTokenCache = multiLevelTokenCache;
        this.profileDetailCache = profileDetailCache;
        this.amazonAccountMap = Collections.unmodifiableMap(amznConfigProperties.getAccountMap());
    }

    public ProfileDetailMeta getProfileDetailMeta(String profileId) {
        return profileDetailCache.get(profileId);
    }

    public AmznTokenResponse getAdvTokenResponse(String profileId) {
        ProfileDetailMeta profileDetailMeta = getProfileDetailMeta(profileId);
        if (profileDetailMeta == null) {
            return null;
        }
        String awsAccountType = profileDetailMeta.getAmazonAdvAccountType();
        AmazonAccountConfigBO amazonAccountConfigBO = amazonAccountMap.get(awsAccountType);
        AccessTokenRequestMeta accessTokenRequestMeta = new AccessTokenRequestMeta();
        accessTokenRequestMeta.setClientId(amazonAccountConfigBO.getAdvClientId());
        accessTokenRequestMeta.setClientSecret(amazonAccountConfigBO.getAdvClientSecret());
        accessTokenRequestMeta.setRefreshToken(profileDetailMeta.getAdvRefreshToken());
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
