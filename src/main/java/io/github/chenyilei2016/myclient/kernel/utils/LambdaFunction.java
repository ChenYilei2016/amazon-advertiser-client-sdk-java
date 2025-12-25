package io.github.chenyilei2016.myclient.kernel.utils;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author chenyilei
 * @date 2023/06/20 15:24
 */
@FunctionalInterface
public interface LambdaFunction<T, R> extends Serializable, Function<T, R> {


}