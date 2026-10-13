package altn72.tpfilrouge.modele;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Accès aux utilisateurs.
 */
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    /** Recherche par login, support de l'authentification (F1). Requête dérivée. */
    Optional<Utilisateur> findByLogin(String login);
}
