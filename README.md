


# api based
generate jdk 17+ 
生成by  openapi-generator-cli-7.12.0.jar


# 生成路径

```shell

cd amazon-advertiser-client-sdk-java ;

java -jar ./openapi-cli/openapi-generator-cli-7.12.0.jar generate \
   -i https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AMCAdministration_prod_3p.json \
   -g java \
   -o ./ \
  --invoker-package "io.github.chenyilei2016.adv" \
  --api-package "io.github.chenyilei2016.adv.api.amcAdmin" \
  --model-package "io.github.chenyilei2016.adv.model.amcAdmin" \
  --group-id "io.github.chenyilei2016" \
  --artifact-id "amazon-advertiser-client-sdk-java" \
  --artifact-version "1.0.0-SNAPSHOT" \
  --additional-properties=useBeanValidation=true,performBeanValidation=true,useJakartaEe=false

```

# 目的
亚马逊生成的api有问题, 借助AI 转换成适合的client


# API 


## AMC 
https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/AMCAdministration_prod_3p.json
https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/Rule-BasedAudiences_prod_3p.json


### rule based api
https://advertising.amazon.com/API/docs/en-us/amc-rba
* https://d1y2lf8k3vrkfu.cloudfront.net/openapi/en-us/dest/Rule-BasedAudiences_prod_3p.json