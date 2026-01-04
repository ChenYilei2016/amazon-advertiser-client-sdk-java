package io.github.chenyilei2016.amznadclient.kernel.exceptions;


import org.slf4j.helpers.MessageFormatter;

/**
 * @author chenyilei
 * @date 2023/04/06 14:57
 */
public class AmznApiException extends RuntimeException {
    public AmznApiException(Throwable e, String format, Object... args) {
        super(MessageFormatter.arrayFormat(format, args).getMessage(), e);
    }

    public AmznApiException(Throwable e) {
        super(e.getMessage(), e);
    }

    public AmznApiException(String format, Object... args) {
        super(MessageFormatter.arrayFormat(format, args).getMessage());
    }


    public static AmznApiException createBizException(String format, Object... args) {
        return new AmznApiException(MessageFormatter.arrayFormat(format, args).getMessage());
    }

    public static AmznApiException createBizException(Throwable e, String format, Object... args) {
        return new AmznApiException(e, MessageFormatter.arrayFormat(format, args).getMessage());
    }
}
