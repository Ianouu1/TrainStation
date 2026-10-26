package altn72.tpfilrouge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Métadonnées de la documentation OpenAPI / Swagger de l'API REST (F12).
 * Interface disponible sur {@code /swagger-ui.html}.
 */
@Configuration
public class ConfigurationOpenApi {

    @Bean
    public OpenAPI decrireApi() {
        return new OpenAPI().info(new Info()
                .title("API TrainStation")
                .description("Supervision d'équipements et suivi d'incidents, API REST des incidents.")
                .version("v1"));
    }
}
