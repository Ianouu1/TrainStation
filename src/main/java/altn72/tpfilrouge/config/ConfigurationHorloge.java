package altn72.tpfilrouge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Fournit une horloge ({@link java.time.Clock}) injectable dans l'application.
 *
 * <p>Les services qui calculent des durées (ex. l'indicateur d'urgence RG3)
 * dépendent de ce bean plutôt que d'appeler {@code LocalDateTime.now()} : en test,
 * l'horloge peut être figée pour vérifier précisément les seuils (astuce §7.8).</p>
 */
@Configuration
public class ConfigurationHorloge {

    @Bean
    public Clock horloge() {
        return Clock.systemDefaultZone();
    }
}
