package io.github.chenyilei2016.amznadclient.kernel.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author chenyilei
 * @date 2023/04/17 17:18
 */
public class EqualValidValueValidator implements ConstraintValidator<EqualValidValue, Object> {

    private String[] strValues;
    private int[] intValues;

    @Override
    public void initialize(EqualValidValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String) {
            if (checkValidStr((String) value)) return true;
            context.buildConstraintViolationWithTemplate("枚举值必须属于:" + Arrays.asList(strValues)).addConstraintViolation();
            return false;
        } else if (value instanceof Integer) {
            if (checkValidInteger((Integer) value)) return true;
            context.buildConstraintViolationWithTemplate("枚举值必须属于:" + Arrays.toString(intValues)).addConstraintViolation();
            return false;
        } else if (value instanceof Collection) {
            Collection<?> col = (Collection<?>) value;
            for (Object o : col) {
                if (o instanceof Collection) {
                    return false;
                }
                boolean valid = this.isValid(o, context);
                if (valid) {
                    //通过
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkValidInteger(Integer value) {
        for (int s : intValues) {
            if (s == value) {
                return true;
            }
        }
        return false;
    }

    private boolean checkValidStr(String value) {
        for (String s : strValues) {
            if (s.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
