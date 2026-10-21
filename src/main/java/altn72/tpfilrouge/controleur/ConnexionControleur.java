package altn72.tpfilrouge.controleur;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Affiche l'écran de connexion (F1). La soumission du formulaire et la
 * déconnexion sont prises en charge par Spring Security.
 */
@Controller
public class ConnexionControleur {

    @GetMapping("/connexion")
    public String afficherConnexion() {
        return "connexion";
    }
}
