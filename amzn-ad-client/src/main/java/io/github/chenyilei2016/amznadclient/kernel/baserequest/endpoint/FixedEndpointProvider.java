package io.github.chenyilei2016.amznadclient.kernel.baserequest.endpoint;

import lombok.Getter;

/**
 * 固定Endpoint提供者
 * 
 * <p>直接使用用户提供的固定endpoint URL,不需要从配置管理器查询。
 * 
 * <p>适用场景:
 * <ul>
 *   <li>已知endpoint URL,不需要通过profileId查询</li>
 *   <li>测试环境中使用特定的endpoint</li>
 *   <li>调用不同区域的Amazon广告API</li>
 * </ul>
 * 
 * <p>使用示例:
 * <pre>{@code
 * EndpointProvider provider = new FixedEndpointProvider("https://advertising-api.amazon.com");
 * 
 * AmznBaseRequest request = AmznBaseRequest.builder()
 *     .endpointProvider(provider)
 *     .url("/v2/profiles")
 *     .build();
 * }</pre>
 * 
 * @author chenyilei
 * @date 2025/12/31
 * @see EndpointProvider
 */
@Getter
public class FixedEndpointProvider implements EndpointProvider {
    
    /**
     * 固定的endpoint URL前缀
     */
    private final String endpointUrlPrefix;
    
    public FixedEndpointProvider(String endpointUrlPrefix) {
        if (endpointUrlPrefix == null || endpointUrlPrefix.trim().isEmpty()) {
            throw new IllegalArgumentException("endpointUrlPrefix must not be null or empty");
        }
        this.endpointUrlPrefix = endpointUrlPrefix;
    }
    
    /**
     * 返回构造时提供的固定endpoint URL
     * 
     * @return endpoint URL前缀
     */
    @Override
    public String getEndpointUrlPrefix() {
        return endpointUrlPrefix;
    }
}
