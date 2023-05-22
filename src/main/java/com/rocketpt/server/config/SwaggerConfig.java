
package com.rocketpt.server.config;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/***
 *  Swagger配置
 */
//@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Rocket PT",
//        description = "A Flexible and Efficient private tracker",
//        version = "v1"))
@Configuration
public class SwaggerConfig {
    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0, 100));
                    tag.setExtensions(map);
                });
            }
            if (openApi.getPaths() != null) {
                openApi.addExtension("x-test123", "333");
                openApi.getPaths().addExtension("x-abb", RandomUtil.randomInt(1, 100));
            }

        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rocket PT API")
                        .version("1.0")
                        .summary("Rocket private tracker")
                        .description("Flexible and Efficient private tracker")
                        .termsOfService("https://github.com/plexpt/rocket-pt")
                        .license(new License().name("Apache 2.0")
                                .url("https://github.com/plexpt/rocket-pt")));
    }


}
