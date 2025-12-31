package io.github.chenyilei2016.amznadclient.kernel.context;


import io.github.chenyilei2016.amznadclient.kernel.token.TokenProvider;

/**
 * @author chenyilei
 * @since 2024/05/22 15:47
 */
public class AmznAdClientHelper {

    protected static final ThreadLocal<TokenProvider> LOCAL_TOKEN_PROVIDER = new ThreadLocal<>();

    public static TokenProvider getTokenProviderThreadLocal() {
        return LOCAL_TOKEN_PROVIDER.get();
    }

    public static void clearTokenProviderThreadLocal() {
        LOCAL_TOKEN_PROVIDER.remove();
    }

    public static void setTokenProviderThreadLocal(TokenProvider tokenProvider) {
        LOCAL_TOKEN_PROVIDER.set(tokenProvider);
    }
}
