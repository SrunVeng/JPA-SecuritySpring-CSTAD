package com.example.mbaningapijpapractice.security;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${Mbanking_Api_prodHttp}")
    private String mbankingProdHttp;

    @Value("${Mbanking_Api_stageHttp}")
    private String mbankingstageHttp;

    @Value("${Mbanking_Api_UatHttp}")
    private String mbankingUatHttp;

    @Value("${Mbanking_Api_devHttp}")
    private String mbankingDevHttp;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {


        Server prodUrl = new Server();
        prodUrl.setUrl(mbankingProdHttp);
        prodUrl.setDescription("Server URL in Prod environment");

        Server stageUrl = new Server();
        stageUrl.setUrl(mbankingstageHttp);
        stageUrl.setDescription("Server URL in Stage environment");

        Server UatUrl = new Server();
        UatUrl.setUrl(mbankingUatHttp);
        UatUrl.setDescription("Server URL in UAT environment");
        Server devUrl = new Server();
        devUrl.setUrl(mbankingDevHttp);
        devUrl.setDescription("Server URL in Development environment");
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("Veng Srun")
                        .description("CSTAD MobileBanking API")
                        .version("1.2")
                        .contact(new Contact()
                                .name("MBanking")
                                .email("srunveng2023@gmail.com")
                                .url("https://github.com/SrunVeng?tab=repositories"))
                        .license(new License()
                                .name("License of API")
                                .url("API license URL")))
                .servers(List.of(prodUrl, stageUrl, UatUrl, devUrl));
    }
}
