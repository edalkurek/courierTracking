package migros.one.courierTracking.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Courier Tracker API")
                        .version("1.0.0")
                        .description("Kurye takip sistemi")
                        .contact(new Contact()
                                .name("Eren Dalkürek")
                                .email("edalkurek@hotmail.com")
                        ));
    }
}
