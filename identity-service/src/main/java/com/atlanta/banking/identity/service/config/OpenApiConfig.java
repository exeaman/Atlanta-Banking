package com.atlanta.banking.identity.service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Atlanta Banking Identity Service API", version = "v1.0.0", description = """
        Identity Service responsible for employee authentication,
        authorization and employee identity management.
        
        Features:
        • JWT Authentication
        • Employee Management
        • Password Management
        • Role Based Access Control (RBAC)
        • Method Level Security
        """, contact = @Contact(name = "Atlanta Banking Backend Team", email = "backend@atlantabank.com"), license = @License(name = "Internal Use Only")), servers = {@Server(description = "Local Development", url = "http://localhost:8083")})
@SecurityScheme(name = "bearerAuth", description = "JWT Bearer Authentication", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}