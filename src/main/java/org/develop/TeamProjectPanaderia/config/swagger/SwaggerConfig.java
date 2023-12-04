package org.develop.TeamProjectPanaderia.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfig {

    @Value("${api.version}")
    private String apiVersion;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Panaderia Pepitos Spring")
                                .version("1.0.0")
                                .description("Somos los que te calientan el bollo")
                                .termsOfService("https://github.com/Alonso2002-jpg/TeamProjectPanaderia-2DAW")
                                .license(
                                        new License()
                                                .name("CC BY-NC-SA 4.0")
                                                .url("https://github.com/Alonso2002-jpg/TeamProjectPanaderia-2DAW")
                                )
                                .contact(
                                        new Contact()
                                                .name("Capitalistas Salvajes, Panaderia Pepitos")
                                                .email("givemeyourmoney@gmail.com")
                                                .url("https://github.com/Alonso2002-jpg/TeamProjectPanaderia-2DAW")
                                )

                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Documentación del Proyecto")
                                .url("https://github.com/Alonso2002-jpg/TeamProjectPanaderia-2DAW/blob/main/README.md")
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("GitHub del Proyecto")
                                .url("https://github.com/Alonso2002-jpg/TeamProjectPanaderia-2DAW")
                )
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }


    @Bean
    GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("https")
                .pathsToMatch("/" + apiVersion + "/producto/**", "/" + apiVersion + "/categoria/**", "/" + apiVersion + "/auth/**", "/" + apiVersion + "/personal/**", "/" + apiVersion + "/proveedores/**", "/" + apiVersion + "/pedidos/**", "/" + apiVersion + "/cliente/**",  "/" + apiVersion + "/users/**")
                .displayName("Panaderia Pepitos Swagger")
                .build();
    }
}