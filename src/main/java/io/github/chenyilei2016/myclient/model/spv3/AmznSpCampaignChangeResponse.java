package io.github.chenyilei2016.myclient.model.spv3;

import com.google.gson.annotations.SerializedName;
import io.github.chenyilei2016.myclient.kernel.advice.AmznClientCrudTypeEnum;
import io.github.chenyilei2016.myclient.kernel.advice.AmznClientResponseBeforeReturn;
import io.github.chenyilei2016.myclient.kernel.errors.AmznOriginHierarchyError;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chenyilei
 * @date 2023/04/14 11:03
 */
@NoArgsConstructor
@Data
public class AmznSpCampaignChangeResponse implements AmznClientResponseBeforeReturn {

    @SerializedName("campaigns")
    private CampaignsDTO campaigns;

    @Override
    public void beforeReturn(AmznClientCrudTypeEnum crudTypeEnum, Object... args) {
        if (AmznClientCrudTypeEnum.CREATE == crudTypeEnum ||
                AmznClientCrudTypeEnum.UPDATE == crudTypeEnum ||
                AmznClientCrudTypeEnum.DELETE == crudTypeEnum) {
            AmznCommonExtendedDataDTO extendedDataDTO = AmznCommonExtendedDataDTO.buildNowDTO();
            for (AmznSpCampaignListResponse.CampaignDTO successDTO : getCampaigns().getSuccessDTOS()) {
                //自带的一个补偿措施, 亚马逊没返回时间戳自己补一下
                if (successDTO.getExtendedData() == null) {
                    successDTO.setExtendedData(extendedDataDTO);
                }
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class CampaignsDTO {
        @SerializedName("error")
        private List<AmznOriginHierarchyError> error;

        @SerializedName("success")
        private List<SuccessDTO> success;

        public List<AmznSpCampaignListResponse.CampaignDTO> getSuccessDTOS() {
            if (success == null) {
                return Collections.emptyList();
            }
            return success.stream().map(SuccessDTO::getCampaign).filter(Objects::nonNull).collect(Collectors.toList());
        }

        public List<String> getSuccessIds() {
            if (success == null) {
                return Collections.emptyList();
            }
            return success.stream().map(SuccessDTO::getCampaignId).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }


    @Data
    public static class SuccessDTO implements AmznBaseIndexDTO {
        @SerializedName("campaign")
        private AmznSpCampaignListResponse.CampaignDTO campaign;

        @SerializedName("campaignId")
        private String campaignId;

        @SerializedName("index")
        private Integer index;

        public String getCampaignId() {
            if (campaignId != null) {
                return campaignId;
            }
            return campaign == null ? null : campaign.getCampaignId();
        }

        @Override
        public String getObjectId() {
            return getCampaignId();
        }
    }

}
