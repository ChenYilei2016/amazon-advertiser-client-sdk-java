package io.github.chenyilei2016.myclient.kernel.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenyilei
 * @date 2023/08/29 10:14
 */
@UtilityClass
public class WordUtil {
    public static int countWords(String sentence) {
        if (StringUtils.isBlank(sentence)) {
            return 0;
        }

        int wordCount = 0;
        boolean insideWord = false;

        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);

            if (Character.isWhitespace(c)) {
                insideWord = false;
            } else {
                if (!insideWord) {
                    wordCount++;
                    insideWord = true;
                }
            }
        }
        return wordCount;
    }


    public static boolean hasTrailingWhitespaces(String checkValue) {
        if (checkValue == null) {
            return false;
        }
        if (StringUtils.startsWith(checkValue, " ") || StringUtils.endsWith(checkValue, " ")) {
            return true;
        }
        return false;
    }
}
