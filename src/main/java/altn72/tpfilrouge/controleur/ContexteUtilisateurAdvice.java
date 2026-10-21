package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.service.UtilisateurService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Expose l'utilisateur connecté à toutes les vues web, pour l'en-tête
 * « Bonjour [Prénom] ([profil]) » (F1) et pour masquer les actions réservées.
 *
 * <p>Ce masquage est un simple confort d'affichage : la vraie restriction reste
 * appliquée côté serveur par Spring Security (RG11).</p>
 */
@ControllerAdvice(basePackages = "altn72.tpfilrouge.controleur")
public class ContexteUtilisateurAdvice {

    private static final String ROLE_RESPONSABLE = "ROLE_RESPONSABLE_EXPLOITATION";

    private final UtilisateurService utilisateurService;

    public ContexteUtilisateurAdvice(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /** Ajoute l'utilisateur connecté au modèle ; rien n'est ajouté sur la page de connexion. */
    @ModelAttribute
    public void ajouterUtilisateurConnecte(Authentication authentication, Model model) {
        if (estAuthentifie(authentication)) {
            utilisateurService.trouverParLogin(authentication.getName())
                    .ifPresent(utilisateur -> model.addAttribute("utilisateurConnecte", utilisateur));
        }
    }

    @ModelAttribute("estResponsable")
    public boolean estResponsable(Authentication authentication) {
        if (!estAuthentifie(authentication)) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(autorite -> ROLE_RESPONSABLE.equals(autorite.getAuthority()));
    }

    private boolean estAuthentifie(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
