package altn72.tpfilrouge.securite;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Garantit que le haché bcrypt des comptes de démo (db/create.sql) correspond
 * bien au mot de passe documenté dans le journal, pour que la connexion (F1)
 * fonctionne lors d'une démonstration.
 */
class MotDePasseDemoTest {

    private static final String HACHE_DEMO =
            "$2a$10$i8MunbkeNsgJaBOMK0DcAeG/4gFgTCcI0NYbc32gOohCmpowhKKRm";

    @Test
    void hacheDeDemonstrationCorrespondAuMotDePasse() {
        assertThat(new BCryptPasswordEncoder().matches("password", HACHE_DEMO)).isTrue();
    }
}
