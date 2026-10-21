package altn72.tpfilrouge.service;

import altn72.tpfilrouge.modele.Utilisateur;
import altn72.tpfilrouge.modele.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Consultation des utilisateurs de l'application (en-tête F1).
 */
@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /** Utilisateur correspondant à un login, s'il existe. */
    public Optional<Utilisateur> trouverParLogin(String login) {
        return utilisateurRepository.findByLogin(login);
    }
}
