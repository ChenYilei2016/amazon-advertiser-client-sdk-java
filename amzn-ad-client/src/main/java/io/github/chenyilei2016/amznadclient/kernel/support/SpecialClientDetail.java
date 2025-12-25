package io.github.chenyilei2016.amznadclient.kernel.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenyilei
 * @date 2023/09/12 11:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialClientDetail {
    private String clientId;

    private String clientSecret;

    private String refreshToken;

    /////

    private transient String specialAccountType;
}
