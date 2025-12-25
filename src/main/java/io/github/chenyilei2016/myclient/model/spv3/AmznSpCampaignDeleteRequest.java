package io.github.chenyilei2016.myclient.model.spv3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.gson.GsonListIncludeSerializer;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author chenyilei
 * @date 2023/04/21 17:42
 */
@Data
public class AmznSpCampaignDeleteRequest {

    @NotEmpty
    private String profileId;

    @Expose
    @Size(min = 1, max = 1000)
    @SerializedName("campaignIdFilter")
    @JsonAdapter(GsonListIncludeSerializer.class)
    private List<String> campaignIdFilter;

}
