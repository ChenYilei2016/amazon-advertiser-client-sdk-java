package io.github.chenyilei2016.myclient.model.spv3;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.gson.GsonUTCStrToTimestampDeserializer;
import lombok.Data;

/**
 * @author chenyilei
 * @date 2023/04/21 10:06
 */
@Data
public class AmznCommonExtendedDataDTO {
    @SerializedName(value = "lastUpdateDateTime", alternate = "lastUpdateDate")
    @JsonAdapter(GsonUTCStrToTimestampDeserializer.class)
    private Long lastUpdatedDate;

    @SerializedName(value = "creationDateTime", alternate = "creationDate")
    @JsonAdapter(GsonUTCStrToTimestampDeserializer.class)
    private Long creationDate;

    @SerializedName(value = "servingStatus", alternate = "serviceStatus")
    private String servingStatus;


    public static AmznCommonExtendedDataDTO buildNowDTO() {
        AmznCommonExtendedDataDTO amznCommonExtendedDataDTO = new AmznCommonExtendedDataDTO();
        long now = System.currentTimeMillis();
        amznCommonExtendedDataDTO.setCreationDate(now);
        amznCommonExtendedDataDTO.setLastUpdatedDate(now);
        return amznCommonExtendedDataDTO;
    }

}
