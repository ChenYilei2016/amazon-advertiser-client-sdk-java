package io.github.chenyilei2016.myclient.kernel.wrapper;

import io.github.chenyilei2016.myclient.kernel.exceptions.AmznApiRetryMaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;

/**
 * @author chenyilei
 * @date 2023/04/06 14:03
 */
@Slf4j
public class AmznIOTimeOutRetryWrapper<T> implements Callable<T> {

    private final Callable<T> delegate;

    private int retryTimes;

    public AmznIOTimeOutRetryWrapper(Callable<T> delegate, int retryTimes) {
        this.delegate = delegate;
        this.retryTimes = retryTimes;
    }

    public static <T> Callable<T> wrap(Callable<T> callable, int timeOutRetry) {
        if (timeOutRetry > 0) {
            return new AmznIOTimeOutRetryWrapper<>(callable, timeOutRetry);
        }
        return callable;
    }

    @Override
    public T call() throws Exception {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                return delegate.call();
            } catch (org.springframework.web.client.ResourceAccessException | java.net.SocketTimeoutException e) {
                if (StringUtils.contains(e.getMessage(), "I/O error") &&
                        StringUtils.contains(e.getMessage(), "timed out")) {
                    retryTimes--;
                    if (retryTimes >= 0) {
                        //还要重试, 无视错误
                        //ignore
                        log.warn("发生一次IOTimeOut异常, 剩余重试次数: {}", retryTimes);
                        Thread.sleep(200L);
                    } else {
                        //超出重试次数
                        throw new AmznApiRetryMaxException(e, "IOTimeOut重试超次数 , " + e.getMessage());
                    }
                } else {
                    //其他以报错方式抛出
                    throw e;
                }
            }
        }
        throw new InterruptedException("发生中断异常");
    }
}
