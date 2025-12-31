package io.github.chenyilei2016.amznadclient.kernel.spi;

import io.github.chenyilei2016.amznadclient.kernel.core.AmznAdAuthCredentialsResponse;

/**
 * Amazon广告认证凭证提供者接口
 * 
 * <p>定义了获取Amazon广告OAuth认证凭证(clientId、clientSecret)的统一接口。
 * 外部系统可以实现此接口来提供自定义的认证凭证获取逻辑。
 * 
 * <p>使用场景:
 * <ul>
 *   <li>从配置文件读取认证凭证</li>
 *   <li>从数据库查询认证凭证</li>
 *   <li>从密钥管理服务获取认证凭证</li>
 * </ul>
 * 
 * @author chenyilei
 * @since 2025/12/31
 * @see AmznAdAuthCredentialsResponse
 */
public interface IAuthCredentialsProvider {
    
    /**
     * 根据profileId获取对应的Amazon广告OAuth认证凭证
     * 
     * @param profileId Amazon广告账户的profileId
     * @return OAuth认证凭证,包含clientId和clientSecret
     * @throws io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException 
     *         如果profileId不存在或获取失败
     */
    AmznAdAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId);
}
