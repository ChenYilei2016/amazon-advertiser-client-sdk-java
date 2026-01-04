package io.github.chenyilei2016.amznadclient;

import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author chenyilei
 * @since 2025/12/31 13:43
 */
public class SystemPropGet {

    static StandardEnvironment environment = new StandardEnvironment();

    public static String refreshToken(){
        return environment.getProperty("amzn.ad.refresh.token");
    }

    public static String clientId(){
        return environment.getProperty("amzn.ad.client.id");
    }

    public static String clientSecret(){
        return environment.getProperty("amzn.ad.client.secret");
    }
}
