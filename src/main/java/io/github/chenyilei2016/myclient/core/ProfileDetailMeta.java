package io.github.chenyilei2016.myclient.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 目前是 amzn client token 信息缓存
 *
 * @author chenyilei
 * @date 2023/09/11 15:57
 */
@Data
public class ProfileDetailMeta implements Serializable {


    private String profileId;

    //新广告授权的店铺都应该有此值
    private String advRefreshToken;

    private String countryCode;

    private String endpointUrl;

    /**
     * SC VC
     */
    private String profileType;



}
