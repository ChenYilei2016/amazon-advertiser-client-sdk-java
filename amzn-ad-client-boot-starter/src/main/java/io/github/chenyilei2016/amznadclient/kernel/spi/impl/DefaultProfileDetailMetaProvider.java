package io.github.chenyilei2016.amznadclient.kernel.spi.impl;

import io.github.chenyilei2016.amznadclient.kernel.core.ProfileDetailMetaResponse;
import io.github.chenyilei2016.amznadclient.kernel.exceptions.AmznApiException;
import io.github.chenyilei2016.amznadclient.kernel.spi.IProfileDetailMetaProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * Profile详情数据提供者默认实现
 *
 * <p>这是一个空实现,抛出异常提示用户需要提供自己的实现。
 * 外部系统应该通过Spring依赖注入替换此默认实现。
 *
 * <p>使用示例:
 * <pre>{@code
 * @Component
 * public class MyProfileDetailMetaProvider implements IProfileDetailMetaProvider {
 *
 *     @Autowired
 *     private ProfileRepository profileRepository;
 *
 *     @Override
 *     public ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId) {
 *         // 从数据库查询
 *         ProfileEntity entity = profileRepository.findByProfileId(profileId);
 *
 *         ProfileDetailMetaResponse response = new ProfileDetailMetaResponse();
 *         response.setProfileId(entity.getProfileId());
 *         response.setAdvRefreshToken(entity.getRefreshToken());
 *         response.setCountryCode(entity.getCountryCode());
 *         response.setEndpointUrl(entity.getEndpointUrl());
 *         response.setProfileType(entity.getProfileType());
 *         return response;
 *     }
 * }
 * }</pre>
 *
 * @author chenyilei
 * @see IProfileDetailMetaProvider
 * @since 2025/12/31
 */
@Slf4j
public class DefaultProfileDetailMetaProvider implements IProfileDetailMetaProvider, InitializingBean {

    @Override
    public ProfileDetailMetaResponse getProfileDetailMetaByProfileId(String profileId) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        throw AmznApiException.createBizException("未提供 IProfileDetailMetaProvider 实现! 请实现IAuthCredentialsProvider接口并注入到Spring容器中");
    }

}
