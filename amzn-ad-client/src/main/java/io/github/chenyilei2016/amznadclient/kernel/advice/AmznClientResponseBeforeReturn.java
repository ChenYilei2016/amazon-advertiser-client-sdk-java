package io.github.chenyilei2016.amznadclient.kernel.advice;


/**
 * @author chenyilei
 * @date 2023/05/10 11:18
 */
public interface AmznClientResponseBeforeReturn {
    public void beforeReturn(AmznClientCrudTypeEnum crudTypeEnum, Object... args);
}
