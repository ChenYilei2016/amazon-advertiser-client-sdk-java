package io.github.chenyilei2016.myclient.kernel.utils;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author chenyilei
 * @date 2023/06/20 15:38
 */
public class MyLambdaUtils {
    /**
     * 获取列名称
     *
     * @param lambda
     * @return String
     */
    public static String getLambdaColumnName(Serializable lambda) {
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            String getter = serializedLambda.getImplMethodName();
            return Introspector.decapitalize(getter.replace("get", ""));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, R> String getFieldName(LambdaFunction<T, R> lambdaFunction) {
        return getLambdaColumnName(lambdaFunction);
    }
}
