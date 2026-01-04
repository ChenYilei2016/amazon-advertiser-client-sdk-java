package io.github.chenyilei2016.amznadclient.kernel.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenMetaRequest {
    private String grantType = AmznGrantTypeEnum.REFRESH_TOKEN.getCode();

    private String clientId;

    private String clientSecret;

    private String refreshToken;

}