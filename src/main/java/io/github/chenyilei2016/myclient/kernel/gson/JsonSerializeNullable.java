package io.github.chenyilei2016.myclient.kernel.gson;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * null字段也输出
 *
 * @author chenyilei
 * @date 2023/06/09 13:40
 */
@Retention(RUNTIME)
@Target({FIELD, TYPE})
public @interface JsonSerializeNullable {
}
