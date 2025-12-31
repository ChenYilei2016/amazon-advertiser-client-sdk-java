package io.github.chenyilei2016.amznadclient.kernel.____tmpv_____;

/**
 * 认证策略接口，核心职责是提供一个有效的 AccessToken。
 */
public interface TokenProvider {

    /**
     * 获取一个有效的访问令牌。
     * 实现类需要负责处理令牌的缓存和过期刷新。
     */
    String getAccessToken();

    /**
     * 使当前缓存的令牌失效。
     * 当外部逻辑（如拦截器）捕获到401等认证失败时，可以调用此方法来强制刷新令牌。
     */
    void invalidateToken();
}
