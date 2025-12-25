package io.github.chenyilei2016.myclient.kernel.errors;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @author chenyilei
 * @date 2023/04/14 11:18
 */
@Data
@NoArgsConstructor
@FieldNameConstants
public class AmznDefaultErrorResponse implements AmznCommonResponse {

    @SerializedName(value = "code")
    protected String code;

    @SerializedName(value = "details", alternate = {"detail", "description"})
    protected String detail;

    @SerializedName(value = "id", alternate = {"targetId"})
    protected String id;

    @SerializedName(value = "index", alternate = {"targetRequestIndex"})
    protected Integer index;

    @SerializedName(value = "reason")
    protected String reason;

    public AmznDefaultErrorResponse(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public AmznDefaultErrorResponse(String id, String code, String detail) {
        this.code = code;
        this.detail = detail;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return detail;
    }
}
