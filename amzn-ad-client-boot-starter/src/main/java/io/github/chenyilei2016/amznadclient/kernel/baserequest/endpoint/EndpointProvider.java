package io.github.chenyilei2016.amznadclient.kernel.baserequest.endpoint;

/**
 * Endpoint提供者接口 - 策略模式
 * 
 * <p>定义了获取Amazon广告API endpoint URL的统一接口。不同的实现类可以采用不同的策略来获取endpoint:
 * <ul>
 *   <li>基于profileId从配置管理器获取</li>
 *   <li>直接使用固定的endpoint URL</li>
 *   <li>用户自定义的endpoint获取逻辑</li>
 * </ul>
 * 
 * @author chenyilei
 * @date 2025/12/31
 * @see ProfileBasedEndpointProvider
 * @see FixedEndpointProvider
 */
public interface EndpointProvider {
    
    /**
     * 获取Amazon广告API的endpoint URL前缀
     * 
     * <p>返回的URL应该是完整的endpoint前缀,例如: "https://advertising-api.amazon.com"
     * 
     * @return endpoint URL前缀字符串
     * @throws io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException 
     *         如果获取endpoint失败
     */
    String getEndpointUrlPrefix();
}
