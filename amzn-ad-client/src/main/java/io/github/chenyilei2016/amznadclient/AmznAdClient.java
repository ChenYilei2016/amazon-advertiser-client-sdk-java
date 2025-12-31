package io.github.chenyilei2016.amznadclient;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.chenyilei2016.amznadclient.kernel.advice.AmznClientCrudTypeEnum;
import io.github.chenyilei2016.amznadclient.kernel.advice.AmznClientRequestBeforeInvoke;
import io.github.chenyilei2016.amznadclient.kernel.advice.AmznClientResponseBeforeReturn;
import io.github.chenyilei2016.amznadclient.kernel.cache.AmznAdvConfigManager;
import io.github.chenyilei2016.amznadclient.kernel.context.AmznAdClientHelper;
import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenRequestMeta;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznConstants;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMeta;
import io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException;
import io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiRetryMaxException;
import io.github.chenyilei2016.amznadclient.kernel.gson.GsonFromStringDeserializer;
import io.github.chenyilei2016.amznadclient.kernel.gson.GsonUtil;
import io.github.chenyilei2016.amznadclient.kernel.support.MediaTypePair;
import io.github.chenyilei2016.amznadclient.kernel.support.SpecialClientDetail;
import io.github.chenyilei2016.amznadclient.kernel.utils.RestTemplateUtil;
import io.github.chenyilei2016.amznadclient.kernel.wrapper.Amzn401UnauthorizedRetryWrapper;
import io.github.chenyilei2016.amznadclient.kernel.wrapper.AmznIOTimeOutRetryWrapper;
import io.github.chenyilei2016.amznadclient.kernel.wrapper.AmznRateLimitRetryWrapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;


/**
 * 使用gson 作为序列化和反序列化
 *
 * @author chenyilei
 * @date 2023/04/03 17:19
 */
@Slf4j
@Component
public class AmznAdClient {

    private final AmznAdvConfigManager amznAdvConfigManager;

    @Getter
    private final Gson requestGson;

    @Getter
    private final Gson resultGson;

    /**
     * 是否是假调用, 防止单测阶段调用亚马逊
     */
    private boolean isMockInvoke = false;

    /**
     * @param amznAdvConfigManager 配置管理
     */
    public AmznAdClient(AmznAdvConfigManager amznAdvConfigManager) {
        this.amznAdvConfigManager = amznAdvConfigManager;
        requestGson = new GsonBuilder()
                //只有标注了@Expose的注解才会序列化和反序列化
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        resultGson = new GsonBuilder()
                .registerTypeAdapter(String.class, new GsonFromStringDeserializer())
                .create();

        String testUnitTag = System.getProperty("fenghuo.test.unit");
        if ("true".equalsIgnoreCase(testUnitTag)) {
            this.isMockInvoke = true;
        }
    }

    public RestTemplate apiClientTemplate() {
        return this.amznAdvConfigManager.getApiClient();
    }

    private String getRequestUrl(AmznBaseRequest amznBaseRequest) {
        String requestUrl = null;

        //url主体连接
        if (StringUtils.isNotBlank(amznBaseRequest.getEndpointUrlPrefix())) {
            requestUrl = String.format("%s%s", amznBaseRequest.getEndpointUrlPrefix(), amznBaseRequest.getUrl());
        } else {
            //没有传endpoint去缓存里查询
            ProfileDetailMeta profileDetailMeta = this.amznAdvConfigManager.getProfileDetailMeta(amznBaseRequest.getProfileId());
            if (null == profileDetailMeta) {
                throw AmznApiException.createBizException("不存在的店铺profileId:{}", amznBaseRequest.getProfileId());
            }
            String endpoint = profileDetailMeta.getEndpointUrl();
            requestUrl = String.format("%s%s", endpoint, amznBaseRequest.getUrl());
        }

        //追加url末尾参数
        if (amznBaseRequest.getAppendUrlEndParamMap() != null) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(requestUrl);
            amznBaseRequest.getAppendUrlEndParamMap().forEach(builder::queryParam);
            requestUrl = builder.build().encode().toString();
        }

        return requestUrl;
    }


    /**
     * http get方法
     */
    public String httpGet(AmznBaseRequest amznBaseRequest) {
        log.info("AmznBaseRequest httpGet baseDetail:{} ,body:{} , jsonBody:{}", amznBaseRequest.baseDetail(), amznBaseRequest.getBody(), amznBaseRequest.getJsonBody());

        Supplier<String> doGetRequestUrl = () -> {
            Map<String, Object> copyBodyMap = Maps.newHashMap(amznBaseRequest.getBody());
            if (StringUtils.isNotBlank(amznBaseRequest.getJsonBody())) {
                Map<String, String> map = GsonUtil.parseObject(amznBaseRequest.getJsonBody(), new TypeToken<Map<String, String>>() {
                });
                copyBodyMap.putAll(map);
            }

            String requestUrl = getRequestUrl(amznBaseRequest);
            if (!CollectionUtils.isEmpty(copyBodyMap)) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(requestUrl);
                copyBodyMap.forEach(builder::queryParam);
                requestUrl = builder.build().encode().toString();
            }
            return requestUrl;
        };

        Supplier<HttpHeaders> doGetHttpHeaders = () -> buildHttpHeaders(amznBaseRequest);

        try {
            ResponseEntity<String> responseEntity = this.executeHttpCall(amznAdvConfigManager.getApiClient(), doGetRequestUrl, HttpMethod.GET, doGetHttpHeaders, null, amznBaseRequest.getTimeOutIoRetryTimes(), amznBaseRequest.getAmznRateLimitRetryTimes());
            log.info("AmznBaseRequest get end respHeader:{}, result:{}", responseEntity.getHeaders(), resultLogBody(amznBaseRequest, responseEntity.getBody()));
            return responseEntity.getBody();
        } finally {
            amznBaseRequest.jsonBody(null);
        }
    }

    /**
     * http get方法
     */
    public ResponseEntity<String> httpGetWithResponse(AmznBaseRequest amznBaseRequest) {
        log.info("AmznBaseRequest httpGet baseDetail:{} ,body:{} , jsonBody:{}", amznBaseRequest.baseDetail(), amznBaseRequest.getBody(), amznBaseRequest.getJsonBody());

        Supplier<String> doGetRequestUrl = () -> {
            Map<String, Object> copyBodyMap = Maps.newHashMap(amznBaseRequest.getBody());
            if (StringUtils.isNotBlank(amznBaseRequest.getJsonBody())) {
                Map<String, String> map = GsonUtil.parseObject(amznBaseRequest.getJsonBody(), new TypeToken<Map<String, String>>() {
                });
                copyBodyMap.putAll(map);
            }

            String requestUrl = getRequestUrl(amznBaseRequest);
            if (!CollectionUtils.isEmpty(copyBodyMap)) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(requestUrl);
                copyBodyMap.forEach(builder::queryParam);
                requestUrl = builder.build().encode().toString();
            }
            return requestUrl;
        };

        Supplier<HttpHeaders> doGetHttpHeaders = () -> buildHttpHeaders(amznBaseRequest);

        try {
            ResponseEntity<String> responseEntity = this.executeHttpCall(amznAdvConfigManager.getApiClient(), doGetRequestUrl, HttpMethod.GET, doGetHttpHeaders, null, amznBaseRequest.getTimeOutIoRetryTimes(), amznBaseRequest.getAmznRateLimitRetryTimes());
            log.info("AmznBaseRequest get end respHeader:{}, result:{}", responseEntity.getHeaders(), resultLogBody(amznBaseRequest, responseEntity.getBody()));
            return responseEntity;
        } finally {
            amznBaseRequest.jsonBody(null);
        }
    }

    public String httpPost(AmznBaseRequest baseRequest) {
        log.info("AmznBaseRequest httpPost baseDetail:{} ,body:{} , jsonBody:{}", baseRequest.baseDetail(), baseRequest.getBody(), baseRequest.getJsonBody());
        ResponseEntity<String> responseEntity = this.httpPostEx(baseRequest, HttpMethod.POST);
        log.info("AmznBaseRequest httpPost respHeader:{}, result :{}", responseEntity.getHeaders(), resultLogBody(baseRequest, responseEntity.getBody()));
        return responseEntity.getBody();
    }

    public String httpPut(AmznBaseRequest baseRequest) {
        log.info("AmznBaseRequest httpPut baseDetail:{},body:{} , jsonBody:{}", baseRequest.baseDetail(), baseRequest.getBody(), baseRequest.getJsonBody());
        ResponseEntity<String> responseEntity = this.httpPostEx(baseRequest, HttpMethod.PUT);
        log.info("AmznBaseRequest httpPut respHeader:{}, result :{}", responseEntity.getHeaders(), resultLogBody(baseRequest, responseEntity.getBody()));
        return responseEntity.getBody();
    }

    public String httpDelete(AmznBaseRequest baseRequest) {
        log.info("AmznBaseRequest httpDelete baseDetail:{} ,body:{} , jsonBody:{}", baseRequest.baseDetail(), baseRequest.getBody(), baseRequest.getJsonBody());
        ResponseEntity<String> responseEntity = this.httpPostEx(baseRequest, HttpMethod.DELETE);
        log.info("AmznBaseRequest httpDelete respHeader:{}, result :{}", responseEntity.getHeaders(), resultLogBody(baseRequest, responseEntity.getBody()));
        return responseEntity.getBody();
    }

    private String resultLogBody(AmznBaseRequest baseRequest, String body) {
        Integer resultLogPrintLength = baseRequest.getResultLogPrintLength();
        if (resultLogPrintLength == null || resultLogPrintLength <= 0) {
            return body;
        }
        return StringUtils.substring(body, 0, resultLogPrintLength);
    }

    private ResponseEntity<String> httpPostEx(AmznBaseRequest amznBaseRequest, HttpMethod httpMethod) {

        Supplier<String> doGetRequestUrl = () -> getRequestUrl(amznBaseRequest);

        Supplier<HttpHeaders> doGetHttpHeaders = () -> buildHttpHeaders(amznBaseRequest);

        String body = StringUtils.isBlank(amznBaseRequest.getJsonBody()) ? requestGson.toJson(amznBaseRequest.getBody()) : amznBaseRequest.getJsonBody();

        try {
            return this.executeHttpCall(amznAdvConfigManager.getApiClient(), doGetRequestUrl, httpMethod, doGetHttpHeaders, body, amznBaseRequest.getTimeOutIoRetryTimes(), amznBaseRequest.getAmznRateLimitRetryTimes());
        } finally {
            amznBaseRequest.jsonBody(null);
        }
    }

    private HttpHeaders buildHttpHeaders(AmznBaseRequest amznBaseRequest) {
        MediaTypePair mediaTypePair = amznBaseRequest.getMediaTypePair();

        //request response mediaType
        HttpHeaders httpHeaders = buildBaseMediaTypeHeaders(amznBaseRequest, mediaTypePair);

        //额外headers
        if (null != amznBaseRequest.getHttpHeaders()) {
            httpHeaders.addAll(amznBaseRequest.getHttpHeaders());
        }

        //目前用于测试使用, 如果一个client里有调用多个api 会导致此值只被一个api使用
        SpecialClientDetail clientIdentity = AmznAdClientHelper.getClientIdentity();
        if (clientIdentity != null) {
            amznBaseRequest.specialClientDetail(clientIdentity);
            AmznAdClientHelper.clearClientIdentity();
        }
        //自定义账号
        if (amznBaseRequest.getSpecialClientDetail() != null) {
            backFillAccount(amznAdvConfigManager, amznBaseRequest.getSpecialClientDetail());
            mergeSpecialClientDetailHeaders(amznBaseRequest, httpHeaders);
        } else {
            mergeProfileHeaders(amznBaseRequest, httpHeaders);
        }
        return httpHeaders;
    }

    private void backFillAccount(AmznAdvConfigManager amznAdvConfigManager, SpecialClientDetail specialClientDetail) {
        if (null == specialClientDetail.getSpecialAccountType()) {
            return;
        }
        //todo: cyl
//        AmazonAccountConfigBO amazonAccountConfigBO = amznAdvConfigManager.getAmznAdvTokenCacheManager().getAmazonAccountMap().get(specialClientDetail.getSpecialAccountType());
//        if (null != amazonAccountConfigBO) {
//            specialClientDetail.setClientId(amazonAccountConfigBO.getAdvClientId());
//            specialClientDetail.setClientSecret(amazonAccountConfigBO.getAdvClientSecret());
//        }
    }


    @NotNull
    private static HttpHeaders buildBaseMediaTypeHeaders(AmznBaseRequest amznBaseRequest, MediaTypePair mediaTypePair) {
        HttpHeaders httpHeaders;
        if (null == mediaTypePair) {
            httpHeaders = RestTemplateUtil.getHttpHeadersDefaultApplicationJsonUtf8(amznBaseRequest.getMediaType(), amznBaseRequest.getMediaTypeAccept());
        } else {
            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf(mediaTypePair.getContentType()));

            if (mediaTypePair.getAccept() != null) {
                httpHeaders.setAccept(Lists.newArrayList(MediaType.valueOf(mediaTypePair.getAccept())));
            }
        }
        return httpHeaders;
    }

    private void mergeSpecialClientDetailHeaders(AmznBaseRequest amznBaseRequest, HttpHeaders httpHeaders) {
        SpecialClientDetail specialClientDetail = amznBaseRequest.getSpecialClientDetail();
        AccessTokenRequestMeta accessTokenRequestMeta = new AccessTokenRequestMeta();
        accessTokenRequestMeta.setClientId(specialClientDetail.getClientId());
        accessTokenRequestMeta.setClientSecret(specialClientDetail.getClientSecret());
        accessTokenRequestMeta.setRefreshToken(specialClientDetail.getRefreshToken());
        AmznTokenResponse advTokenResponse = this.amznAdvConfigManager.getAdvToken(accessTokenRequestMeta);
        httpHeaders.add(AmznConstants.HEADER_authorization, AmznConstants.HEADER_authorizationPrefix + advTokenResponse.getAccess_token());
        httpHeaders.add(AmznConstants.HEADER_clientId, specialClientDetail.getClientId());
        if (amznBaseRequest.getProfileId() != null) {
            httpHeaders.add(AmznConstants.HEADER_advertisingApiScope, amznBaseRequest.getProfileId());
        }
    }

    private void mergeProfileHeaders(AmznBaseRequest amznBaseRequest, HttpHeaders httpHeaders) {
        String profileId = amznBaseRequest.getProfileId();
        if (profileId == null) {
            throw new IllegalArgumentException("无profileId , requestDetail: " + amznBaseRequest.baseDetail());
        }
        //需要profileId的情况
        httpHeaders.add(AmznConstants.HEADER_advertisingApiScope, profileId);
        AmznTokenResponse advTokenResponse = this.amznAdvConfigManager.getAdvToken(profileId);
        httpHeaders.add(AmznConstants.HEADER_authorization, AmznConstants.HEADER_authorizationPrefix + advTokenResponse.getAccess_token());
        httpHeaders.add(AmznConstants.HEADER_clientId, advTokenResponse.getClientId());
    }

    @SneakyThrows
    protected ResponseEntity<String> executeHttpCall(RestTemplate client, Supplier<String> doGetRequestUrl, HttpMethod httpMethod, Supplier<HttpHeaders> doGetHttpHeaders, Object body, int timeOutRetry, int amznRateLimitRetry) {
        if (isMockInvoke) {
            return ResponseEntity.ok("");
        }

        String requestUrl = doGetRequestUrl.get();
        HttpHeaders httpHeaders = doGetHttpHeaders.get();
        Callable<ResponseEntity<String>> callable = () -> client.exchange(requestUrl, httpMethod, new HttpEntity<>(body, httpHeaders), String.class);

        //亚马逊偶尔会抽风说401
        callable = Amzn401UnauthorizedRetryWrapper.wrap(callable, 1);
        //亚马逊偶尔会抽风连接不上
        callable = AmznIOTimeOutRetryWrapper.wrap(callable, timeOutRetry);
        //接口被限流
        callable = AmznRateLimitRetryWrapper.wrap(callable, amznRateLimitRetry, requestUrl, httpMethod);

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = callable.call();
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                log.warn("请求亚马逊返回异常 traceId:{} ,url:{} response:{}", responseEntity.getHeaders()
                        .getFirst(AmznConstants.HEADER_requestId), requestUrl, responseEntity);
                throw AmznApiException.createBizException("请求亚马逊返回异常:{}", responseEntity.getStatusCode().value(), responseEntity.getBody());
            }
        } catch (HttpClientErrorException httpClientErrorException) {
            log.warn("请求亚马逊网络异常, responseHeaders:{}, responseErrorBody:{}", httpClientErrorException.getResponseHeaders(), httpClientErrorException.getResponseBodyAsString(), httpClientErrorException);
            throw AmznApiException.createBizException("请求亚马逊网络异常:{}", httpClientErrorException.getMessage());
        } catch (AmznApiRetryMaxException amznApiResponseException) {
            Throwable throwable = amznApiResponseException.getCause() != null ? amznApiResponseException.getCause() : amznApiResponseException;
            throw AmznApiException.createBizException(throwable, "请求亚马逊接口重试上限异常:" + throwable.getMessage());
        }
        return responseEntity;
    }

    public <T> T beforeReturnResult(AmznClientCrudTypeEnum crudTypeEnum, T res, Object... args) {
        if (res instanceof AmznClientResponseBeforeReturn) {
            ((AmznClientResponseBeforeReturn) res).beforeReturn(crudTypeEnum, args);
        } else if (res instanceof Collection) {
            for (Object obj : ((Collection<?>) res)) {
                if (obj instanceof AmznClientResponseBeforeReturn) {
                    ((AmznClientResponseBeforeReturn) obj).beforeReturn(crudTypeEnum, args);
                }
            }
        }
        return res;
    }

    public void beforeInvokeRequest(AmznBaseRequest amznBaseRequest, AmznAdvConfigManager amznAdvConfigManager) {
        Object sourceRequest = amznBaseRequest.getSourceRequest();
        if (sourceRequest instanceof AmznClientRequestBeforeInvoke) {
            ((AmznClientRequestBeforeInvoke) sourceRequest).beforeInvoke(amznAdvConfigManager);
        }
    }

    public void beforeInvokeRequest(Object sourceRequest) {
        if (sourceRequest instanceof AmznClientRequestBeforeInvoke) {
            ((AmznClientRequestBeforeInvoke) sourceRequest).beforeInvoke(this.amznAdvConfigManager);
        }
    }
}
