package io.github.chenyilei2016.amznadclient.kernel.validate;

/**
 * 单词数校验
 *
 * @author chenyilei
 * @see javax.validation.constraints.Size
 */

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
@Constraint(validatedBy = {WordSizeValidator.class})
public @interface WordSize {
    // 默认错误消息
    String message() default "词的数量必须在 {min} and {max}";

    int min() default 1;

    int max() default Integer.MAX_VALUE;

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

}
