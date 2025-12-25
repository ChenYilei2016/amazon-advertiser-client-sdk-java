package io.github.chenyilei2016.myclient.kernel.cache;

import io.github.chenyilei2016.myclient.kernel.core.AccessTokenRequestMeta;
import io.github.chenyilei2016.myclient.kernel.core.AmznConstants;
import io.github.chenyilei2016.myclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.myclient.kernel.core.ProfileDetailMeta;
import io.github.chenyilei2016.myclient.kernel.utils.RestTemplateUtil;
import io.github.chenyilei2016.myclient.kernel.utils.TrustSSLConstant;
import io.github.chenyilei2016.myclient.kernel.wrapper.AmznIOTimeOutRetryWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author chenyilei
 * @date 2023/04/03 17:38
 * https://zbyit.yuque.com/staff-pmf7d0/fqqknv/feufv0oeis91fb0c
 */
@Slf4j
public class AmznAdvConfigManager implements InitializingBean, DisposableBean {

    @Setter
    @Getter
    private AmznAdvTokenCacheManager amznAdvTokenCacheManager;

    @Getter
    protected RestTemplate apiClient;

    @Getter
    protected RestTemplate tokenClient;


    public AmznAdvConfigManager() {
        initClient();
    }

    private void initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(RestTemplateUtil.pool())
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true);
        this.tokenClient = new RestTemplate(new OkHttp3ClientHttpRequestFactory(builder.build()));

        this.apiClient = new RestTemplate(new OkHttp3ClientHttpRequestFactory(new OkHttpClient().newBuilder()
                .connectionPool(RestTemplateUtil.pool())
                .connectTimeout(15 * 1000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .readTimeout(60 * 1000L, TimeUnit.MILLISECONDS)
                .writeTimeout(60 * 1000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(TrustSSLConstant.getTrustSSLContext().getSocketFactory(), TrustSSLConstant.getTrustX509TrustManager())
                .build())
        );
        RestTemplateUtil.converterUtf8(this.tokenClient);
        RestTemplateUtil.converterUtf8(this.apiClient);
    }

    /**
     * 通过profileId查询亚马逊信息, 再加载
     */
    public AmznTokenResponse getAdvToken(String profileId) {
        return amznAdvTokenCacheManager.getAdvTokenResponse(profileId);
    }


    public ProfileDetailMeta getProfileDetailMeta(String profileId) {
        return this.amznAdvTokenCacheManager.getProfileDetailMeta(profileId);
    }

    /**
     * 通过亚马逊信息加载
     */
    public AmznTokenResponse getAdvToken(AccessTokenRequestMeta accessTokenRequestMeta) {
        return amznAdvTokenCacheManager.getAdvToken(accessTokenRequestMeta);
    }

    public AmznTokenResponse doRefreshToken(AccessTokenRequestMeta accessTokenRequestMeta) {
        return this.doRefreshToken(tokenClient, accessTokenRequestMeta);
    }

    @SneakyThrows
    protected AmznTokenResponse doRefreshToken(RestTemplate client, AccessTokenRequestMeta accessTokenRequestMeta) {
        return AmznIOTimeOutRetryWrapper.wrap(() -> {
                    String apiTokenUrl = "https://api.amazon.com/auth/o2/token";
                    HttpHeaders httpHeaders = RestTemplateUtil.getHttpHeadersOfFormUrlencoded();
                    String grantType = "grant_type=" + accessTokenRequestMeta.getGrantType();
                    String refreshToken = "refresh_token=" + accessTokenRequestMeta.getRefreshToken();
                    String clientId = "client_id=" + accessTokenRequestMeta.getClientId();
                    String clientSecret = "client_secret=" + accessTokenRequestMeta.getClientSecret();
                    String param = String.join("&", grantType, refreshToken, clientId, clientSecret);
                    HttpEntity<?> httpEntity = new HttpEntity<>(param, httpHeaders);
                    ResponseEntity<AmznTokenResponse> amznTokenResponseResponseEntity = client.postForEntity(apiTokenUrl, httpEntity, AmznTokenResponse.class);
                    AmznTokenResponse response = amznTokenResponseResponseEntity.getBody();
                    boolean flag = Objects.nonNull(response) && StringUtils.isNotEmpty(response.getAccess_token());
                    Assert.isTrue(flag, "获取token失败，请重试");
                    response.setClientId(accessTokenRequestMeta.getClientId());
                    log.warn("获取token:{},requestId:{}", response, httpEntity.getHeaders().get(AmznConstants.HEADER_requestId));
                    return response;
                }
                , 3
        ).call();
    }


    @Override
    public void afterPropertiesSet() {
        if (this.amznAdvTokenCacheManager == null) {
            throw new IllegalArgumentException("amznAdvTokenCacheManager 不能为空");
        }
    }

    @Override
    public void destroy() throws Exception {
        amznAdvTokenCacheManager.shutdown();
    }
}
