package io.github.chenyilei2016.myclient.kernel.utils;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author chenyilei
 * @since 2024/08/13 11:08
 */
public class TrustSSLConstant {

    private static SSLContext trustSslContext = null;
    private static X509TrustManager X509TrustManager = null;
    private static HostnameVerifier ignoreHostnameVerifier = null;

    static {
        try {
            trustSslContext = SSLContext.getInstance("SSL", "SunJSSE");
            X509TrustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            trustSslContext.init(null, new TrustManager[]{X509TrustManager}, new java.security.SecureRandom());
            ignoreHostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslsession) {
                    return true;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static SSLContext getTrustSSLContext() {
        return trustSslContext;
    }

    public static X509TrustManager getTrustX509TrustManager() {
        return X509TrustManager;
    }


    public static HostnameVerifier getIgnoreHostnameVerifier() {
        return ignoreHostnameVerifier;
    }
}
