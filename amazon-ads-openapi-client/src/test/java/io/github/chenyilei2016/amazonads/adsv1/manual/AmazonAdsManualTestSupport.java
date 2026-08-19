package io.github.chenyilei2016.amazonads.adsv1.manual;

import io.github.chenyilei2016.amazonads.adsv1.client.ApiClient;
import org.junit.jupiter.api.Assumptions;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 手动 Amazon Ads 集成测试的公共上下文。
 *
 * <p>仅在显式传入 {@code -Damazon.ads.manual-test=true} 时允许发起远程请求。</p>
 */
public final class AmazonAdsManualTestSupport {

    private static final String MANUAL_TEST_ENABLED_PROPERTY = "amazon.ads.manual-test";
    private static final String TIMEOUT_SECONDS_PROPERTY = "amazon.ads.manual.timeout-seconds";
    private static final String LOCAL_CONFIG_RESOURCE = "amazon-ads-manual-test.local.properties";
    private static final int DEFAULT_TIMEOUT_SECONDS = 30;
    private static final Properties LOCAL_CONFIG = loadLocalConfig();

    private AmazonAdsManualTestSupport() {
    }

    public static Context requireContext() {
        Assumptions.assumeTrue(Boolean.parseBoolean(stringProperty(MANUAL_TEST_ENABLED_PROPERTY, "false")),
                "手动测试默认关闭。执行时添加 -D" + MANUAL_TEST_ENABLED_PROPERTY + "=true。");

        String accessToken = requiredValue("AMAZON_ADS_ACCESS_TOKEN");
        Duration timeout = timeout();
        ApiClient apiClient = new ApiClient()
                .setConnectTimeout(timeout)
                .setReadTimeout(timeout)
                .setRequestInterceptor(builder -> builder.header("Authorization", "Bearer " + accessToken));
        String baseUrl = stringProperty("AMAZON_ADS_API_BASE_URL", null);
        if (baseUrl != null) {
            apiClient.updateBaseUri(baseUrl);
        }
        return new Context(
                requiredValue("AMAZON_ADS_CLIENT_ID"),
                stringProperty("AMAZON_ADS_ACCOUNT_ID", null),
                stringProperty("AMAZON_ADS_PROFILE_ID", null),
                apiClient);
    }

    public static String stringProperty(String name, String defaultValue) {
        String value = resolveValue(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static int intProperty(String name, int defaultValue, int minimum, int maximum) {
        String value = stringProperty(name, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            int result = Integer.parseInt(value);
            assertTrue(result >= minimum && result <= maximum,
                    () -> name + " 必须介于 " + minimum + " 到 " + maximum + "。");
            return result;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(name + " 必须是整数。", exception);
        }
    }

    private static String requiredValue(String name) {
        String value = stringProperty(name, null);
        assertTrue(value != null && !value.isBlank(), () -> "缺少环境变量 " + name + "。");
        return value;
    }

    private static Properties loadLocalConfig() {
        Properties properties = new Properties();
        try (InputStream input = AmazonAdsManualTestSupport.class.getClassLoader()
                .getResourceAsStream(LOCAL_CONFIG_RESOURCE)) {
            if (input != null) {
                properties.load(input);
            }
            return properties;
        } catch (IOException exception) {
            throw new IllegalStateException("读取本地手动测试配置失败：" + LOCAL_CONFIG_RESOURCE, exception);
        }
    }

    private static String resolveValue(String name) {
        String localValue = LOCAL_CONFIG.getProperty(name);
        if (localValue != null && !localValue.isBlank()) {
            return localValue;
        }
        String systemValue = System.getProperty(name);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return System.getenv(name);
    }

    private static Duration timeout() {
        return Duration.ofSeconds(intProperty(TIMEOUT_SECONDS_PROPERTY, DEFAULT_TIMEOUT_SECONDS, 1, 300));
    }

    public record Context(String clientId, String accountId, String profileId, ApiClient apiClient) {

        public String requireProfileId() {
            assertTrue(profileId != null && !profileId.isBlank(), "缺少配置 AMAZON_ADS_PROFILE_ID。");
            return profileId;
        }
    }
}
