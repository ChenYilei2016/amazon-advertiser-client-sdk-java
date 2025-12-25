package io.github.chenyilei2016.myclient.model.spv3;


import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;

/**
 * BROAD_MATCH	Match if the queried value contains the filter value. (substring matching)
 * EXACT_MATCH	Match if the queried value is exactly equivalent to the filter value.
 *
 * @author chenyilei
 * @date 2023/04/12 19:55
 */
public enum AmznCommonQueryTermMatchTypeEnum implements BaseEnum {
    BROAD_MATCH, EXACT_MATCH;

    @Override
    public Object getCode() {
        return this.name();
    }
}
