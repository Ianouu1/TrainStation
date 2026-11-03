package altn72.trainstation.controleur;

import altn72.trainstation.service.PrestataireService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Liste des prestataires avec leur note moyenne (F10).
 */
@Controller
public class PrestataireControleur {

    private final PrestataireService prestataireService;

    public PrestataireControleur(PrestataireService prestataireService) {
        this.prestataireService = prestataireService;
    }

    @GetMapping("/prestataires")
    public String listerPrestataires(Model model) {
        model.addAttribute("prestataires", prestataireService.listerPrestatairesAvecNoteMoyenne());
        return "prestataires/liste";
    }
}
