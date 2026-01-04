package io.github.chenyilei2016.amznadclient.model.spv3;

import com.google.gson.annotations.Expose;
import io.github.chenyilei2016.amznadclient.kernel.validate.EnumValue;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chenyilei
 * @date 2023/04/17 10:57
 */
@Data
public class AmznCommonQueryTermMatchTypeFilter {
    @Expose
    @EnumValue(enumClass = AmznCommonQueryTermMatchTypeEnum.class)
    @NotNull
    private String queryTermMatchType;

    @Expose
    @NotEmpty
    private List<String> include;

    public AmznCommonQueryTermMatchTypeFilter() {
    }

    public AmznCommonQueryTermMatchTypeFilter(String queryTermMatchType, List<String> include) {
        this.queryTermMatchType = queryTermMatchType;
        this.include = include;
    }
}
