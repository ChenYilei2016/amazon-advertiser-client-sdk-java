package io.github.chenyilei2016.amznadclient.kernel.spi;

import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;

/**
 * Profile详情数据提供者接口
 * 
 * <p>定义了获取Amazon广告账户Profile详细信息的统一接口。
 * 外部系统可以实现此接口来提供自定义的Profile数据获取逻辑。
 * 
 * <p>使用场景:
 * <ul>
 *   <li>从数据库查询Profile信息</li>
 *   <li>从缓存中获取Profile信息</li>
 *   <li>从第三方服务获取Profile信息</li>
 * </ul>
 * 
 * @author chenyilei
 * @since 2025/12/31
 * @see ProfileDetailMetaResponse
 */
public interface IProfileDetailMetaProvider {
    
    /**
     * 根据profileId获取Profile详细信息
     * 
     * @param profileId Amazon广告账户的profileId
     * @return Profile详细信息,包含endpointUrl、refreshToken、countryCode等
     * @throws io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException 
     *         如果profileId不存在或获取失败
     */
    ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId);
}
