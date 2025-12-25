package io.github.chenyilei2016.myclient.context;


import io.github.chenyilei2016.myclient.support.SpecialClientDetail;

/**
 * @author chenyilei
 * @since 2024/05/22 15:47
 */
public class AmznAdClientHelper {

    protected static final ThreadLocal<SpecialClientDetail> LOCAL_CLIENT_IDENTITY = new ThreadLocal<>();

    public static SpecialClientDetail getClientIdentity() {
        return LOCAL_CLIENT_IDENTITY.get();
    }

    public static void clearClientIdentity() {
        LOCAL_CLIENT_IDENTITY.remove();
    }

    public static void setClientIdentity(SpecialClientDetail specialClientDetail) {
        LOCAL_CLIENT_IDENTITY.set(specialClientDetail);
    }
}
