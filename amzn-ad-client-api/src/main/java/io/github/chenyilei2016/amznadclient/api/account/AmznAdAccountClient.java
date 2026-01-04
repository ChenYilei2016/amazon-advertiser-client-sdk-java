package io.github.chenyilei2016.amznadclient.api.account;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.gson.reflect.TypeToken;
import io.github.chenyilei2016.amznadclient.AmznAdClient;
import io.github.chenyilei2016.amznadclient.AmznBaseRequest;
import io.github.chenyilei2016.amznadclient.kernel.advice.AmznClientCrudTypeEnum;
import io.github.chenyilei2016.amznadclient.kernel.baserequest.endpoint.FixedEndpointProvider;
import io.github.chenyilei2016.amznadclient.kernel.baserequest.token.FixedCredentialsTokenProvider;
import io.github.chenyilei2016.amznadclient.model.account.AdAccountBudgetBO;
import io.github.chenyilei2016.amznadclient.model.common.AmznRegionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author SunYingLing
 * @description 店铺预算请求
 * @create 2023/3/3 1:40 下午
 *
 * https://advertising.amazon.com/API/docs/en-us/reference/2/profiles#tag/Profiles/operation/getProfileById
 */
@Slf4j
@Component
public class AmznAdAccountClient {

    private final AmznAdClient amznAdClient;

    public AmznAdAccountClient(AmznAdClient amznAdClient) {
        this.amznAdClient = amznAdClient;
    }


    public List<AdAccountBudgetBO> listAdAccount(String region, String refreshToken, String amazonAccountType) {
        try {
            ResponseEntity<String> response = listAdvAccount(region, refreshToken, amazonAccountType);
            if (response == null) {
                return Collections.emptyList();
            }
            return amznAdClient.getResultGson().fromJson(response.getBody(), new TypeToken<List<AdAccountBudgetBO>>() {
            }.getType());
        } catch (Exception e) {
            log.error("获取profile列表接口失败", e);
        }
        return Collections.emptyList();
    }

    public ResponseEntity<String> listAdvAccount(String region, String refreshToken, String amazonAccountType) {
        String endpointUrlPrefix = AmznRegionEnum.getEndpointUrlPrefix(region);
        if (CharSequenceUtil.isBlank(endpointUrlPrefix) || CharSequenceUtil.isBlank(refreshToken)) {
            return null;
        }


        AmznBaseRequest amznBaseRequest = AmznBaseRequest.builder()
                .url("/v2/profiles")
                .crudTypeEnum(AmznClientCrudTypeEnum.QUERY)
                .endpointProvider(new FixedEndpointProvider(endpointUrlPrefix))
                .tokenProvider(new FixedCredentialsTokenProvider(
                        amznAdClient.getAmznAdvConfigManager(),
                        amazonAccountType,//accountDetail.getAdvClientId(),
                        amazonAccountType,//accountDetail.getAdvClientSecret(),
                        refreshToken
                ));

        return amznAdClient.httpGetWithResponse(amznBaseRequest);
    }

}
