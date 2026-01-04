package io.github.chenyilei2016.amznadclient.model.spv3;


import io.github.chenyilei2016.amznadclient.kernel.core.BaseEnum;
import lombok.Getter;

@Getter
public enum BiddingAudienceSegmentTypeEnum implements BaseEnum {

    SPONSORED_ADS_AMC, BEHAVIOR_DYNAMIC;

    @Override
    public String getCode() {
        return this.name();
    }
}
