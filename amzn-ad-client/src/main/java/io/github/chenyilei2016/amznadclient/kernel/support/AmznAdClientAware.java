package io.github.chenyilei2016.amznadclient.kernel.support;

import io.github.chenyilei2016.amznadclient.AmznAdClient;

/**
 * AmznAdClient感知接口
 * 
 * <p>实现此接口的类可以自动获取AmznAdClient实例,从而访问IAmznAdvConfigManager等资源。
 * 这样可以简化API,用户不需要手动传入IAmznAdvConfigManager。
 * 
 * <p>使用场景:
 * <ul>
 *   <li>TokenProvider需要访问IAmznAdvConfigManager来获取token</li>
 *   <li>EndpointProvider需要访问IAmznAdvConfigManager来获取endpoint</li>
 * </ul>
 * 
 * @author chenyilei
 * @since 2025/12/31
 */
public interface AmznAdClientAware {
    
    /**
     * 设置AmznAdClient实例
     * 
     * <p>此方法会在AmznBaseRequest构建时自动调用,将AmznAdClient实例注入到实现类中。
     * 
     * @param amznAdClient AmznAdClient实例
     */
    void setAmznAdClient(AmznAdClient amznAdClient);
}
