package altn72.tpfilrouge.controleur;

import altn72.tpfilrouge.dto.CritereSupervisionForm;
import altn72.tpfilrouge.dto.LigneSupervisionDto;
import altn72.tpfilrouge.service.StationService;
import altn72.tpfilrouge.service.SupervisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Tableau de bord de supervision des incidents actifs (F11), avec filtres
 * combinables et tri (lot alpha, supervision avancée). Réservé au responsable
 * d'exploitation côté serveur (voir {@code ConfigurationSecurite}, RG11).
 */
@Controller
public class SupervisionControleur {

    private final SupervisionService supervisionService;
    private final StationService stationService;

    public SupervisionControleur(SupervisionService supervisionService, StationService stationService) {
        this.supervisionService = supervisionService;
        this.stationService = stationService;
    }

    @GetMapping("/supervision")
    public String afficherTableauDeBord(@RequestParam(required = false) Integer idStation,
                                @RequestParam(required = false) String niveau,
                                @RequestParam(required = false) String tri,
                                Model model) {
        CritereSupervisionForm critere = new CritereSupervisionForm(idStation, niveau, tri);
        List<LigneSupervisionDto> lignes = supervisionService.construireTableauDeBord(critere);
        model.addAttribute("lignes", lignes);
        model.addAttribute("indicateurs", supervisionService.calculerIndicateurs(lignes));
        model.addAttribute("stations", stationService.listerStations());
        model.addAttribute("critere", critere);
        return "supervision/tableauDeBord";
    }
}
