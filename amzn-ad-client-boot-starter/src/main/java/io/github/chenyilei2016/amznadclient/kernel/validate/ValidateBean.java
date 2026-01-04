package io.github.chenyilei2016.amznadclient.kernel.validate;

import cn.hutool.core.collection.CollectionUtil;
import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author chenyilei
 * @date 2022/09/06 10:14
 */
public interface ValidateBean extends Serializable {
    Validator validator = ((ValidatorFactoryImpl) Validation.buildDefaultValidatorFactory())
            .usingContext().allowOverridingMethodAlterParameterConstraint(true) //允许子类重写规则
            .getValidator();

    /**
     * 校验前可能要对自身字段做一些修改
     */
    interface ExecuteBeforeValidate {
        void beforeValidate();
    }

    interface OtherValidate {
        void validate();
    }

    /**
     * 校验后可能要对自身字段做一些修改
     */
    interface ExecuteAfterValidate {
        void afterValidate();
    }

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

        if (obj instanceof ExecuteBeforeValidate) {
            ((ExecuteBeforeValidate) obj).beforeValidate();
        }

        Set<ConstraintViolation<Object>> validate = validator.validate(obj, groups);
        if (!validate.isEmpty()) {
            ConstraintViolation<Object> validateBeanConstraintViolation = validate.stream().findFirst().get();
            throw new IllegalArgumentException(
                    validateBeanConstraintViolation.getPropertyPath() + ":" + validateBeanConstraintViolation.getMessage()
            );
        }

        if (obj instanceof OtherValidate) {
            ((OtherValidate) obj).validate();
        }

        if (obj instanceof ExecuteAfterValidate) {
            ((ExecuteAfterValidate) obj).afterValidate();
        }
    }

    static void validateThrowFirst(List<?> list, Class<?>... groups)  {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (Object obj : list) {
            validateThrow(obj, groups);
        }
    }

    default void selfValidateThrow()  {
        validateThrow(this);
    }
}
