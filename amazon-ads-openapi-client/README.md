# Amazon Ads OpenAPI Client

基于 Amazon Ads 官方 OpenAPI 生成的 JDK 17 二方包。当前已包含 Ads v1 的 Campaigns 与 Targets 两组接口，各包含创建、查询、更新和删除操作。

该模块只提供传输模型与 HTTP 调用能力；LwA 授权、profile、区域选择、限流、重试和业务错误处理由调用方实现。

## 生成输入

- Campaigns 官方来源：`https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLMerged_prod_3p.json`
- Targets 官方来源：`https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLTargetsContract_prod_3p.json`
- 冻结来源规格：`spec/AmazonAdsAPIALLMerged_prod_3p.json`、`spec/AmazonAdsAPIALLTargetsContract_prod_3p.json`
- 裁剪规格与校验信息：`spec/campaigns.openapi.json`、`spec/targets.openapi.json`、`spec/*.source.properties`
- 生成器：OpenAPI Generator 7.24.0

生成代码位于标准 Maven 主源码目录 `src/main/java` 并提交到 Git，以便规格和生成器升级时直接审阅差异。`.openapi-generator` 仅是生成器状态，不纳入 Git。该模块不放人工实现；执行 Maven `generate-sources` 时会完整重建 `src/main`，不允许手工修改。

`Target.targetDetails` 在该 SDK 中保持为强类型 `TargetDetails`。`TargetDetails` 与 `CreateTargetDetails` 的 27 个 oneOf 分支被规范化为可空的强类型字段，以避免生成器为同名的请求/响应分支产生额外包装类；未知字段仍会保留。调用方应依据 `targetType` 读取对应字段，例如 `PRODUCT` 对应 `getProductTarget()`。

## 更新规格

```bash
curl --fail --location --output spec/AmazonAdsAPIALLMerged_prod_3p.json \
  https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLMerged_prod_3p.json
curl --fail --location --output spec/AmazonAdsAPIALLTargetsContract_prod_3p.json \
  https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AmazonAdsAPIALLTargetsContract_prod_3p.json
node scripts/prepare-spec.mjs
node scripts/verify-spec.mjs
mvn test
```

## 验证

模块以 Java 17 作为编译基线；应在 JDK 17 与 JDK 25 环境分别执行上述命令。

## 按组更新与生成

新增或更新单个 API 组时，可只处理该组；例如 Targets：

```bash
node scripts/prepare-spec.mjs --groups=targets
node scripts/verify-spec.mjs --groups=targets
node scripts/generate-groups.mjs --groups=targets
mvn -Damazon.ads.openapi.skip-generation=true test
```

`--groups` 支持逗号分隔，例如 `--groups=campaigns,targets`。省略该参数时，所有已登记 API 组都会完整重建。单组生成只改动该组的 API 与模型代码，保留其他组；Campaigns 还负责生成共享 `ApiClient`。当升级 OpenAPI Generator、调整共享 Client 配置，或官方契约删除模型/操作时，必须省略 `--groups` 执行全量重建。

新增 API 组时，在 `scripts/openapi-groups.mjs` 登记其来源、Tag、操作和 Maven execution，并在 `pom.xml` 增加对应的生成 execution；随后使用该组的 `--groups` 参数生成即可。

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
