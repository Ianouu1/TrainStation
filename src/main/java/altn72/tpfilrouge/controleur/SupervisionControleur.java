package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.dto.LigneSupervisionDto;
import altn72.tpfilrouge.service.SupervisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Tableau de bord de supervision des incidents actifs (F11). Réservé au
 * responsable d'exploitation côté serveur (voir {@code ConfigurationSecurite}, RG11).
 */
@Controller
public class SupervisionControleur {

    private final SupervisionService supervisionService;

    public SupervisionControleur(SupervisionService supervisionService) {
        this.supervisionService = supervisionService;
    }

    @GetMapping("/supervision")
    public String afficherTableauDeBord(Model model) {
        List<LigneSupervisionDto> lignes = supervisionService.construireTableauDeBord();
        model.addAttribute("lignes", lignes);
        model.addAttribute("indicateurs", supervisionService.calculerIndicateurs(lignes));
        return "supervision/tableauDeBord";
    }
}
