package io.github.chenyilei2016.myclient.model.spv3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.gson.GsonListIncludeSerializer;
import io.github.chenyilei2016.myclient.kernel.validate.EnumValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author chenyilei
 * @date 2023/04/11 14:13
 */

@Data
@NoArgsConstructor
public class AmznSpCampaignListRequest {
    @NotEmpty
    private String profileId;

    @Expose
    @SerializedName("campaignIdFilter")
    @JsonAdapter(GsonListIncludeSerializer.class)
    private List<String> campaignIdFilter;


    @Expose
    @SerializedName("portfolioIdFilter")
    @JsonAdapter(GsonListIncludeSerializer.class)
    private List<String> portfolioIdFilter;

    @Expose
    @SerializedName("stateFilter")
    @JsonAdapter(GsonListIncludeSerializer.class)
    @EnumValue(enumClass = AmznCommonEntityStateEnum.class)
    private List<String> stateFilter;

    @Expose
    @SerializedName("maxResults")
    private Integer maxResults;

    @Expose
    @SerializedName("nextToken")
    private String nextToken;

    @Expose
    @SerializedName("includeExtendedDataFields")
    private Boolean includeExtendedDataFields = true;

    @Expose
    @Valid
    @SerializedName("nameFilter")
    private AmznCommonQueryTermMatchTypeFilter nameFilter;

    public AmznSpCampaignListRequest(String profileId) {
        this.profileId = profileId;
    }
}
