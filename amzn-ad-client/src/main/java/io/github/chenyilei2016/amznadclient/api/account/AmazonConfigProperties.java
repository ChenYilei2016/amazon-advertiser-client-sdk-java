package io.github.chenyilei2016.amznadclient.api.account;

import lombok.Data;

import java.util.Map;

/**
 * @author chenyilei
 * @since 2025/12/31 09:48
 */
public interface AmazonConfigProperties {

    AmazonAccountConfigBO getAccountDetail(String accountType);


    @Data
    public static class AmazonAccountConfigBO {

        /**
         * 广告客户端编号
         */
        private String advClientId;

        /**
         * 广告客户端密钥
         */
        private String advClientSecret;

    }

}
