package io.github.chenyilei2016.myclient.kernel.exceptions;


import org.slf4j.helpers.MessageFormatter;

/**
 * @author chenyilei
 * @date 2023/04/06 14:57
 */
public class AmznApiRetryMaxException extends RuntimeException {
    public AmznApiRetryMaxException(Throwable e, String format, Object... args) {
        super(MessageFormatter.arrayFormat(format, args).getMessage(), e);
    }

    public AmznApiRetryMaxException(Throwable e) {
        super(e.getMessage(), e);
    }

    public AmznApiRetryMaxException(String format, Object... args) {
        super(MessageFormatter.arrayFormat(format, args).getMessage());
    }


}
