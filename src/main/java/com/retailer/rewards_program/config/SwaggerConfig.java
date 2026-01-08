package com.retailer.rewards_program.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * Access swagger: http://localhost:8080/swagger-ui.html
     */
    public OpenAPI rewardsApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Rewards API")
                        .description("API for calculating customer reward points")
                        .version("1.0.0"));
    }
}
