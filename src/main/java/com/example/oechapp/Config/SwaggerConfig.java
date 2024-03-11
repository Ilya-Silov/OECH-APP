package com.example.oechapp.Config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
public class SwaggerConfig {


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi()
    {
        return new OpenAPI()
                .info(new Info().title("OECH API").version("1.0")
                        .description("<h2>Авторизация</h2><p>Для авторизации в API используется JWT token.</p> <p><strong>Инструкция по авторизации методом API: </strong><br> 1) отправьте запрос по адресу /api/auth/login <br> 2) добавьте в заголовки запроса заголовок Authorization: Bearer 'token'.</p> <p><strong>Инструкция по авторизации методом OAuth Google: </strong><br> 1) реализуйте в приложении авторизацию через Google и сохраните id_token <br> 2) добавьте в заголовки запроса заголовок Authorization: Google 'token'</p> <h2>Расчёт стоимости посылки</h2> <p><strong>Instant Delivery</strong> = 500</p> <p>При создании посылки сумма сама списывается с баланса</p>")
                )

                //.addSecurityItem(new SecurityRequirement().addList("JWT Token"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "JWT Token", new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                )
                );
    }

}
