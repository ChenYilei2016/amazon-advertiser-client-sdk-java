package io.github.chenyilei2016.myclient.kernel.errors;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author chenyilei
 * @date 2023/04/14 15:03
 * @see AmznErrorFormatter
 */
@NoArgsConstructor
@Data
public class AmznOriginHierarchyError {

    @SerializedName("errors")
    private List<ErrorsDTO> errors;

    @SerializedName("index")
    private Integer index;

    @NoArgsConstructor
    @Data
    public static class ErrorsDTO {
        @SerializedName("errorType")
        private String errorType;

        @SerializedName("errorValue")
        private Map<String, ErrorValueDTO> errorValue;
    }

    @NoArgsConstructor
    @Data
    public static class ErrorValueDTO {
        @SerializedName("cause")
        private CauseDTO cause;
        @SerializedName("message")
        private String message;
        @SerializedName("reason")
        private String reason;
    }

    @NoArgsConstructor
    @Data
    public static class CauseDTO {
        @SerializedName("location")
        private String location;
        @SerializedName("trigger")
        private String trigger;
    }
}
