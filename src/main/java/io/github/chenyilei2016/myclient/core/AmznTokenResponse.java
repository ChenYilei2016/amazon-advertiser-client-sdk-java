package io.github.chenyilei2016.myclient.core;

import lombok.Data;

import java.io.Serializable;

@Data
public class AmznTokenResponse implements Serializable {

    private String access_token;

    private String refresh_token;

    private String token_type;

    //秒
    private Long expires_in;


    ////////自存储额外信息

    private String clientId;

    private String marketplaceId;
}
