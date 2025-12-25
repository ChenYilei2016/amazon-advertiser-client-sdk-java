package io.github.chenyilei2016.amznadclient.kernel.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RestTemplateUtil {

    //连接池中整体的空闲连接的最大数量
    private static Integer maxIdleConnections = 20;
    //连接空闲时间最多为 300 秒
    private static Long keepAliveDuration = 300L * 1000;


    private static final RestTemplate restTemplate;
    private static final RestTemplate lessConnectTimeRestTemplate;

    public static ConnectionPool pool() {
        return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS);
    }


    public static ClientHttpRequestFactory okHttpRequestFactory(int cTime, int rTime, int wTime) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectionPool(pool())
                .connectTimeout(cTime, TimeUnit.MILLISECONDS)
                .readTimeout(rTime, TimeUnit.MILLISECONDS)
                .writeTimeout(wTime, TimeUnit.MILLISECONDS)
                .build();
        return new OkHttp3ClientHttpRequestFactory(client);
    }

    static {
        restTemplate = new RestTemplate(okHttpRequestFactory(10 * 1000, 30 * 1000, 30 * 1000));
        converterUtf8(restTemplate);
        lessConnectTimeRestTemplate = new RestTemplate(okHttpRequestFactory(3000, 3000, 3000));
        converterUtf8(lessConnectTimeRestTemplate);


    }

    public static void converterUtf8(RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
                break;
            }
        }
    }

    public static RestTemplate getInstance() {
        return restTemplate;
    }

    public static RestTemplate getLessConnectTimeInstance() {
        return lessConnectTimeRestTemplate;
    }

    /**
     * 获取HttpHeaders
     * ContentType = application/x-www-form-urlencoded;charset=UTF-8
     *
     * @return
     */
    public static HttpHeaders getHttpHeadersOfFormUrlencoded() {
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/x-www-form-urlencoded;charset=UTF-8");
        httpHeaders.setContentType(mediaType);
        return httpHeaders;
    }


    /**
     * 获取HttpHeaders
     * ContentType = application/json;charset=UTF-8
     *
     * @return
     */
    public static HttpHeaders getHttpHeadersDefaultApplicationJsonUtf8(String mediaType, String mediaTypeAccept) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (mediaType != null) {
            MediaType mt = MediaType.parseMediaType(mediaType);
            httpHeaders.setContentType(mt);
            httpHeaders.setAccept(Lists.newArrayList(mt));
        } else {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        }

        if (mediaTypeAccept != null) {
            httpHeaders.setAccept(Lists.newArrayList(MediaType.valueOf(mediaTypeAccept)));
        }
        return httpHeaders;
    }

    /**
     * 获取HttpHeaders
     * ContentType = application/json;charset=UTF-8
     *
     * @return
     */
    public static HttpHeaders getHttpHeadersOfApplicationJsonUtf8() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return httpHeaders;
    }


//    /**
//     * 禁止 RestTemplate 自动重定向
//     *
//     * @return
//     */
//    public static RestTemplate getInstanceWithNoRedirect() {
//        return new RestTemplateBuilder()
//                .requestFactory(NoRedirectSimpleClientHttpRequestFactory.class)
//                .setConnectTimeout(Duration.ofMillis(connectTimeout))
//                .build();
////        return new RestTemplate(new NoRedirectSimpleClientHttpRequestFactory());
//    }

    /**
     * 禁止自动重定向
     */
    static class NoRedirectSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
        @Override
        protected void prepareConnection(HttpURLConnection connection,
                                         String httpMethod) throws IOException {
            super.prepareConnection(connection, httpMethod);
            connection.setInstanceFollowRedirects(false);
        }
    }


    public static <T> Set<T> addFirst(Collection<? extends T> collection, T toAdd) {
        LinkedHashSet<T> result = new LinkedHashSet<>();

        if (null != toAdd) {
            result.add(toAdd);
        }

        if (null != collection) {
            result.addAll(collection);
        }

        return Collections.unmodifiableSet(result);
    }
}
