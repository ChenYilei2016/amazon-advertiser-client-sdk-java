package io.github.chenyilei2016.myclient.kernel.validate;

/**
 * 期望前后无空格
 * trailing whitespaces
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
@Constraint(validatedBy = {ExpectNoTrailingWhitespacesValidator.class})
public @interface ExpectNoTrailingWhitespaces {
    // 默认错误消息
    String message() default "期望前后无空格";

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

}
