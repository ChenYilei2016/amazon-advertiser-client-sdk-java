package io.github.chenyilei2016.amznadclient.kernel.advice;


import io.github.chenyilei2016.amznadclient.kernel.cache.AmznAdvConfigManager;

/**
 * @author chenyilei
 * @date 2023/05/10 11:18
 */
public interface AmznClientRequestBeforeInvoke {

    public void beforeInvoke(AmznAdvConfigManager amznAdvConfigManager);
}
