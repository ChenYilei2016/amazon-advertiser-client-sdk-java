package io.github.chenyilei2016.amznadclient.kernel.spi;

import io.github.chenyilei2016.amznadclient.kernel.core.AccessTokenMetaRequest;
import io.github.chenyilei2016.amznadclient.kernel.core.AmznTokenResponse;

/**
 * @author chenyilei
 * @since 2025/12/31 14:02
 */
public interface IAmznTokenRequestProvider {


    AmznTokenResponse doRefreshToken(AccessTokenMetaRequest accessTokenMetaRequest);



}
