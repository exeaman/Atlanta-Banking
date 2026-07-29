package com.atlanta.banking.audit.service.config.openapi;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "Bearer Authentication";

    @Bean
    public OpenAPI auditServiceOpenAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("Atlanta Banking - Audit Service API")
                        .version("v1")
                        .description("""
                                Audit Service provides centralized audit logging for all Atlanta Banking microservices.
                                
                                It records immutable audit events and provides searchable, paginated access
                                to historical audit records for operational monitoring, compliance and investigation.
                                """)
                        .contact(new Contact()
                                .name("Atlanta Banking")
                                .email("support@atlanta-banking.com"))
                        .license(new License()
                                .name("Internal Use Only")))

                .servers(List.of(
                        new Server()
                                .url("http://localhost:8088")
                                .description("Local Development")
                ))

                .externalDocs(new ExternalDocumentation()
                        .description("Atlanta Banking Documentation"))

                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME))

                .schemaRequirement(
                        SECURITY_SCHEME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }
}