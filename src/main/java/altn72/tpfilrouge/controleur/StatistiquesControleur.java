package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.service.StatistiquesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Page de visualisation des données (lot alpha). Sous {@code /supervision/**},
 * donc réservée au responsable d'exploitation côté serveur (RG11).
 */
@Controller
public class StatistiquesControleur {

    private final StatistiquesService statistiquesService;

    public StatistiquesControleur(StatistiquesService statistiquesService) {
        this.statistiquesService = statistiquesService;
    }

    @GetMapping("/supervision/statistiques")
    public String afficherStatistiques(Model model) {
        model.addAttribute("incidentsParStation", statistiquesService.compterIncidentsActifsParStation());
        model.addAttribute("equipementsParType", statistiquesService.compterEquipementsParType());
        model.addAttribute("notesPrestataires", statistiquesService.calculerNoteMoyenneParPrestataire());
        return "supervision/statistiques";
    }
}
