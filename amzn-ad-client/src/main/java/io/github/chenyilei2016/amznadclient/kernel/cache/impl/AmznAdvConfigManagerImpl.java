package io.github.chenyilei2016.amznadclient.kernel.cache.impl;

import io.github.chenyilei2016.amznadclient.kernel.amazon.IAmznTokenRequestHandler;
import io.github.chenyilei2016.amznadclient.kernel.cache.IAmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznAdAuthCredentialsResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.utils.RestTemplateUtil;
import io.github.chenyilei2016.amznadclient.kernel.utils.TrustSSLConstant;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author chenyilei
 * @since 2025/12/31 14:00
 */
public class AmznAdvConfigManagerImpl implements IAmznAdvConfigManager {

    private final IAmznTokenRequestHandler amznTokenRequestHandler;

    protected RestTemplate apiClient;

    public AmznAdvConfigManagerImpl(
            IAmznTokenRequestHandler amznTokenRequestHandler
    ) {
        this.amznTokenRequestHandler = amznTokenRequestHandler;
        this.apiClient = new RestTemplate(new OkHttp3ClientHttpRequestFactory(new OkHttpClient().newBuilder()
                .connectionPool(RestTemplateUtil.pool())
                .connectTimeout(15 * 1000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .readTimeout(60 * 1000L, TimeUnit.MILLISECONDS)
                .writeTimeout(60 * 1000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(TrustSSLConstant.getTrustSSLContext().getSocketFactory(), TrustSSLConstant.getTrustX509TrustManager())
                .build())
        );
        RestTemplateUtil.converterUtf8(this.apiClient);
    }

    @Override
    public RestTemplate getApiClient() {
        return this.apiClient;
    }

    @Override
    public AmznTokenResponse getAdvTokenByProfileId(String profileId) {
        return null;
    }

    @Override
    public ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId) {
        return null;
    }

    @Override
    public AmznAdAuthCredentialsResponse getAuthCredentialsByProfileId(String profileId) {

        return null;
    }
}
