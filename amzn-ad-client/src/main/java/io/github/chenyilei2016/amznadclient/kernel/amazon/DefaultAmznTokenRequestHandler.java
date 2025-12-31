package io.github.chenyilei2016.amznadclient.kernel.amazon;

import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznConstants;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.utils.RestTemplateUtil;
import io.github.chenyilei2016.amznadclient.kernel.wrapper.AmznIOTimeOutRetryWrapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2025/12/31 14:02
 */
@Slf4j
public class DefaultAmznTokenRequestHandler implements IAmznTokenRequestHandler, InitializingBean {

    @Getter
    protected RestTemplate tokenClient;

    @Override
    @SneakyThrows
    public AmznTokenResponse doRefreshToken(AccessTokenMetaRequest accessTokenMetaRequest) {
        return AmznIOTimeOutRetryWrapper.wrap(() -> {
            String apiTokenUrl = "https://api.amazon.com/auth/o2/token";
            HttpHeaders httpHeaders = RestTemplateUtil.getHttpHeadersOfFormUrlencoded();
            String grantType = "grant_type=" + accessTokenMetaRequest.getGrantType();
            String refreshToken = "refresh_token=" + accessTokenMetaRequest.getRefreshToken();
            String clientId = "client_id=" + accessTokenMetaRequest.getClientId();
            String clientSecret = "client_secret=" + accessTokenMetaRequest.getClientSecret();
            String param = String.join("&", grantType, refreshToken, clientId, clientSecret);
            HttpEntity<?> httpEntity = new HttpEntity<>(param, httpHeaders);
            ResponseEntity<AmznTokenResponse> amznTokenResponseResponseEntity = this.tokenClient.postForEntity(apiTokenUrl, httpEntity, AmznTokenResponse.class);
            AmznTokenResponse response = amznTokenResponseResponseEntity.getBody();
            boolean flag = Objects.nonNull(response) && StringUtils.isNotEmpty(response.getAccess_token());
            Assert.isTrue(flag, "获取token失败，请重试");
            response.setClientId(accessTokenMetaRequest.getClientId());
            log.warn("获取token:{}, requestId:{}", response, httpEntity.getHeaders().get(AmznConstants.HEADER_requestId));
            return response;
        }, 3).call();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(RestTemplateUtil.pool())
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true);
        this.tokenClient = new RestTemplate(new OkHttp3ClientHttpRequestFactory(builder.build()));

        RestTemplateUtil.converterUtf8(this.tokenClient);
    }
}
