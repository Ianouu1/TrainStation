package altn72.trainstation.controleur;

import altn72.trainstation.service.EquipementService;
import altn72.trainstation.service.StationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Pages liées aux stations et à leurs équipements (F2).
 */
@Controller
public class StationControleur {

    private final StationService stationService;
    private final EquipementService equipementService;

    public StationControleur(StationService stationService, EquipementService equipementService) {
        this.stationService = stationService;
        this.equipementService = equipementService;
    }

    @GetMapping("/")
    public String afficherAccueil() {
        return "redirect:/stations";
    }

    @GetMapping("/stations")
    public String listerStations(Model model) {
        model.addAttribute("stations", stationService.listerStations());
        return "stations/liste";
    }

    @GetMapping("/stations/{id}")
    public String afficherStation(@PathVariable("id") Integer idStation, Model model) {
        model.addAttribute("station", stationService.obtenirStation(idStation));
        model.addAttribute("equipements", equipementService.listerEquipementsParStation(idStation));
        return "stations/detail";
    }
}
