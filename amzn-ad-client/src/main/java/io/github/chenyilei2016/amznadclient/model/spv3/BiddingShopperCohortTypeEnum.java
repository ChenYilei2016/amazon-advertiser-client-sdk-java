package io.github.chenyilei2016.amznadclient.model.spv3;

import io.github.chenyilei2016.amznadclient.kernel.core.BaseEnum;
import lombok.Getter;

@Getter
public enum BiddingShopperCohortTypeEnum implements BaseEnum {

    AUDIENCE_SEGMENT;


    @Override
    public String getCode() {
        return this.name();
    }
}
