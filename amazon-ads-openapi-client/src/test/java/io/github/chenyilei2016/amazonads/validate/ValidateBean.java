package io.github.chenyilei2016.amazonads.validate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author chenyilei
 * @date 2022/09/06 10:14
 */
public interface ValidateBean extends Serializable {
    Validator validator = ((ValidatorFactoryImpl) Validation.buildDefaultValidatorFactory())
            .getValidator();


    static void validateCannotNullThrow(Object obj, Class<?>... groups) {
        if (null == obj) {
            throw new IllegalArgumentException("参数不能为空");
        }
        validateThrow(obj, groups);
    }

    static void validateThrow(Object obj, Class<?>... groups) {
        if (null == obj) {
            return;
        }


        Set<ConstraintViolation<Object>> validate = validator.validate(obj, groups);
        if (!validate.isEmpty()) {
            ConstraintViolation<Object> validateBeanConstraintViolation = validate.stream().findFirst().get();
            throw new IllegalArgumentException(
                    validateBeanConstraintViolation.getPropertyPath() + ":" + validateBeanConstraintViolation.getMessage()
            );
        }

    }

    static void validateThrowFirst(List<?> list, Class<?>... groups) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (Object obj : list) {
            validateThrow(obj, groups);
        }
    }

    default void selfValidateThrow() {
        validateThrow(this);
    }
}
