package altn72.tpfilrouge.service;

import altn72.tpfilrouge.modele.Utilisateur;
import altn72.tpfilrouge.modele.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Charge un utilisateur de l'application pour Spring Security à partir de son login.
 *
 * <p>Le profil ({@code RESPONSABLE_EXPLOITATION} / {@code AGENT}) devient un rôle
 * Spring Security (préfixé {@code ROLE_}), ce qui permet d'appliquer les
 * restrictions côté serveur (RG11).</p>
 */
@Service
public class UtilisateurDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu : " + login));
        return User.withUsername(utilisateur.getLogin())
                .password(utilisateur.getMotDePasseHache())
                .roles(utilisateur.getProfil().name())
                .build();
    }
}
