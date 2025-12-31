package io.github.chenyilei2016.amznadclient.kernel.context;


import io.github.chenyilei2016.amznadclient.kernel.baserequest.endpoint.EndpointProvider;
import io.github.chenyilei2016.amznadclient.kernel.baserequest.token.TokenProvider;

/**
 * 一些特殊场景 便于直接透传参数调用
 *
 * >>>> 只能让下一次调用生效
 *
 * @author chenyilei
 * @since 2024/05/22 15:47
 */
public class AmznAdClientHelper {

    protected static final ThreadLocal<TokenProvider> LOCAL_TOKEN_PROVIDER = new ThreadLocal<>();
    protected static final ThreadLocal<EndpointProvider> LOCAL_ENDPOINT_PROVIDER = new ThreadLocal<>();

    public static TokenProvider getTokenProviderThreadLocal() {
        return LOCAL_TOKEN_PROVIDER.get();
    }

    public static void clearTokenProviderThreadLocal() {
        LOCAL_TOKEN_PROVIDER.remove();
    }

    public static void setTokenProviderThreadLocal(TokenProvider tokenProvider) {
        LOCAL_TOKEN_PROVIDER.set(tokenProvider);
    }

    public static EndpointProvider getEndpointProviderThreadLocal() {
        return LOCAL_ENDPOINT_PROVIDER.get();
    }

    public static void clearEndpointProviderThreadLocal() {
        LOCAL_ENDPOINT_PROVIDER.remove();
    }

    public static void setEndpointProviderThreadLocal(EndpointProvider endpointProvider) {
        LOCAL_ENDPOINT_PROVIDER.set(endpointProvider);
    }
}
