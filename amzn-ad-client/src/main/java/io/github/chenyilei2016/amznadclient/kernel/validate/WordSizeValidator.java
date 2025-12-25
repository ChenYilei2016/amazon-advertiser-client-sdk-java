package io.github.chenyilei2016.amznadclient.kernel.validate;


import io.github.chenyilei2016.amznadclient.kernel.utils.WordUtil;
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
public class WordSizeValidator implements ConstraintValidator<WordSize, String> {

    private WordSize wordSize;

    @Override
    public void initialize(WordSize wordSize) {
        ConstraintValidator.super.initialize(wordSize);
        this.wordSize = wordSize;
    }

    @Override
    @SneakyThrows
    public boolean isValid(String checkValue, ConstraintValidatorContext context) {
        int checkWordCounts = WordUtil.countWords(checkValue);
        if (checkWordCounts < wordSize.min() || checkWordCounts > wordSize.max()) {
            return false;
        }
        return true;
    }
}