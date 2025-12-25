package io.github.chenyilei2016.myclient.kernel.validate;

/**
 * 枚举值校验
 *
 * @author chenyilei
 * @date 2022/09/28 13:55
 */


import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
public @interface EnumValue {
    // 默认错误消息
    String message() default "枚举必须为指定值";

    Class<? extends BaseEnum> enumClass();

    boolean nullEnabled() default true;

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

}
