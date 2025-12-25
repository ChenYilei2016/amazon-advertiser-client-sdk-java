package io.github.chenyilei2016.myclient.model.spv3;

import io.github.chenyilei2016.myclient.kernel.core.BaseEnum;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author chenyilei
 * @date 2023/04/13 17:18
 */
@Getter
public enum AmznSpProductTargetExpressionTypeEnum implements BaseEnum {

    /**
     * 手动投放
     */
    asinExpandedFrom("ASIN_EXPANDED_FROM", "asin", AmznCommonExpressionTypeEnum.MANUAL, "asinExpandedFrom", "asin"),
    asinSameAs("ASIN_SAME_AS", "asin", AmznCommonExpressionTypeEnum.MANUAL, "asinSameAs", "asin"),
    asinCategorySameAs("ASIN_CATEGORY_SAME_AS", "category", AmznCommonExpressionTypeEnum.MANUAL, "asinCategorySameAs", "类目"),
    asinBrandSameAs("ASIN_BRAND_SAME_AS", "Target the brand that is the same as the brand expressed.", AmznCommonExpressionTypeEnum.MANUAL, "asinBrandSameAs", "品牌"),


    /**
     * 自动投放
     */
    queryHighRelMatches("QUERY_HIGH_REL_MATCHES", "紧密匹配", AmznCommonExpressionTypeEnum.AUTO, "queryHighRelMatches", "紧密匹配"),
    queryBroadRelMatches("QUERY_BROAD_REL_MATCHES", "宽泛匹配", AmznCommonExpressionTypeEnum.AUTO, "queryBroadRelMatches", "宽泛匹配"),
    asinAccessoryRelated("ASIN_ACCESSORY_RELATED", "关联商品", AmznCommonExpressionTypeEnum.AUTO, "asinAccessoryRelated", "关联商品"),
    asinSubstituteRelated("ASIN_SUBSTITUTE_RELATED", "同类商品", AmznCommonExpressionTypeEnum.AUTO, "asinSubstituteRelated", "同类商品"),

    /**
     * 关键词组
     **/

    keywordGroupSameAs("KEYWORD_GROUP_SAME_AS", "关键词组相关", AmznCommonExpressionTypeEnum.MANUAL, "KEYWORD_GROUP_SAME_AS", "关键词组相关"),

    ///////////// other
    asinPriceLessThan("ASIN_PRICE_LESS_THAN", "价格小于", null, "asinPriceLessThan", "价格小于"),
    asinPriceBetween("ASIN_PRICE_BETWEEN", "价格区间", null, "asinPriceBetween", "价格区间"),
    asinPriceGreaterThan("ASIN_PRICE_GREATER_THAN", "价格大于", null, "asinPriceGreaterThan", "价格大于"),
    asinReviewRatingLessThan("ASIN_REVIEW_RATING_LESS_THAN", "星级小于", null, "asinReviewRatingLessThan", "星级小于"),
    asinReviewRatingBetween("ASIN_REVIEW_RATING_BETWEEN", "星级区间", null, "asinReviewRatingBetween", "星级区间"),
    asinReviewRatingGreaterThan("ASIN_REVIEW_RATING_GREATER_THAN", "星级大于", null, "asinReviewRatingGreaterThan", "星级大于"),
    //Target an age range that is in the expressed range. This refinement can be applied for toys and games categories only.
    asinAgeRangeSameAs("ASIN_AGE_RANGE_SAME_AS", "年龄范围", null, "asinAgeRangeSameAs", "年龄范围"),
    //Target products related to the expressed genre. This refinement can be applied for Books and eBooks categories only.
    asinGenreSameAs("ASIN_GENRE_SAME_AS", "类型相关", null, "asinGenreSameAs", "类型相关"),
    asinIsPrimeShippingEligible("ASIN_IS_PRIME_SHIPPING_ELIGIBLE", "会员配送", null, "asinIsPrimeShippingEligible", "会员配送"),
    queryExactMatches("QUERY_EXACT_MATCHES", "", null, "queryExactMatches", ""),
    queryPhraseMatches("QUERY_PHRASE_MATCHES", "", null, "queryPhraseMatches", ""),
    queryBroadMatches("QUERY_BROAD_MATCHES", "", null, "queryBroadMatches", ""),

    ;


    private final String code;

    private final String desc;

    private final AmznCommonExpressionTypeEnum expressionTypeEnum;

    private final String oldCode;

    private final String translate;

    AmznSpProductTargetExpressionTypeEnum(String code, String desc, AmznCommonExpressionTypeEnum expressionTypeEnum, String oldCode, String translate) {
        this.code = code;
        this.desc = desc;
        this.expressionTypeEnum = expressionTypeEnum;
        this.oldCode = oldCode;
        this.translate = translate;
    }


    public static AmznSpProductTargetExpressionTypeEnum getEnumFromCode(String code) {
        for (AmznSpProductTargetExpressionTypeEnum type : AmznSpProductTargetExpressionTypeEnum.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static String getTranslateFromCode(String code) {
        return Arrays.stream(AmznSpProductTargetExpressionTypeEnum.values())
                .filter(item -> item.getType().equalsIgnoreCase(code))
                .map(AmznSpProductTargetExpressionTypeEnum::getTranslate)
                .findFirst().orElse(null);
    }

    public static AmznSpProductTargetExpressionTypeEnum getEnumFromOldCode(String code) {
        for (AmznSpProductTargetExpressionTypeEnum type : AmznSpProductTargetExpressionTypeEnum.values()) {
            if (type.oldCode.equals(code)) {
                return type;
            }
        }
        return null;
    }

    public String getType() {
        return this.getCode();
    }

    public static String findDescByCode(String code) {
        return Arrays.stream(values()).filter(e -> e.getCode().equals(code))
                .map(AmznSpProductTargetExpressionTypeEnum::getTranslate).filter(Objects::nonNull).findFirst().orElse(null);
    }

    @Getter
    public enum AmznSpCampaignTargetTypeEnum implements BaseEnum {

        AUTO, MANUAL;

        @Override
        public String getCode() {
            return this.name();
        }
    }

    /**
     * 获取自动广告投放类型
     *
     * @return 自动广告投放类型
     */
    public static List<String> getAmznSpProductTargetAutoCodeList() {
        return new ArrayList<String>() {{
            add(queryHighRelMatches.getCode());
            add(queryBroadRelMatches.getCode());
            add(asinAccessoryRelated.getCode());
            add(asinSubstituteRelated.getCode());
        }};
    }

    /**
     * 获取手动广告投放品类型
     *
     * @return 手动广告广告投放类型
     */
    public static List<String> getAmznSpProductTargetManualCodeList() {
        return new ArrayList<String>() {{
            add(asinSameAs.getDesc());
            add(asinCategorySameAs.getDesc());
        }};
    }

    /**
     * 获取除手动以外的广告投放品类型
     *
     * @return 自动广告投放类型
     */
    public static List<String> getAmznSpProductTargetAutoAndKeywordGroupList() {
        return new ArrayList<String>() {{
            add(queryHighRelMatches.getCode());
            add(queryBroadRelMatches.getCode());
            add(asinAccessoryRelated.getCode());
            add(asinSubstituteRelated.getCode());
            add(keywordGroupSameAs.getCode());
        }};
    }
}