/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Value("${swagger.server.profile}")
  private String profileUrl;

  @Value("${swagger.server.name}")
  private String profileName;

  @Bean
  public OpenAPI customOpenAPI() {
    Server server = new Server().url(profileUrl + contextPath).description(profileName + " Server");

    return new OpenAPI()
        .addServersItem(server)
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(
            new Info()
                .title("Refit API 명세서")
                .version("1.0")
                .description(
                    """
                    # 제 15 ICT 콤플렉스 피우다 프로젝트 - 다시입을Lab(Re:fit)

                    ## 주의사항
                    - 파일 업로드 크기 제한: 5MB (1개 파일 크기)

                    ## 문의
                    - 기술 문의: unijun0109@gmail.com, 1030n@naver.com
                    - 일반 문의: unijun0109@gmail.com, 1030n@naver.com
                    """));
  }

  @Bean
  public GroupedOpenApi apiGroup() {
    return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
  }
}
