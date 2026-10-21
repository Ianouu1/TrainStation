package altn72.tpfilrouge.controleur;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Page affichée lorsqu'un utilisateur authentifié tente une action réservée à
 * un autre profil (RG11). La restriction est appliquée par Spring Security ;
 * cette page ne fait que présenter un message clair.
 */
@Controller
public class AccesRefuseControleur {

    @GetMapping("/acces-refuse")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String afficherAccesRefuse(Model model) {
        model.addAttribute("message", "Vous n'avez pas les droits nécessaires pour cette action.");
        return "error";
    }
}
