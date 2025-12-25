package io.github.chenyilei2016.myclient.kernel.errors;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @formatter:off
 * {
 *     "targetingClauses": {
 *         "error": [
 *             {
 *                 "errors": [
 *                     {
 *                         "errorType": "duplicateValueError",
 *                         "errorValue": {
 *                             "duplicateValueError": {
 *                                 "cause": {
 *                                     "location": "$.targetingClauses[0].expression",
 *                                     "trigger": "[{\"type\":\"ASIN_EXPANDED_FROM\",\"value\":\"B08K2Z78R3\"}]"
 *                                 },
 *                                 "message": "TargetingClause with campaignId=257441709563399, adGroupId=123806928861543, expressionType=MANUAL, expression=[(value=B08K2Z78R3, type=ASIN_EXPANDED_FROM)] already exists!",
 *                                 "reason": "DUPLICATE_VALUE"
 *                             }
 *                         }
 *                     }
 *                 ],
 *                 "index": 0
 *             }
 *         ],
 *         "success": []
 *     }
 * }
 * @formatter:on
 * @author chenyilei
 * @date 2023/04/14 14:20
 */
public class AmznErrorFormatter {
    //防止冲突临时创建
    public static List<AmznDefaultErrorResponse> formatErrorWithIndex(List<AmznOriginHierarchyError> errors) {
        if (CollectionUtils.isEmpty(errors)) {
            return Collections.emptyList();
        }
        List<AmznDefaultErrorResponse> result = new ArrayList<>();
        for (AmznOriginHierarchyError error : errors) {
            List<AmznOriginHierarchyError.ErrorsDTO> errorDTOS = error.getErrors();
            if (CollectionUtils.isEmpty(errorDTOS)) {
                continue;
            }
            for (AmznOriginHierarchyError.ErrorsDTO errorDTO : errorDTOS) {
                String errorType = errorDTO.getErrorType();
                AmznOriginHierarchyError.ErrorValueDTO errorValueDTO = errorDTO.getErrorValue().get(errorType);
                if (errorValueDTO == null) {
                    //没有对应的error , 取一个
                    if (null == errorDTO.getErrorValue()) {
                        continue;
                    }
                    errorDTO.getErrorValue().entrySet().stream().findFirst().ifPresent(errorValueDTOEntry -> {
                        AmznDefaultErrorResponse e = new AmznDefaultErrorResponse(errorValueDTOEntry.getKey(), errorValueDTOEntry.getValue().getMessage());
                        e.setIndex(error.getIndex());
                        e.setReason(errorValueDTOEntry.getValue().getReason());
                        result.add(e);
                    });
                } else {
                    AmznDefaultErrorResponse e = new AmznDefaultErrorResponse(errorType, errorValueDTO.getMessage());
                    e.setIndex(error.getIndex());
                    e.setReason(errorValueDTO.getReason());
                    result.add(e);
                }
            }
        }
        return result;
    }


    public static List<AmznDefaultErrorResponse> formatError(List<AmznOriginHierarchyError> errors) {
        return formatErrorWithIndex(errors);
    }

    public static List<AmznOriginHierarchyError.ErrorsDTO> ignoreTypeError(List<AmznOriginHierarchyError> amznOriginHierarchyErrors, String errorType) {
        if (null == amznOriginHierarchyErrors || amznOriginHierarchyErrors.size() == 0) {
            return Collections.emptyList();
        }
        List<AmznOriginHierarchyError.ErrorsDTO> ignores = new ArrayList<>();

        CollectionUtil.filter(amznOriginHierarchyErrors, amznSpOriginError -> {
            boolean filter = true;
            List<AmznOriginHierarchyError.ErrorsDTO> errors = amznSpOriginError.getErrors();
            if (errors == null || errors.size() == 0) {
                return false;
            }
            for (AmznOriginHierarchyError.ErrorsDTO error : errors) {
                if (errorType.equalsIgnoreCase(error.getErrorType())) {
                    ignores.add(error);
                    filter = false;
                    break;
                }
            }
            return filter;
        });
        return ignores;
    }

}
