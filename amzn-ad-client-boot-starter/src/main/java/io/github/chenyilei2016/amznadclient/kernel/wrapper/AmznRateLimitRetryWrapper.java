package io.github.chenyilei2016.amznadclient.kernel.wrapper;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiRetryMaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.util.concurrent.Callable;

/**
 * @author chenyilei
 * @date 2023/04/06 14:03
 */
@Slf4j
public class AmznRateLimitRetryWrapper implements Callable<ResponseEntity<String>> {

    private final Callable<ResponseEntity<String>> delegate;

    /**
     * 递减... 一开始和total一样
     */
    private int retryTimes;
    private final int totalRetryTimes;
    private final String requestUrl;
    private final HttpMethod httpMethod;

    public AmznRateLimitRetryWrapper(Callable<ResponseEntity<String>> delegate, int retryTimes, String requestUrl, HttpMethod httpMethod) {
        this.delegate = delegate;
        this.retryTimes = retryTimes;
        this.totalRetryTimes = retryTimes;
        this.requestUrl = requestUrl;
        this.httpMethod = httpMethod;
    }

    public static Callable<ResponseEntity<String>> wrap(Callable<ResponseEntity<String>> callable, int amznRateLimitRetryTimes, String requestUrl, HttpMethod httpMethod) {
        if (amznRateLimitRetryTimes > 0) {
            return new AmznRateLimitRetryWrapper(callable, amznRateLimitRetryTimes, requestUrl, httpMethod);
        }
        return callable;
    }


    private long findRetryTime(HttpHeaders responseHeaders, HttpClientErrorException httpClientErrorException, HttpStatus statusCode) {
        long retryAfterTime = 0;
        String headerRetryAfter = responseHeaders.getFirst("Retry-After");
        if (null != headerRetryAfter
                || httpClientErrorException instanceof HttpClientErrorException.TooManyRequests
                || StringUtils.contains(httpClientErrorException.getMessage(), "Too Many Requests")
                || HttpStatus.TOO_MANY_REQUESTS == statusCode) {
            if (null != headerRetryAfter && NumberUtil.isLong(headerRetryAfter)) {
                retryAfterTime += Long.parseLong(headerRetryAfter);
            }
            retryAfterTime = retryAfterTime + RandomUtil.randomInt(100, 500) + currentRetryCount() * 300L;
        }
        return retryAfterTime;
    }

    @Override
    public ResponseEntity<String> call() throws Exception {
        ResponseEntity<String> responseEntity = null;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                responseEntity = delegate.call();

                //成功
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    return responseEntity;
                }

                if (responseEntity.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    throw HttpClientErrorException.create(
                            responseEntity.getStatusCode(),
                            responseEntity.getStatusCode().getReasonPhrase(),
                            responseEntity.getHeaders(),
                            StringUtils.getBytes(responseEntity.getBody(), Charset.defaultCharset()), Charset.defaultCharset()
                    );
                }

                return responseEntity;
            } catch (HttpClientErrorException httpClientErrorException) {
                final HttpStatus statusCode = httpClientErrorException.getStatusCode();
                final HttpHeaders responseHeaders = httpClientErrorException.getResponseHeaders();
                if (null == responseHeaders) {
                    throw httpClientErrorException;
                }

                //有重试信息则进行重试
                long retryAfterTime = findRetryTime(responseHeaders, httpClientErrorException, statusCode);

                if (retryTimes > 0 && retryAfterTime > 0) {
                    //若有重试次数 && 是需要重试的
                    retryTimes--;
                    log.warn("AmznRateLimitRetryWrapper 请求亚马逊被限流, url:{} ,sleepTime:{}, totalTimes:{}, retryCount:{}, headers:{}", requestUrl, retryAfterTime, totalRetryTimes, currentRetryCount(), responseHeaders.toSingleValueMap());
                    Thread.sleep(retryAfterTime);
                } else if (retryTimes <= 0 && retryAfterTime > 0) {
                    //重试次数用完了, 依然失败
                    log.warn("AmznRateLimitRetryWrapper 请求亚马逊重试多次依然失败,重试次数:{}, url:{} ,headers:{},method:{}", totalRetryTimes, requestUrl, responseHeaders, httpMethod);
                    throw new AmznApiRetryMaxException(httpClientErrorException, "请求亚马逊接口重试多次异常, +" + httpClientErrorException.getMessage());
                } else {
                    log.warn("AmznRateLimitRetryWrapper 不是重试的HttpClientErrorException, 请求亚马逊接口出错 非限流重试  ,url:{} response:{}", requestUrl, responseEntity);
                    throw httpClientErrorException;
                }
            }
        }
        throw new InterruptedException("发生中断异常");
    }

    public int currentRetryCount() {
        return totalRetryTimes - retryTimes;
    }
}

