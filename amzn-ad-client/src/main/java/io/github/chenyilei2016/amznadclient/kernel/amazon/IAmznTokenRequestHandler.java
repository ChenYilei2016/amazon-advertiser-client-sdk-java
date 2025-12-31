package io.github.chenyilei2016.amznadclient.kernel.amazon;

import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenyilei
 * @since 2025/12/31 14:02
 */
public interface IAmznTokenRequestHandler {


    AmznTokenResponse doRefreshToken(AccessTokenMetaRequest accessTokenMetaRequest);



}
