package io.github.chenyilei2016.amznadclient.kernel.validate;

import cn.hutool.core.map.WeakConcurrentMap;
import io.github.chenyilei2016.amznadclient.kernel.core.BaseEnum;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 校验一个属性是否是指定枚举里的值
 *
 * @author chenyilei
 * @date 2022/09/28 13:56
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private Class<? extends BaseEnum> enumClass;
    private EnumValue enumValue;
    private static final Map<Class<?>, List> existEnumCodeCache = new WeakConcurrentMap<>();

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.enumClass = constraintAnnotation.enumClass();
        this.enumValue = constraintAnnotation;
    }

    @Override
    @SneakyThrows
    public boolean isValid(Object checkValue, ConstraintValidatorContext context) {
        if (enumValue.nullEnabled() && checkValue == null) {
            return true;
        }
        if (!this.enumClass.isEnum()) {
            context.buildConstraintViolationWithTemplate("value:" + checkValue + ", 不是一个枚举, 不可使用 @EnumValue").addConstraintViolation();
            return false;
        }

        List existEnumCodes = existEnumCodeCache.get(this.enumClass);
        if (existEnumCodes == null) {
            existEnumCodes = new ArrayList(8);
            for (BaseEnum enumConstant : this.enumClass.getEnumConstants()) {
                Object existValue = enumConstant.getCode();
                existEnumCodes.add(existValue);
            }
            existEnumCodeCache.put(this.enumClass, existEnumCodes);
        }

        if (checkValue instanceof Collection) {
            for (Object o : ((Collection<?>) checkValue)) {
                if (!existEnumCodes.contains(o)) {
                    context.buildConstraintViolationWithTemplate("枚举值必须属于:" + existEnumCodes).addConstraintViolation();
                    return false;
                }
            }
        } else {
            if (!existEnumCodes.contains(checkValue)) {
                context.buildConstraintViolationWithTemplate("枚举值必须属于:" + existEnumCodes).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}