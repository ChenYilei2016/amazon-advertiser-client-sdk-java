package io.github.chenyilei2016.myclient.kernel.wrapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.Callable;

/**
 * @author chenyilei
 * @date 2023/06/16 17:19
 */
public class Amzn401UnauthorizedRetryWrapper<T> implements Callable<T> {

    private final Callable<T> delegate;

    private int retryTimes;

    public Amzn401UnauthorizedRetryWrapper(Callable<T> delegate, int retryTimes) {
        this.delegate = delegate;
        this.retryTimes = retryTimes;
    }

    public static <T> Callable<T> wrap(Callable<T> callable, int timeOutRetry) {
        if (timeOutRetry > 0) {
            return new Amzn401UnauthorizedRetryWrapper<>(callable, timeOutRetry);
        }
        return callable;
    }

    @Override
    public T call() throws Exception {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                return delegate.call();
            } catch (org.springframework.web.client.HttpClientErrorException e) {
                if (e instanceof HttpClientErrorException.Unauthorized || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    retryTimes--;
                    if (retryTimes >= 0) {
                        //还要重试, 无视错误
                        //ignore
                        Thread.sleep(300L);
                    } else {
                        //超出重试次数
                        throw e;
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
