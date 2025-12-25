package io.github.chenyilei2016.amznadclient.kernel.errors;

/**
 * @author chenyilei
 * @date 2022/11/15 10:51
 */
public interface AmznCommonResponse {
    default String getId() {
        return null;
    }

    String getCode();

    String getDescription();
}
