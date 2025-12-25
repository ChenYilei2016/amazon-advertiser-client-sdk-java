package io.github.chenyilei2016.myclient.kernel.core;

/**
 * @author chenyilei
 * @date 2022/09/28 14:22
 * @see com.zbycorp.fenghuo.domain.common.validate.EnumValueValidator
 */
public interface BaseEnum {

    default String enumClass() {
        return this.getClass().getName();
    }

    /**
     * 枚举一般作为value的字段
     */
    Object getCode();

}
