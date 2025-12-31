package io.github.chenyilei2016.amznadclient;

/**
 * @author chenyilei
 * @since 2025/12/31 13:43
 */
public class SystemPropGet {

    public static String refreshToken(){
        return System.getProperty("amzn.ad.refresh.token");
    }

    public static String clientId(){
        return System.getProperty("amzn.ad.client.id");
    }

    public static String clientSecret(){
        return System.getProperty("amzn.ad.client.secret");
    }
}
