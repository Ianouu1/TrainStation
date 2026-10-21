package altn72.tpfilrouge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité (F1, RG11).
 *
 * <p>Authentification par formulaire, mots de passe vérifiés avec bcrypt, et
 * autorisations appliquées côté serveur : le tableau de bord et les actions
 * sensibles (affectation, résolution, évaluation, modification) sont réservés au
 * responsable d'exploitation ; le reste nécessite d'être authentifié. Masquer un
 * bouton ne suffit pas : la restriction vit ici.</p>
 */
@Configuration
public class ConfigurationSecurite {

    private static final String RESPONSABLE = "RESPONSABLE_EXPLOITATION";

    @Bean
    public PasswordEncoder encodeurMotDePasse() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain chaineFiltresSecurite(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Ressources publiques et documentation de l'API.
                        .requestMatchers("/connexion", "/css/**", "/js/**", "/webjars/**", "/favicon.ico").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Tableau de bord de supervision : responsable d'exploitation uniquement (F11).
                        .requestMatchers("/supervision/**").hasRole(RESPONSABLE)
                        // Actions sensibles sur les incidents : responsable d'exploitation (F6, F7, F8, F10).
                        .requestMatchers(HttpMethod.POST,
                                "/incidents/*/affectation",
                                "/incidents/*/resolution",
                                "/incidents/*/description",
                                "/incidents/*/evaluation").hasRole(RESPONSABLE)
                        .requestMatchers(HttpMethod.GET, "/incidents/*/evaluation").hasRole(RESPONSABLE)
                        // Tout le reste exige une session authentifiée.
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/connexion")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/connexion?erreur")
                        .permitAll())
                .logout(out -> out
                        .logoutUrl("/deconnexion")
                        .logoutSuccessUrl("/connexion?deconnecte")
                        .permitAll())
                // L'API REST est sans état : la protection CSRF (utile aux formulaires) y est désactivée.
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
        return http.build();
    }
}
