package io.github.chenyilei2016.amznadclient;

import com.google.common.collect.Maps;
import io.github.chenyilei2016.amznadclient.kernel.advice.AmznClientCrudTypeEnum;
import io.github.chenyilei2016.amznadclient.kernel.support.MediaTypePair;
import io.github.chenyilei2016.amznadclient.kernel.support.SpecialClientDetail;
import lombok.*;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenyilei
 * @date 2023/04/06 16:50
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class AmznBaseRequest {

    private static final Pattern urlReplacePattern = Pattern.compile("\\{([^}]+)}");
    /**
     * 亚马逊概念的profileId
     */
    private transient String profileId;


    /**
     * 如果没有profileId, 会使用此配置
     */
    private transient SpecialClientDetail specialClientDetail;

    /**
     * 请求参数,
     * get会拼接到url上
     * post放在消息体 , 如果jsonBody不为null则会忽略此值!
     */
    private transient Map<String, Object> body = Maps.newHashMap();

    private transient HttpHeaders httpHeaders;

    /**
     * post请求使用, 如果此值不为null 直接会忽视body属性 {@link AmznBaseRequest#body}
     */
    private transient String jsonBody = null;

    /**
     * 是否更改调用地址
     */
    private transient String endpointUrlPrefix = null;

    /**
     * 请求url
     */
    private transient String url;

    /**
     * 在mediaTypePair没有的情况下
     * 请求mediaType, 如果只有此值 , 没有accept, 则mediaTypeAccept和此值一样
     */
    private transient String mediaType;

    private transient String mediaTypeAccept;

    /**
     * 有此值则无视
     * mediaType + mediaTypeAccept
     */
    private transient MediaTypePair mediaTypePair;


    /**
     * io timeout时候进行retry , 默认0 就是没有重试
     */
    private transient int timeOutIoRetryTimes = 0;

    /**
     * amzn rate limit retry
     */
    private transient int amznRateLimitRetryTimes = 15;

    /**
     * 单纯追加在url后面的参数, 不论 get or post
     */
    private transient Map<String, Object> appendUrlEndParamMap = null;

    private transient Integer resultLogPrintLength = 2048;

    private transient Object sourceRequest;

    /**
     * 亚马逊api操作类型
     */
    private transient AmznClientCrudTypeEnum crudTypeEnum;


    public static AmznBaseRequest builder() {
        return new AmznBaseRequest();
    }

    public AmznBaseRequest mediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public AmznBaseRequest mediaTypeAccept(String mediaTypeAccept) {
        this.mediaTypeAccept = mediaTypeAccept;
        return this;
    }

    public AmznBaseRequest mediaTypePair(MediaTypePair mediaTypePair) {
        this.mediaTypePair = mediaTypePair;
        return this;
    }

    public AmznBaseRequest url(String url, Object... replaceArgs) {
        if (null != replaceArgs && replaceArgs.length > 0) {
            int i = 0;
            Matcher matcher = urlReplacePattern.matcher(url);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(sb, String.valueOf(replaceArgs[i++]));
            }
            matcher.appendTail(sb);
            url = sb.toString();
        }
        this.url = url;
        return this;
    }

    public AmznBaseRequest profileId(String profileId) {
        this.profileId = profileId;
        return this;
    }

    public AmznBaseRequest advertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
        return this;
    }

    public AmznBaseRequest entityId(String entityId) {
        this.entityId = entityId;
        return this;
    }


    public AmznBaseRequest bodyKeyValue(String key, Object value) {
        this.body.put(key, value);
        return this;
    }

    public AmznBaseRequest bodyKeyValue(boolean judgeNull, String key, Object value) {
        if (judgeNull && value == null) {
            return this;
        }
        this.body.put(key, value);
        return this;
    }

    public AmznBaseRequest headerValue(String key, String value) {
        if (this.httpHeaders == null) {
            this.httpHeaders = new HttpHeaders();
        }
        this.httpHeaders.add(key, value);
        return this;
    }

    public AmznBaseRequest jsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
        return this;
    }

    public AmznBaseRequest crudTypeEnum(AmznClientCrudTypeEnum crudTypeEnum) {
        this.crudTypeEnum = crudTypeEnum;
        return this;
    }

    public AmznBaseRequest endpointUrlPrefix(String endpointUrlPrefix) {
        this.endpointUrlPrefix = endpointUrlPrefix;
        return this;
    }

    public AmznBaseRequest timeOutIoRetry(int times) {
        this.timeOutIoRetryTimes = times;
        return this;
    }

    public AmznBaseRequest specialClientDetail(SpecialClientDetail specialClientDetail) {
        this.specialClientDetail = specialClientDetail;
        return this;
    }

    public AmznBaseRequest resultLogPrintLength(Integer resultLogPrintLength) {
        this.resultLogPrintLength = resultLogPrintLength;
        return this;
    }

    public AmznBaseRequest resultLogPrintLengthNoLimit() {
        this.resultLogPrintLength = -1;
        return this;
    }

    public AmznBaseRequest sourceRequest(Object sourceRequest, Runnable runnable) {
        this.sourceRequest = sourceRequest;
        if (runnable != null) {
            runnable.run();
        }
        return this;
    }


    public AmznBaseRequest refreshToken(String accountType, String refreshToken) {
        this.specialClientDetail = new SpecialClientDetail();
        this.specialClientDetail.setRefreshToken(refreshToken);
        this.specialClientDetail.setSpecialAccountType(accountType);
        return this;
    }


    public String baseDetail() {
        return "{" +
                "profileId='" + profileId + '\'' +
                ", endpointUrlPrefix='" + endpointUrlPrefix + '\'' +
                ", url='" + url + '\'' +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }

    /**
     * 单纯追加在url后面的参数, 不论 get or post
     */
    public AmznBaseRequest appendUrlEndParam(Boolean judgeNull, String key, Object value) {
        if (judgeNull && value == null) {
            return this;
        }
        if (this.appendUrlEndParamMap == null) {
            this.appendUrlEndParamMap = Maps.newHashMap();
        }
        appendUrlEndParamMap.put(key, value);
        return this;
    }
}
