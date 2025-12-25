package io.github.chenyilei2016.myclient.model.spv3;

import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;
import lombok.Getter;

@Getter
public enum BiddingShopperCohortTypeEnum implements BaseEnum {

    AUDIENCE_SEGMENT;


    @Override
    public String getCode() {
        return this.name();
    }
}
