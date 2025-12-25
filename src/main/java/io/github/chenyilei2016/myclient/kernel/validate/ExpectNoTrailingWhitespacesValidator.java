package io.github.chenyilei2016.myclient.kernel.validate;

import io.github.chenyilei2016.myclient.kernel.utils.WordUtil;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * word size validator
 *
 * @author chenyilei
 * @date 2022/09/28 13:56
 * @see javax.validation.constraints.Size
 * @see javax.validation.constraints.Min
 */
public class ExpectNoTrailingWhitespacesValidator implements ConstraintValidator<ExpectNoTrailingWhitespaces, String> {

    private ExpectNoTrailingWhitespaces trailingWhitespaces;

    @Override
    public void initialize(ExpectNoTrailingWhitespaces trailingWhitespaces) {
        ConstraintValidator.super.initialize(trailingWhitespaces);
        this.trailingWhitespaces = trailingWhitespaces;
    }

    @Override
    @SneakyThrows
    public boolean isValid(String checkValue, ConstraintValidatorContext context) {
        if (checkValue == null) {
            return true;
        }
        if (WordUtil.hasTrailingWhitespaces(checkValue)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(trailingWhitespaces.message()).addConstraintViolation();
            return false;
        }
        return true;
    }
}