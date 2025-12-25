package io.github.chenyilei2016.amznadclient.kernel.support;

import lombok.Data;

/**
 * @author chenyilei
 * @date 2023/05/15 14:15
 */
@Data
public class MediaTypePair {

    private String contentType;

    private String accept;

    public static MediaTypePair of(String contentType, String accept) {
        MediaTypePair mediaTypePair = new MediaTypePair();
        mediaTypePair.setContentType(contentType);
        mediaTypePair.setAccept(accept);
        return mediaTypePair;
    }
}
