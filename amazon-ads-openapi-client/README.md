# Amazon Ads OpenAPI Client

基于 Amazon Ads 官方 OpenAPI 生成的 JDK 17 二方包。当前已包含 Ads v1 的 Campaigns 与 Targets 两组接口，各包含创建、查询、更新和删除操作。

该模块只提供传输模型与 HTTP 调用能力；LwA 授权、profile、区域选择、限流、重试和业务错误处理由调用方实现。

## 生成输入

- Campaigns 官方来源：`https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLMerged_prod_3p.json`
- Targets 官方来源：`https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLTargetsContract_prod_3p.json`
- 冻结来源规格：`spec/AmazonAdsAPIALLMerged_prod_3p.json`、`spec/AmazonAdsAPIALLTargetsContract_prod_3p.json`
- 裁剪规格与校验信息：`spec/campaigns.openapi.json`、`spec/targets.openapi.json`、`spec/*.source.properties`
- 生成器：OpenAPI Generator 7.24.0

生成代码位于标准 Maven 主源码目录 `src/main/java` 并提交到 Git，以便规格和生成器升级时直接审阅差异。该模块不放人工实现；执行 Maven `generate-sources` 时会完整重建 `src/main`，不允许手工修改。

## 更新规格

```bash
curl --fail --location --output spec/AmazonAdsAPIALLMerged_prod_3p.json \
  https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLMerged_prod_3p.json
curl --fail --location --output spec/AmazonAdsAPIALLTargetsContract_prod_3p.json \
  https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLTargetsContract_prod_3p.json
node scripts/prepare-spec.mjs
node scripts/verify-spec.mjs
```

## 验证

```bash
mvn -pl amazon-ads-openapi-client test
```

模块以 Java 17 作为编译基线；应在 JDK 17 与 JDK 25 环境分别执行上述命令。

## 手动 API 测试

手动测试统一复用 [`AmazonAdsManualTestSupport`](src/test/java/io/github/chenyilei2016/amazonads/adsv1/manual/AmazonAdsManualTestSupport.java)：它优先读取本地配置，再回退到 JVM 参数和环境变量，并负责执行开关、LwA Authorization、Client ID、可选 Account ID / API 地址以及超时。每个 API 只需新增一个以 `ManualIT` 结尾的测试类，并保留自己的强类型请求与断言；普通 `mvn test` 不会匹配这类测试。

先复制本地配置模板并填写凭证；`amazon-ads-manual-test.local.properties` 已被 Git 忽略，不会提交密钥。

```bash
cp src/test/resources/amazon-ads-manual-test.properties.example \
  src/test/resources/amazon-ads-manual-test.local.properties
```

当前示例为 [`TargetsApiManualIT`](src/test/java/io/github/chenyilei2016/amazonads/adsv1/targets/manual/TargetsApiManualIT.java)。配置中的 `amazon.ads.manual-test=true` 才允许它发起远程请求。

```bash
mvn -pl amazon-ads-openapi-client \
  -Dtest=TargetsApiManualIT \
  test
```

本地配置字段使用现有环境变量名称：`AMAZON_ADS_CLIENT_ID`、`AMAZON_ADS_ACCESS_TOKEN`、`AMAZON_ADS_PROFILE_ID`、`AMAZON_ADS_ACCOUNT_ID`、`AMAZON_ADS_API_BASE_URL`；也可配置 API 专用参数，例如 `amazon.ads.manual.ad-product`。`AMAZON_ADS_PROFILE_ID` 仅由需要 profile scope 的 API 测试调用 `context.requireProfileId()` 时要求。测试只打印响应数据摘要和业务数据，绝不打印 access token。
