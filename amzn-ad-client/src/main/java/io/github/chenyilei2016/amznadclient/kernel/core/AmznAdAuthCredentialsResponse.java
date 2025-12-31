package io.github.chenyilei2016.amznadclient.kernel.core;

import lombok.Data;

/**
 * @author chenyilei
 * @since 2025/12/31 14:12
 */
@Data
public class AmznAdAuthCredentialsResponse {

    private String grantType = AmznGrantTypeEnum.REFRESH_TOKEN.getCode();

    private String advClientId;

    private String advClientSecret;

}
